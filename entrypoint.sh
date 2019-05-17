#!/bin/sh
set -x

# configure script to call original entrypoint
set -- tini -- /usr/local/bin/jenkins.sh "$@"

# get gid of docker socket file
SOCK_DOCKER_GID=`ls -ng /var/run/docker.sock | cut -f3 -d' '`

# get group of docker inside container
CUR_DOCKER_GID=`getent group docker | cut -f3 -d: || true`

# if they don't match, adjust
if [ ! -z "$SOCK_DOCKER_GID" -a "$SOCK_DOCKER_GID" != "$CUR_DOCKER_GID" ]; then
  groupmod -g ${SOCK_DOCKER_GID} -o docker
fi

# Add call to su-exec to drop from root user to jenkins user when running original entrypoint
set -- su-exec jenkins "$@"

# replace the current pid 1 with original entrypoint
exec "$@"

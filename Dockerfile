FROM jenkins/jenkins:lts-alpine
LABEL maintainer "Gustavo Muniz do Carmo <gustavo@esign.com.br>"

ARG JMX_EXPORTER_VERSION=0.14.0
ARG GECKODRIVER_VERSION=0.28.0

USER root
RUN mkdir /usr/bin/jmx_exporter \
 && wget -O /usr/bin/jmx_exporter/jmx_prometheus_javaagent.jar https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/$JMX_EXPORTER_VERSION/jmx_prometheus_javaagent-$JMX_EXPORTER_VERSION.jar \
 && echo "{}" > /usr/bin/jmx_exporter/config.yaml \
 && wget -c https://github.com/mozilla/geckodriver/releases/download/v$GECKODRIVER_VERSION/geckodriver-v$GECKODRIVER_VERSION-linux64.tar.gz -O - | tar -xzC /usr/local/bin \
 && apk add --no-cache build-base docker python3 python3-dev libffi-dev openssl-dev firefox-esr openrc shadow su-exec nodejs \
 && rc-update add docker boot \
 && usermod -aG docker jenkins

ENV JAVA_OPTS="-javaagent:/usr/bin/jmx_exporter/jmx_prometheus_javaagent.jar=8081:/usr/bin/jmx_exporter/config.yaml -Djenkins.install.runSetupWizard=false"
EXPOSE 8081

COPY scripts/*.groovy /usr/share/jenkins/ref/init.groovy.d/

COPY plugins.txt /usr/share/jenkins/ref
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
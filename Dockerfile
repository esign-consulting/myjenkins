FROM jenkins/jenkins:lts-alpine
LABEL maintainer "Gustavo Muniz do Carmo <gustavo@esign.com.br>"

USER root
RUN mkdir /usr/bin/jmx_exporter \
 && wget -O /usr/bin/jmx_exporter/jmx_prometheus_javaagent.jar https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.3.1/jmx_prometheus_javaagent-0.3.1.jar \
 && echo "{}" > /usr/bin/jmx_exporter/config.yaml \
 && wget -c https://github.com/mozilla/geckodriver/releases/download/v0.24.0/geckodriver-v0.24.0-linux64.tar.gz -O - | tar -xzC /usr/local/bin \
 && apk add --no-cache build-base docker python3 python3-dev libffi-dev openssl-dev firefox-esr openrc shadow su-exec \
 && rc-update add docker boot \
 && usermod -aG docker jenkins \
 && pip3 install --no-cache-dir setuptools wheel \
 && pip3 install --no-cache-dir ansible jmespath boto boto3

ENV JAVA_OPTS="-javaagent:/usr/bin/jmx_exporter/jmx_prometheus_javaagent.jar=8081:/usr/bin/jmx_exporter/config.yaml -Djenkins.install.runSetupWizard=false"
EXPOSE 8081

COPY scripts/*.groovy /usr/share/jenkins/ref/init.groovy.d/

COPY plugins.txt /usr/share/jenkins/ref
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
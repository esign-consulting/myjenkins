FROM jenkins/jenkins:lts
LABEL maintainer "Gustavo Muniz do Carmo <gustavo@esign.com.br>"

USER root
RUN mkdir /usr/bin/jmx_exporter \
 && wget -O /usr/bin/jmx_exporter/jmx_prometheus_javaagent.jar https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.3.1/jmx_prometheus_javaagent-0.3.1.jar \
 && echo "{}" > /usr/bin/jmx_exporter/config.yaml \
 && curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - \
 && apt-get update && apt-get install -y --no-install-recommends software-properties-common apt-transport-https \
 && add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian stretch stable" \
 && apt-get update && apt-get install -y --no-install-recommends docker-ce python-pip python-jmespath \
 && usermod -aG docker jenkins \
 && pip install setuptools wheel \
 && pip install ansible \
 && rm -rf /var/lib/apt/lists/*
USER jenkins

ENV JAVA_OPTS="-javaagent:/usr/bin/jmx_exporter/jmx_prometheus_javaagent.jar=8081:/usr/bin/jmx_exporter/config.yaml -Djenkins.install.runSetupWizard=false"
EXPOSE 8081

COPY *.groovy /usr/share/jenkins/ref/init.groovy.d/

COPY plugins.txt /usr/share/jenkins/ref
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

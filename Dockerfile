FROM jenkins/jenkins:lts
LABEL maintainer "Gustavo Muniz do Carmo <gustavo@esign.com.br>"

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

COPY *.groovy /usr/share/jenkins/ref/init.groovy.d/

COPY plugins.txt /usr/share/jenkins/ref
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

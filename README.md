# Jenkins custom Docker image

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Docker Build status](https://img.shields.io/docker/build/esignbr/myjenkins.svg)](https://hub.docker.com/r/esignbr/myjenkins/builds) [![Docker Pulls](https://img.shields.io/docker/pulls/esignbr/myjenkins.svg)](https://hub.docker.com/r/esignbr/myjenkins)

This custom Jenkins image is built with the following features:

- [Default administrator](default-user.groovy) user whose credentials are defined through the environment variables **JENKINS_USER** and **JENKINS_PASS**. If no value is set to the these environment files, the default is *admin* for username and *jenkins* for password.
- [Default location configuration](config-location.groovy) through the definition of the environment variables **JENKINS_URL** and **JENKINS_EMAIL**;
- Optional [add of new global credentials](https://jenkins.io/doc/book/using/using-credentials/#adding-new-global-credentials), if defined the environment variables **CREDENTIALS_ID**, **CREDENTIALS_USER** and **CREDENTIALS_PASS**;
- Optional add AWS credentials, if defined the environment variables **AWS_CREDENTIALS_ID**, **AWS_ACCESS_KEY_ID** and **AWS_SECRET_ACCESS_KEY**;
- Optional [approval of in-process script or method signature](https://jenkins.io/doc/book/managing/script-approval), if defined the environment variable **SIGNATURE**;
- Integration with [SonarQube](https://www.sonarqube.org) made through the installation of the [SonarQube plugin](https://plugins.jenkins.io/sonar) and the configuration of the *SonarQube URL* defined by the environment variable **SONARQUBE_URL**.
- Integration with [Slack](https://slack.com/) made through the installation of the [Slack Notification plugin](https://plugins.jenkins.io/slack). Just set the *Slack Workspace* in the environment variable **SLACK_WORKSPACE**, and the *Integration Token* in the environment variable **SLACK_TOKEN**.
- [Apache Maven](https://maven.apache.org) automatic installation. The Maven version is get from the environment variable **MAVEN_VERSION**. If no value is set, *3.6.0* is assumed. Maven can then be referenced by **M3** in the Jenkinsfile.
- Integration with [jmx_exporter](https://github.com/prometheus/jmx_exporter), a process for exposing JMX Beans via HTTP for [Prometheus](https://prometheus.io) consumption. The JVM metrics are exposed through port 8081.
- [Firefox ESR](https://www.mozilla.org/en-US/firefox/organizations) installed, and [geckodriver](https://github.com/mozilla/geckodriver) available in `/usr/local/bin/geckodriver`, enabling UI tests with [Selenium](https://www.seleniumhq.org);
- [Security improved](harden-jenkins.groovy), through:
  - [Enabling CSRF protection](https://wiki.jenkins.io/display/JENKINS/CSRF+Protection);
  - [Disabling Jenkins CLI](https://support.cloudbees.com/hc/en-us/articles/234709648-Disable-Jenkins-CLI);
  - [Enabling Agent -> Master access control](https://wiki.jenkins.io/display/JENKINS/Slave+To+Master+Access+Control);
  - Disabling the deprecated [JNLP](https://en.wikipedia.org/wiki/Java_Web_Start#Java_Network_Launching_Protocol_(JNLP)).

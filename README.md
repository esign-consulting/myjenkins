# Jenkins custom Docker image

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Docker Build status](https://img.shields.io/docker/build/esignbr/myjenkins.svg)](https://hub.docker.com/r/esignbr/myjenkins/builds) [![Docker Pulls](https://img.shields.io/docker/pulls/esignbr/myjenkins.svg)](https://hub.docker.com/r/esignbr/myjenkins)

This custom Jenkins image is built with the following features:

- Default administrator user whose credentials are defined through the environment variables **JENKINS_USER** and **JENKINS_PASS**. If no value is set to the these environment files, the default is *admin* for username and *jenkins* for password.
- Integration with [SonarQube](https://www.sonarqube.org) made through the installation of the [SonarQube plugin](https://plugins.jenkins.io/sonar) and the configuration of the *SonarQube URL* defined by the environment variable **SONARQUBE_URL**.
- [Apache Maven](https://maven.apache.org) 3.5.4 automatic installation. Maven can then be referenced by **M3** in the Jenkinsfile.
- Integration with [jmx_exporter](https://github.com/prometheus/jmx_exporter), a process for exposing JMX Beans via HTTP for Prometheus consumption. The JVM metrics are exposed through port 8081.

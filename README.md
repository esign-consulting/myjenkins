# Jenkins custom Docker image

This custom Jenkins image is built with the following features:

- Default administrator user whose credentials are defined through the environment variables **JENKINS_USER** and **JENKINS_PASS**
- Integration with [SonarQube](https://www.sonarqube.org) made through the installation of the [SonarQube plugin](https://plugins.jenkins.io/sonar) and the configuration of the SonarQube URL defined by the environment variable **SONARQUBE_URL**

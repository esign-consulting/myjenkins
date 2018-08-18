# Jenkins custom Docker image

This custom Jenkins image is built with the following features:

- Default administrator user whose credentials are defined through the environment variables **JENKINS_USER** and **JENKINS_PASS**.
- Integration with [SonarQube](https://www.sonarqube.org) made through the installation of the [SonarQube plugin](https://plugins.jenkins.io/sonar) and the configuration of the *SonarQube URL* defined by the environment variable **SONARQUBE_URL**.
- [Apache Maven](https://maven.apache.org) 3.5.4 automatic installation. Maven can then be referenced by **M3** in the Jenkinsfile.

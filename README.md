# Jenkins custom Docker image

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Docker Build status](https://img.shields.io/docker/build/esignbr/myjenkins.svg)](https://hub.docker.com/r/esignbr/myjenkins/builds) [![Docker Pulls](https://img.shields.io/docker/pulls/esignbr/myjenkins.svg)](https://hub.docker.com/r/esignbr/myjenkins)

This custom Jenkins image is built with the following features:

- [Pre-installed plugins](#pre-installed-plugins)
- [Default administrator](#default-administrator)
- [No setup wizard](#no-setup-wizard)
- [Default location configuration](#default-location-configuration)
- [Default Maven installation](#default-maven-installation)
- [Adding a global credentials](#adding-a-global-credentials)
- [Adding an AWS credentials](#adding-an-aws-credentials)
- [In-process Script Approval](#in-process-script-approval)
- [Integration with Bitbucket](#integration-with-bitbucket)
- [Integration with SonarQube](#integration-with-sonarqube)
- [Integration with Slack](#integration-with-slack)
- [JVM Metrics](#jvm-metrics)
- [UI tests capability](#ui-tests-capability)
- [Jenkins hardening](#jenkins-hardening)

## Preinstalled plugins

A bunch of [plugins](plugins.txt) are installed during the image build. Once they increase the image size considerably, the [Alpine](https://alpinelinux.org) version of the Jenkins Docker image is used as the base image ([jenkins/jenkins:lts-alpine](https://github.com/jenkinsci/docker/blob/master/Dockerfile-alpine)), in order to keep the built image as small as possible.

Most of the plugins enable other features, as described below.

## Default administrator

The default Jenkins administrator account is created during the execution of [default-user.groovy](scripts/default-user.groovy). Its credentials are obtained from the environment variables:

- JENKINS_USER (default: admin)
- JENKINS_PASS (default: jenkins)

## No setup wizard

With preinstalled plugins and a default administrator, there is no need of following the Jenkins wizard during its setup. For this reason, the wizard is disabled: `-Djenkins.install.runSetupWizard=false`.

## Default location configuration

The default Jenkins location is configured during the execution of [config-location.groovy](scripts/config-location.groovy). The Jenkins URL and the e-mail address Jenkins use as the sender for e-mail notification are obtained from the environment variables:

- JENKINS_URL (default: <http://localhost:8080>)
- JENKINS_EMAIL (default: jenkins@example.com)

## Default Maven installation

The default [Apache Maven](https://maven.apache.org) installation is configured during the execution of [config-maven.groovy](scripts/config-maven.groovy). The Maven version is obtained from the environment variable:

- MAVEN_VERSION (default: 3.6.0)

Maven can then be referenced by `M3` in the Jenkinsfile, like in the example below:

```groovy
node {
    def mvnHome = tool 'M3'
    ...
    stage('Build and Unit Tests') {
        sh "'${mvnHome}/bin/mvn' clean install"
    }
    ...
}
```

## Adding a global credentials

[A new global credentials can be created in Jenkins](https://jenkins.io/doc/book/using/using-credentials/#adding-new-global-credentials) with the execution of [add-credentials.groovy](scripts/add-credentials.groovy). It only happens if the following environment variables are defined:

- CREDENTIALS_ID
- CREDENTIALS_USER
- CREDENTIALS_PASS

The global credentials can then be referenced by its `credentialsId` in the Jenkinsfile, like in the example below:

```groovy
node {
    ...
    stage('Deploy Java Artifacts') {
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'ossrh', usernameVariable: 'OSSRH_USER', passwordVariable: 'OSSRH_PASSWORD']]) {
            sh "'${mvnHome}/bin/mvn' -s .travis.settings.xml source:jar deploy -DskipTests=true"
        }
    }
    ...
}
```

## Adding an AWS credentials

A new AWS credentials can be created in Jenkins with the execution of [add-aws-credentials.groovy](scripts/add-credentials.groovy). It only happens if the following environment variables are defined:

- AWS_CREDENTIALS_ID
- AWS_ACCESS_KEY_ID
- AWS_SECRET_ACCESS_KEY

The AWS credentials can then be referenced by its `credentialsId` in the Jenkinsfile, like in the example below:

```groovy
node {
    ...
    stage('Deploy to AWS') {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
            ansiblePlaybook(playbook: 'deploy-to-aws.yml')
        }
    }
    ...
}
```

This feature is enabled by the [CloudBees AWS Credentials plugin](https://plugins.jenkins.io/aws-credentials).

## In-process Script Approval

[An in-process script or a method signature can be approved in Jenkins](https://jenkins.io/doc/book/managing/script-approval) with the execution of [approve-signature.groovy](scripts/approve-signature.groovy). It only happens if the following environment variable is defined:

- SIGNATURE

## Integration with Bitbucket

Jenkins can be integrated to [Bitbucket](https://www.atlassian.com/software/bitbucket) with the execution of [config-bitbucket.groovy](scripts/config-bitbucket.groovy). It only happens if the following environment variables are defined:

- BITBUCKET_URL - the endpoint where your Bitbucket instance is available
- BITBUCKET_CREDENTIALS_ID - the global credentials id previously added to Jenkins with the Bitbucket *username* and *password*

This feature is enabled by the [Bitbucket plugin](https://plugins.jenkins.io/bitbucket).

## Integration with SonarQube

Jenkins can be integrated to [SonarQube](https://www.sonarqube.org) with the execution of [config-sonarqube.groovy](scripts/config-sonarqube.groovy). It only happens if the following environment variable is defined:

- SONARQUBE_URL - the endpoint where your SonarQube instance is available

SonarQube can then be referenced by `SonarQube` in the Jenkinsfile, like in the example below:

```groovy
node {
    ...
    stage('Static Code Analysis') {
        withSonarQubeEnv('SonarQube') {
            sh "'${mvnHome}/bin/mvn' sonar:sonar"
        }
    }
    ...
}
```

This feature is enabled by the [SonarQube plugin](https://plugins.jenkins.io/sonar).

## Integration with Slack

Jenkins can be integrated to [Slack](https://slack.com) with the execution of [config-slack.groovy](scripts/config-slack.groovy). It only happens if the following environment variables are defined:

- SLACK_WORKSPACE
- SLACK_TOKEN

Slack can then be used in the Jenkinsfile, like in the example below:

```groovy
node {
    ...
    stage('Results') {
        ...
        slackSend(channel: 'builds', message: 'Pipeline succeed!', color: 'good')
    }
}
```

This feature is enabled by the [Slack Notification plugin](https://plugins.jenkins.io/slack).

## JVM Metrics

The Jenkins JVM metrics are exposed by [jmx_exporter](https://github.com/prometheus/jmx_exporter), a process for exposing JMX Beans via HTTP for [Prometheus](https://prometheus.io) consumption. The JVM metrics are exposed through port **8081**, as passed to the Java Agent:

`-javaagent:/usr/bin/jmx_exporter/jmx_prometheus_javaagent.jar=8081:/usr/bin/jmx_exporter/config.yaml`

## UI tests capability

The Jenkins image has installed [Firefox ESR](https://www.mozilla.org/en-US/firefox/organizations) and [geckodriver](https://github.com/mozilla/geckodriver) (available in `/usr/local/bin/geckodriver`), enabling that way UI tests with [Selenium](https://www.seleniumhq.org).

The example below shows how the UI test can be performed during the execution of your Jenkinsfile:

```groovy
node {
    ...
    stage('UI Tests') {
        sh "'${mvnHome}/bin/mvn' -f test-selenium test -Dwebdriver.gecko.driver=/usr/local/bin/geckodriver -Dheadless=true"
    }
    ...
}
```

## Jenkins hardening

The Jenkins security is improved during the execution of [harden-jenkins.groovy](scripts/harden-jenkins.groovy), when the following actions are taken:

- [Enabling CSRF protection](https://wiki.jenkins.io/display/JENKINS/CSRF+Protection);
- [Disabling Jenkins CLI](https://support.cloudbees.com/hc/en-us/articles/234709648-Disable-Jenkins-CLI);
- [Enabling Agent -> Master access control](https://wiki.jenkins.io/display/JENKINS/Slave+To+Master+Access+Control);
- Disabling the deprecated [JNLP](https://en.wikipedia.org/wiki/Java_Web_Start#Java_Network_Launching_Protocol_(JNLP)).

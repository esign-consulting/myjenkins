#!groovy

import jenkins.model.Jenkins
import org.jenkinsci.plugins.authorizeproject.*
import org.jenkinsci.plugins.authorizeproject.strategy.*
import jenkins.security.QueueItemAuthenticatorConfiguration

def jenkins = Jenkins.getInstance()

// Define which strategies you want to allow to be set per project
def strategyMap = [
    (jenkins.getDescriptor(AnonymousAuthorizationStrategy.class).getId()): true, 
    (jenkins.getDescriptor(TriggeringUsersAuthorizationStrategy.class).getId()): true,
    (jenkins.getDescriptor(SpecificUsersAuthorizationStrategy.class).getId()): true,
    (jenkins.getDescriptor(SystemAuthorizationStrategy.class).getId()): false
]

def authenticators = QueueItemAuthenticatorConfiguration.get().getAuthenticators()
def configureProjectAuthenticator = true

// only add if it does not already exist
for (authenticator in authenticators) {
    if (authenticator instanceof ProjectQueueItemAuthenticator)
        configureProjectAuthenticator = false
}

if (configureProjectAuthenticator) 
    authenticators.add(new ProjectQueueItemAuthenticator(strategyMap))

jenkins.save()

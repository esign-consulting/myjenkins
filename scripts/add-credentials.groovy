#!groovy

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import hudson.util.Secret

def credentialsId = System.getenv("CREDENTIALS_ID")
def credentialsUsername = System.getenv("CREDENTIALS_USER")
def credentialsPassword = System.getenv("CREDENTIALS_PASS")
if (credentialsId == null || credentialsUsername == null || credentialsPassword == null)
    return 0

Credentials credentials = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, credentialsId, null, credentialsUsername, credentialsPassword)
SystemCredentialsProvider provider = SystemCredentialsProvider.getInstance()
def credentialsList = provider.getCredentials()
if (credentialsList.contains(credentials)) {
    def index = credentialsList.indexOf(credentials)
    def currentCredentials = credentialsList.get(index)
    def currentUsername = currentCredentials.getUsername()
    def currentPassword = currentCredentials.getPassword()
    if (credentialsUsername == currentUsername && Secret.fromString(credentialsPassword) == currentPassword)
        return 0

    provider.getStore().updateCredentials(Domain.global(), currentCredentials, credentials)
} else {
    provider.getStore().addCredentials(Domain.global(), credentials)
}
return 1

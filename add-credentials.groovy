import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl

def credentialsId = System.getenv("CREDENTIALS_ID")
def credentialsUsername = System.getenv("CREDENTIALS_USER")
def credentialsPassword = System.getenv("CREDENTIALS_PASS")
if (credentialsId == null || credentialsUsername == null || credentialsPassword == null)
    return

Credentials credentials = new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, credentialsId, null, credentialsUsername, credentialsPassword)
SystemCredentialsProvider.getInstance().getStore().addCredentials(Domain.global(), credentials)

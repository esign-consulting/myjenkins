#!groovy

import jenkins.model.Jenkins
import com.cloudbees.jenkins.plugins.bitbucket.endpoints.BitbucketEndpointConfiguration
import com.cloudbees.jenkins.plugins.bitbucket.endpoints.BitbucketServerEndpoint
import com.cloudbees.jenkins.plugins.bitbucket.server.BitbucketServerWebhookImplementation

def bitbucketUrl = System.getenv("BITBUCKET_URL")
def bitbucketCredentialsId = System.getenv("BITBUCKET_CREDENTIALS_ID")
if (bitbucketUrl == null || bitbucketCredentialsId == null)
    return

Jenkins jenkins = Jenkins.getInstance()
BitbucketEndpointConfiguration bitbucketConfig = jenkins.getDescriptor(BitbucketEndpointConfiguration.class)
if (bitbucketConfig.findEndpoint(bitbucketUrl) != null)
    return

BitbucketServerEndpoint endpoint = new BitbucketServerEndpoint("Bitbucket", bitbucketUrl, true, bitbucketCredentialsId)
endpoint.setWebhookImplementation(BitbucketServerWebhookImplementation.NATIVE)
bitbucketConfig.addEndpoint(endpoint)

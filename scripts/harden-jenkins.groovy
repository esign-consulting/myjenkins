#!groovy

import jenkins.model.Jenkins
import hudson.security.csrf.DefaultCrumbIssuer

Jenkins jenkins = Jenkins.getInstance()

// CSRF protection
jenkins.setCrumbIssuer(new DefaultCrumbIssuer(true))

// Disable old Non-Encrypted protocols
HashSet<String> newProtocols = new HashSet<>(jenkins.getAgentProtocols());
newProtocols.removeAll(Arrays.asList("JNLP2-connect", "JNLP-connect"));
jenkins.setAgentProtocols(newProtocols);
jenkins.save()

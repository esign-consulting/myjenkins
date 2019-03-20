#!groovy

import jenkins.model.Jenkins
import jenkins.security.s2m.*
import hudson.security.csrf.DefaultCrumbIssuer

Jenkins jenkins = Jenkins.getInstance()

// CSRF protection
jenkins.setCrumbIssuer(new DefaultCrumbIssuer(true))

// Disable CLI remoting
jenkins.getDescriptor("jenkins.CLI").get().setEnabled(false)

// Enable Agent to master security subsystem
jenkins.injector.getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false);

// Disable old Non-Encrypted protocols
HashSet<String> newProtocols = new HashSet<>(jenkins.getAgentProtocols());
newProtocols.removeAll(Arrays.asList("JNLP2-connect", "JNLP-connect"));
jenkins.setAgentProtocols(newProtocols);
jenkins.save()

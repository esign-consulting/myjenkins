#!groovy

import org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval

def signature = System.getenv("SIGNATURE")
if (signature == null)
    return

def scriptApproval = ScriptApproval.get()
if (scriptApproval.getApprovedSignatures().contains(signature))
    return

scriptApproval.approveSignature(signature)

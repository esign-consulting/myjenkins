#!groovy

import hudson.tools.CommandInstaller
import hudson.tools.InstallSourceProperty
import hudson.tools.ToolProperty
import hudson.tools.ToolPropertyDescriptor
import hudson.util.DescribableList
import jenkins.model.Jenkins
import org.jenkinsci.plugins.ansible.AnsibleInstallation

CommandInstaller ansibleInstaller = new CommandInstaller(null,
	'''test -e ~/.local/bin/ansible \
		|| (curl https://bootstrap.pypa.io/get-pip.py -o ~/get-pip.py \
			&& python3 ~/get-pip.py --user \
			&& ~/.local/bin/pip install ansible --user \
			&& ln -s ~/.local/bin/ansible-playbook ~/tools/org.jenkinsci.plugins.ansible.AnsibleInstallation/Ansible/ansible-playbook)''',
	'.')
InstallSourceProperty installSourceProperty = new InstallSourceProperty()
installSourceProperty.installers.add(ansibleInstaller)

DescribableList properties = new DescribableList<ToolProperty<?>, ToolPropertyDescriptor>()
properties.add(installSourceProperty)

AnsibleInstallation.DescriptorImpl ansibleDescriptor = Jenkins.getActiveInstance().getDescriptorByType(AnsibleInstallation.DescriptorImpl.class);
AnsibleInstallation[] installations = [new AnsibleInstallation('Ansible', '', properties)]
ansibleDescriptor.setInstallations(installations)
ansibleDescriptor.save()

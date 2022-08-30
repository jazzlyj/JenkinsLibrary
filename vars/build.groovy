
def build(ArrayList commands = []) {
	nvmBuild(commands)
}


/**
 *  Function to run the commands using specific node version.
 *
 *  @param commands which is a list of string that contains commands
 *         for execution
 *
 *  @return void.
 */
def nvmBuild(ArrayList commands = [],String nodeVer=null){
	/**
	 *  Node Version used with command execution. It is read either from environment variable
	 *  or from globalsettings.json
	 */
	def nodeVersion = nodeVer ? nodeVer : util.getResourceValue("nodeVersion")
	/**
	 *  NVM install URL used with command execution. It is read either from environment variable
	 *  or from globalsettings.json
	 */
	def nvmInstallURL = env.nvmInstallURL ? env.nvmInstallURL : util.getResourceValue("nvmInstallURL")
	/**
	 *  NVM IO Org Mirron URL used with command execution. It is read either from environment variable
	 *  or from globalsettings.json
	 */
	def nvmIoJsOrgMirror = env.nvmIoJsOrgMirror ? env.nvmIoJsOrgMirror : util.getResourceValue("nvmIoJsOrgMirror")
	/**
	 *  NVM Node Org Mirron URL used with command execution. It is read either from environment variable
	 *  or from globalsettings.json
	 */
	def nvmNodeJsOrgMirror = env.nvmNodeJsOrgMirror ? env.nvmNodeJsOrgMirror : util.getResourceValue("nvmNodeJsOrgMirror")

		nvm(nvmInstallURL: nvmInstallURL,
		nvmIoJsOrgMirror: nvmIoJsOrgMirror,
		nvmNodeJsOrgMirror: nvmNodeJsOrgMirror,version: nodeVersion){
			commands.each {
				sh "${it}"
			}
		}
}
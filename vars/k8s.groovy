/**
 * Function to deploy the application in kubernetes cluster.
 *
 * @param prjName, String for project name
 * @param appType, String for application type
 *
 * @return void.
 */
def deploy(String prjName, String appType) {
	echo "Deploying $appType"
	redeploy(prjName, appType)
}

/**
 * Function to deploy the application in kubernetes cluster.
 *
 * @param prjName, String for project name
 * @param deploymentType, String for application type
 *
 * @return void.
 */
def redeploy(String namespace, String appName){
    def branch = BRANCH_NAME
    def kubecfg = getKubeCfg()
    echo 'Redeploying in K8s (' + kubecfg + ')...'
    def script = libraryResource 'redeploy.sh'
    writeFile file:'redeploy.sh', text: script
    return sh(script: "sh redeploy.sh $appName $kubecfg $namespace ", returnStdout: true).trim()
}

/**
 * Function to get kubernetes configuration details based on the branch.
 *
 * @return configuration path.
 */
def getKubeCfg(){
    def kubecfg
        kubecfg = libraryResource 'k8s-prod-kubecfg'
	writeFile file:'kubecfg-out', text: kubecfg
	return './kubecfg-out'
}

/**
 * Function to prepare project name.
 *
 * @param prjName, String for project name
 * @param deploymentType, String for deployment type
 *
 * @return projectName.
 */
def getWorkloadName(String deploymentType){
	def branch = BRANCH_NAME
	def projectName = deploymentType + getBranchSuffix()

	return projectName
}

/**
 * Function to get the deployment namespace.
 *
 * @param prjName, String for project name
 *
 * @return namespace.
 */
def getDeployNamespace(String prjName){
	def branch = BRANCH_NAME
	def  namespace = prjName + getBranchSuffix()

	return namespace
}

/**
 * Function to get the deployment YML file path.
 *
 * @param prjName, String for project name
 * @param deploymentType, String for deployment type
 *
 * @return path
 */
def getDeploymentYMLPath(String prjName, String deploymentType){
	def branch = BRANCH_NAME
	def path = 'cicd/deployments/' + prjName + '-' + deploymentType
	path = path + getBranchSuffix() + '.yml'

	return path
}

/**
 * Function for preparing branch suffix.
 *
 * @return suffix.
 */
def getBranchSuffix() {
	def branch = BRANCH_NAME
	def suffix = ""
	if(branch == 'develop') {
		suffix = '-dev'
	} else if (branch == 'stage') {
		suffix = '-stg'
	} else if (branch == 'master') {
		suffix = ""
	} else {
		suffix = '-feature'
	}

	return suffix
}
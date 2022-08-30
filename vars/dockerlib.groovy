def buildAndPublishWithDockerFile(prjName, dockerRegistryPath, dockerfilePath, version) {
  echo "Building Docker image for $prjName"
  dockerRegistry = util.getResourceValue("dockerRegistry")
  dockerRegistryCredentials = util.getResourceValue("dockerRegistryCredentials")
  dockerImageBuildServer = util.getResourceValue("dockerImageBuildServer")
  dockerOrganizationPath = util.getResourceValue("dockerOrganizationPath")

  docker.withRegistry(dockerRegistry, dockerRegistryCredentials) {
    docker.withServer(dockerImageBuildServer){
      path = "${dockerRegistryPath}:${version}"
      image = docker.build(path, "-f $dockerfilePath .")
      image.push()
      // image.pull()
      // image.push('latest')
    }
  }
}

/**
 * Function to build the docker image and push the latest as well as tagged images.
 *
 * @param prjName, String parameter  for project name
 * @param appName, String parameter for application name
 * @param appType, String parameter for application type
 *
 * @return void.
 */

def buildAndPublish(prjName, appName) {
  echo "Building Docker image for $appName"
  dockerRegistry = util.getResourceValue("dockerRegistry")
  dockerRegistryCredentials = util.getResourceValue("dockerRegistryCredentials")
  dockerImageBuildServer = util.getResourceValue("dockerImageBuildServer")
  dockerOrganizationPath = util.getResourceValue("dockerOrganizationPath")
  buildAndPublishWithDockerFile(
    prjName,
    "${dockerOrganizationPath}/${getProjectDockerPath(prjName, appName)}",
    "./cicd/Dockerfile-$appName",
    getDockerBuildNumber()
  )
  // docker.withRegistry(dockerRegistry, dockerRegistryCredentials) {
  //   docker.withServer(dockerImageBuildServer){
  //     path = "${dockerOrganizationPath}/${getProjectDockerPath(prjName, appName)}:${getDockerBuildNumber()}"
  //     image = docker.build(path, "-f ./cicd/Dockerfile-$appName .")
  //     image.push()
      // image.pull()
      // image.push('latest')
    // }
  // }
}

/**
 * Function for preparing the project name based on the branch and subject.
 *
 * @param prjName, String parameter for project name
 * @param sub, String parameter for subject/application type
 *
 * @return projectName.
 */
def getProjectDockerPath(prjName, sub){
	def branch =BRANCH_NAME
	projectName = prjName + '-' + sub

	if(branch == 'develop'){
		projectName = projectName + '-dev'
	} else if (branch == 'stage'){
		projectName = projectName + '-stg'
	} else if (branch == 'master'){
		projectName = projectName
	} else {
		projectName = projectName + '-feature'
	}

	return projectName
}

/**
 * Function for getting docker build number based on branch.
 *
 * @return buildNumber.
 */
def getDockerBuildNumber(){
	def branch = BRANCH_NAME

  def buildNumber = util.getTimestamp()

  packageVersion = util.getPackageVersion()

  if(packageVersion != false){
    if(branch == 'develop' || branch == 'stage' || branch == 'master'){
  		buildNumber = packageVersion

  	} else {
  		buildNumber = "${branch}.${packageVersion}"

  	}
  }

	return buildNumber
}
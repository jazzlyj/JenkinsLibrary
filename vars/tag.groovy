/**
 * Function for creating git tag.
 * 
 * @param msg,String parameter for git tag message
 * 
 * @return void.
 */
def call() {
	createGitTag(getTagName())
}

/**
 * Function to form the tag name.
 * 
 * @return tagName.
 */
def getTagName(){
	def branch = env.GIT_BRANCH_CLEAN
	def tagName = '';
	if(branch == 'develop' ||  branch == 'devops'){
		tagName = branch + '-' + util.getPackageVersion() + '-rc-' + env.BUILD_NUMBER
	} else if (branch == 'master'){
		tagName = util.getPackageVersion() + env.BUILD_NUMBER
	}
	return tagName
}

/**
 * Function to create git tag locally and then push it to the origin.
 * 
 * @param tagName, String parameter for creating a tag with specific name.
 * 
 * @return void.
 */
def createGitTag(tagName){
	echo "Tagging the repository with the tag name" + getTagName()
	sh 'git fetch --tags'
	sh 'git tag ' + tagName
	sh 'git tag'
	sh 'git push origin ' + tagName
}

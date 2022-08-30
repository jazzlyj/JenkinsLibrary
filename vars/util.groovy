/**
 * Function to retrieve the value of a resource from globalsettings.json.
 *
 * @param resourceName which is a String parameter whose value is to be retrieved
 *
 * @return String which is a value fo a resource.
 */
def String getResourceValue(String resourceName){
    def str = libraryResource 'globalsettings.json'
    def request = new groovy.json.JsonSlurper().parseText(str)
    return request."$resourceName"
}

def getPackageVersion(){
    try {
      def filePath = getWorkspacePath() + 'package.json'
      def f = readFile(filePath)
      def json = new groovy.json.JsonSlurper().parseText(f)
      return json."version"
    } catch (Exception ex) {
      echo "Error: ${ex}"
      return false
    }
}

def getTimestamp(){
  return new Date().getTime()
}

def getWorkspacePath(){
  return pwd() + File.separator
}
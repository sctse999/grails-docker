def supportedMajorVersions = ['2','3']

// Retrive all tags from grails-core repo
String cmd = "git ls-remote -t https://github.com/grails/grails-core.git"
def process = cmd.execute();

def versions = [];
process.text.eachLine() {
        def result = it =~ /v([0-9]*).[0-9]*.[0-9]*/

        def version = result[0][0].startsWith('v')?result[0][0].substring(1):result[0][0];
        def majorVersion = result[0][1]

        if (majorVersion in supportedMajorVersions) {
            // println "version : $version";
            // println "majorVersion : $majorVersion";
            versions << version;
        }


}
versions = versions.unique()

versions.each() { version->
    cmd = "mkdir ${version}";
    String dockerfile = generateDockerfile(version);

    String filePath = "${version}/Dockerfile"
    def file = new File(filePath)
    file.newWriter().withWriter { w->
        w << dockerfile
    }

    println "cp $filePath ./Dockerfile"
    "cp $filePath ./Dockerfile".execute().waitFor();
    execute("cat ./Dockerfile");

    'git status'.execute().text
    execute("git add .")
    execute("git commit -m '${version}'")
    execute("git tag -f ${version}");
    // cmd.execute();
}

def execute(String cmd) {
    def proc = cmd.execute();
    proc.waitFor();
    println "$cmd: " + proc.text
}

// cmd = " git push --tags"
// cmd.execute();


def generateDockerfile(String version) {

    String dockerfile = new File('./Dockerfile.tmpl').text
    dockerfile = dockerfile.replace('%%version%%', version)
    return dockerfile
}

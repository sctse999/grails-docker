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

    def folder = new File(version);
    if (!folder.exists()) {
        "mkdir ${version}".execute().waitFor();
        String dockerfile = generateDockerfile(version);

        String filePath = "${version}/Dockerfile"
        def file = new File(filePath)
        file.newWriter().withWriter { w->
            w << dockerfile
        }

        println "cp $filePath ./Dockerfile"
        "cp $filePath ./Dockerfile".execute().waitFor();

        execute("git add .")
        execute("git commit -m '${version}'")
        execute("git tag -f ${version}");
        execute("git push origin -f ${version}");
    } else {
        println "${version} directory exists, skipping";
    }



}

def execute(String cmd) {
    def proc = cmd.execute();
    proc.waitFor();
    println "$cmd: " + proc.text
}

def generateDockerfile(String version) {
    String templateFile;
    def majorVersion = version.take(1);

    if (majorVersion == "2") {
        templateFile = './Dockerfile.v2.tmpl'
    } else if (majorVersion == "3") {
        templateFile = './Dockerfile.v3.tmpl'
    }

    String dockerfile = new File(templateFile).text
    dockerfile = dockerfile.replace('%%version%%', version)
    return dockerfile
}


import groovy.text.*
// def yourString = 'asfas'
// matcher = (yourString =~ /\s(abc_.*)$/m)
// this is how you would extract the value from
// the matcher object
// matcher[0][1]


// String a = 'b96a2bcc20abc49ce56237a95d82aa43b9819484	refs/tags/v3.2.71'
        // def result = a =~ /v([0-9]*).[0-9]*.[0-9]*./
        // println result[0][0]


String source = new File('./Dockerfile.tmpl').text
source = source.replace('%%version%%', '1.2.344')
println source

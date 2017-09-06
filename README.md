# Grails-docker - Dockerized grails image

## What is Grails?
Grails is a powerful Groovy-based web application framework for the JVM built on top of Spring Boot. 

## What is grails docker?
Grails Docker is a dockerized images pre-installed with grails, derived from `openjdk` image. All grails version from grails `2.0.0` to grails `3.3.0` is built automatically on docker hub using automatic build. You can verify how the image is built by referring to the dockerfile in each version directory.

## Why would I need this image?
Normally you don't. To deploy a grails application, it is recommended that you use `grails war` to build a war file and deploy to container like tomcat. 

However, there is one case that you may want to use a dockerized grails image: When you want to setup a build pipeline to build a grails application *without* installing grails in your CI server (e.g. Jenkins). Instead, you use an grails docker to build the war file and assemble another image which contains your war file. This way, you remove grails dependency from your CI server. The CI server just spin off a grails docker container to build the war file and throw that container away. 

## How to use this image

`# docker run -it -v my-grails-app:/app proactivehk/grails:2.3.7 `
`# grails run-app`

## Advance usage

Use this together with a Jenkins pipeline file, Jenkinsfile. Refer to a sample project in this link(https://github.com/sctse999/grails-jenkins-pipeline)

# Feckless Weasel
[![Build Status](https://travis-ci.org/CatalystOfNostalgia/feckless-weasel.svg?branch=master)](https://travis-ci.org/CatalystOfNostalgia/feckless-weasel)
<<<<<<< 738c6f4d9d9f72dab8d4e1e12bb02bbc6702d4f0
[![Coverage Status](https://coveralls.io/repos/CatalystOfNostalgia/feckless-weasel/badge.svg?branch=master&service=github)](https://coveralls.io/github/CatalystOfNostalgia/feckless-weasel?branch=master)

##### Introduction

Feckless Weasel is a collaborative project for Case Western Reserve university's
Software Engineering class. At the moment, only user account database and object
model are complete.

##### One Stop Shop for Build and Deployment

Feckless Weasel can currently be built and deployed on Windows and OS X. Linux
deployment is planned for the future. Application uses a standard Gradle script
for building, however, it has a lot of dependencies that have to be handled, so
for this we have *One Stop Shop* scripts for deployment tasks on each platform.
The scripts are named *oss_osx.sh* and *oss_windows.bat* respectively and accept
more or less the same commands.
###### OSS Commands
OSS commands are the first argument after the script name on the command line.
e.g.:
```
oss_windows.bat *command*
```

OSS supports the following commands on each
platform:
* prereq: Installs all application prereqs and dependencies.
* deploy_database: Creates MySQL Database and Tables.
* build: Installs prereqs and compiles.
* test: Runs Gradle unit tests.
* clean: Runs Gradle clean.
* start_server: Starts the development server.
* stop_server: Stops the development server.
* deploy: Installs the stack in the Tomcat web server.

###### Workflow

The typical workflow for developers looks like this:
* Clone Repo
* Run *oss_[platform] build* to install dependencies and perform first build
* Run *oss_[platform] deploy* to deploy application and start server
* Run *oss_[platform] deploy_database* at least once to create database tables.
* Visit *http://localhost:8080* to see the applet.
* When done, run *oss_[platform] stop_server*

###### Building and Deploying on Windows
You build and run on Windows as described above. Simply running the OSS script
should be enough to set up the entire environment. If you have any problems
such as *missing cmd-let*, you might need to download a newer version of
Powershell from Microsoft. Also, the deploy scripts **MUST** be executed
from an Administrator command prompt or Powershell instance. You can launch
these as Admin by right clicking them in the start menu and selecting
*Run as Administrator*.

If you have other problems it is possible that the deployment script versions
are incorrect and need to be updated since paths in the OSS scripts are
hardcoded.

###### Building and Deploying on OS X

You build and run on OS X as described above. The script will automatically
install Brew and other dependencies. The only manual prereq is that you visit
Oracle's website and download the newest Java 8 JDK before running the OSS
script.

If the scripts do not operate as described, check to make sure that the paths
in the scripts match those of the version of mysql, jdk, mysql-java-connector,
and tomcat installed on your machine.

###### Building and Deploying on Other Platforms

You can currently build on any platform that supports Gradle but you will have to manually
deploy the various dependencies. Support for OS X and Linux deployment are planned and in
progress.

###### Database Deployment

Before running the first time be sure to run *oss_[platform] deploy_database* at least
once to create database tables. The database tables are the one portion of the service
that are not prereq checked each time you run. After deploying the database, the only
times you will need to run the command again are when you make database schema changes,
such as new tables. **NOTE: DB deployment wipes the database.**

###### Running Tests and Test Coverage

Unit test coverage for the master branch is displayed on the badge in the top of this
page. For more detailed information about coverage, run the tests with:
```
gradle test
```
And then analyze the code coverage with:
```
gradle jacocoTestReport
```

The coverage report is in *service/build/reports/jacoco/test/html/index.html*
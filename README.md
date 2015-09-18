# Feckless Weasel
###### Introduction

Feckless Weasel is a collaborative project for Case Western Reserve university's
Software Engineering class. At the moment, the repo is more or less empty.

###### Building and Deploying on Windows

Although there is currently no code, you can install the application dependencies
and frameworks with the provided *tracie* script. To install dependencies run *tracie prereq*.
To build, run *tracie build*. To clean, run *tracie clean*. The tool will automatically
install gradle, mysql, and all other dependencies. After the prereqs are installed you can
revert to using Gradle for building. Eventually there will be a *tracie deploy* option for
installing the service.

###### Building and Deploying on Other Platforms

You can currently build on any platform that supports Gradle but you will have to manually
deploy the various dependencies. Support for OS X and Linux deployment are planned and in
progress.
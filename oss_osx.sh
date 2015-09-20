#!/bin/bash
# One Stop Shop, The Feckless Weasel Deployment System for OS X
# By: Eric Luan and Christian Gunderman

function PrintHelp {
    clear
    echo "One Stop Shop, The Feckless Weasel Deployment System for OS X"
    echo "By: Eric Luan and Christian Gunderman"
    echo
    echo "Accepts the following commands:"
    echo "  prereq - Checks that all prereqs for deployment are met"
    echo "  build  - Compiles the project"
    echo "  test  - Tests the project"
    echo "  clean  - Cleans the project"
    echo "  start_server - Starts a local development server"
    echo "  stop_server - Stops a local development server"
    echo "  deploy - Installs the servlet and deploys it."
}

function CheckPrereqs {
    echo "Checking prereqs..."

    # Check to make sure brew exists.
    if hash brew 2>/dev/null; then
        echo "Brew already installed"
    else
        echo "Installing Brew package manager..."
        ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
    fi

    # Try reinstalling prereqs, if they are installed, brew won't reinstall.
    echo "Checking Tomcat8..."
    brew install tomcat
    echo "Checking Gradle..."
    brew install gradle
    echo "Checking Mysql..."
    brew install mysql
    echo "Checking JDBC..."
    brew tap gbeine/homebrew-java
    brew install mysql-connector-java
}

function StartServer {
    echo "Starting server..."
    /usr/local/Cellar/tomcat/8.0.21/bin/catalina start
    mysql.server start
}

function StopServer {
    echo "Stopping Server..."
    /usr/local/Cellar/tomcat/8.0.21/bin/catalina stop
    mysql.server stop
}

function Build {
    CheckPrereqs
    echo "Building..."
    gradle build
}

if [ "$#" -eq 1 ]; then
    if [ "$1" = "prereq" ]; then
        CheckPrereqs
        exit
    fi

    if [ "$1" == "start_server" ]; then  CheckPrereqs
        StartServer
        exit
    fi

    if [ "$1" == "deploy" ]; then
        echo "Not implemented yet."
        exit
    fi

    if [ "$1" == "stop_server" ]; then
        CheckPrereqs
        StopServer
        exit
    fi

    if [ "$1" == "build" ]; then
        Build
        exit
    fi

    if [ "$1" == "clean" ]; then
        gradle clean
        exit
    fi

    if [ "$1" == "test" ]; then
        gradle test
        exit
    fi
fi

# No commands match, print help.
PrintHelp

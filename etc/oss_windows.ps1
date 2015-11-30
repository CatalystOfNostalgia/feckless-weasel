# One Stop Shop, The Feckless Weasel Deployment System for Windows
# By: Christian Gunderman

$tomcatVersion = "v8.0.1"

function Write-Help()
{
    Clear-Host
    Write-Output "One Stop Shop, The Feckless Weasel Deployment System for Windows"
    Write-Output "By: Christian Gunderman"
    Write-Output ""
    Write-Output "Accepts the following commands:"
    Write-Output "  prereq - Checks that all prereqs for deployment are met"
    Write-Output "  deploy_database - Creates MySQL database and tables."
    Write-Output "  build  - Compiles the project"
    Write-Output "  test  - Tests the project"
    Write-Output "  clean  - Cleans the project"
    Write-Output "  start_server - Starts a local development server"
    Write-Output "  stop_server - Stops a local development server"
    Write-Output "  deploy - Installs the servlet and deploys it."
}

function Reload-Path()
{
    $env:Path = [System.Environment]::GetEnvironmentVariable("Path","Machine")
    $env:JAVA_HOME = [System.Environment]::GetEnvironmentVariable("JAVA_HOME","Machine")
}

function Check-Is-Admin()
{
    if (!([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole(
        [Security.Principal.WindowsBuiltInRole]"Administrator"))
    {
        Write-Warning "One Stop Shop must be run from an Administrative prompt."
        exit
    }
}

function Command-Exists([string]$command)
{
    if (Get-Command $command -ErrorAction SilentlyContinue)
    {
        return $true
    }

    return $false
}

function Install-Choco-If-Needed()
{
    Write-Output "Checking for Chocolately package manager..."
    if (Command-Exists "choco")
    {
        Write-Output("Chocolately package manager is already installed.")
    }
    else
    {
        Write-Output "Installing Chocolately package manager..."
        Invoke-Expression (new-object net.webclient).DownloadString('https://chocolatey.org/install.ps1')
        $env:Path += ";%ALLUSERSPROFILE%\chocolatey\bin"
    }
}

function Install-Gradle-If-Needed()
{
    Write-Output "Checking for Gradle build system..."
    if (Command-Exists "gradle")
    {
        Write-Output "Gradle build system is already installed."
    }
    else
    {
        Write-Output "Installing Gradle build system..."
        choco install --yes --force gradle
        Reload-Path
    }
}

function Install-JDK-If-Needed()
{
    Write-Output "Checking for Java Development kit..."
    if (([string]$env:JAVA_HOME).Length -ne 0)
    {
        Write-Output "Java Development Kit is already installed."
    }
    else
    {
        Write-Output "Installing Java Development Kit..."
        choco install --yes --force jdk8
        Reload-Path
    }
}

function Is-Installed($programName)
{
    $programs = (Get-ItemProperty HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\* | select DisplayName)

    $programs32 = (Get-ItemProperty HKLM:\SOFTWARE\Wow6432Node\Microsoft\Windows\CurrentVersion\Uninstall\* | select DisplayName)

    foreach ($program in $programs)
    {
        if (($program -ne $null))
        {
            if (($program.DisplayName -ne $null))
            {
                if (($program.DisplayName.Contains($programName)))
                {
                    return $true
                }
            }
        }
    }

    foreach ($program in $programs32)
    {
        if (($program -ne $null))
        {
            if (($program.DisplayName -ne $null))
            {
                if (($program.DisplayName.Contains($programName)))
                {
                    return $true
                }
            }
        }
    }
    
    return $false
}

function Install-Tomcat-If-Needed()
{
    # TODO: Use chocolately package manager. For now though chocolately is broken
    # so we'll download the installer directly.
    Write-Output "Checking for Apache Tomcat..."
    if (Is-Installed "Apache Tomcat")
    {
        Write-Output "Apache Tomcat is already installed."
    }
    else
    {
        Write-Output "Downloading Tomcat installer..."
        Invoke-WebRequest "http://archive.apache.org/dist/tomcat/tomcat-8/$tomcatVersion/bin/apache-tomcat-8.0.1.exe" -OutFile "tomcat.tmp.exe"
        Write-Output "Running Tomcat installer..."
        Start-Process "tomcat.tmp.exe" -ArgumentList "/S" -Wait
        Remove-Item "tomcat.tmp.exe"
    }
}

function Install-MySQL-If-Needed()
{
    Write-Output "Checking for MySQL database system..."
    if (Command-Exists "mysql")
    {
        Write-Output "MySQL database system is already installed."
    }
    else
    {
        Write-Output "Installing MySQL database system..."
        choco install --yes --force -version 5.6.26 mysql
        Set-Service MySQL -StartupType Manual
        Stop-Service MySQL
        Reload-Path
    }
}

function Install-MySQL-Connector-If-Needed()
{
    Write-Output "Checking for MySQL to Java connector..."
    if (Is-Installed "MySQL Connector J")
    {
        Write-Output "MySQL to Java connector is already installed."
    }
    else
    {
        Write-Output "Downloading MySQL to Java connector installer..."
        Invoke-WebRequest "http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-gpl-5.1.36.msi" -OutFile "mysql-connector.tmp.msi"
        Write-Output "Running MySQL to Java connector installer..."
        Start-Process "msiexec.exe" -ArgumentList "/package", "mysql-connector.tmp.msi", "/passive" -Wait
        Remove-Item "mysql-connector.tmp.msi"
    }

    Write-Output "Checking MySQL to Java connector present in Tomcat libs..."
    if (Test-Path "C:\Program Files\Apache Software Foundation\Tomcat 8.0\lib\mysql-connector-java.jar")
    {
        Write-Output "MySQL to Java connector is present in Tomcat libs."
    }
    else
    {
        Write-Output "Copying MySQL to Java connector to Tomcat libs."
        Copy-Item "C:\Program Files (x86)\MySQL\MySQL Connector J\*.jar" "C:\Program Files\Apache Software Foundation\Tomcat 8.0\lib\mysql-connector-java.jar"
    }

}

function Check-Prereqs()
{
    Write-Output "Checking deployment prereqs..."
    Install-Choco-If-Needed
    Install-Gradle-If-Needed
    Install-JDK-If-Needed
    Install-Tomcat-If-Needed
    Install-MySQL-If-Needed
    Install-MySQL-Connector-If-Needed
    Write-Output "Completed checking deployment prereqs."
}

function Set-Server-Running([bool]$isRunning)
{
    if ($isRunning)
    {
        Write-Output "Starting Tomcat Server..."
        Start-Service Tomcat8
        Write-Output "Starting MySQL..."
        Start-Service MySQL
    }
    else
    {
        Write-Output "Stopping Tomcat Server..."
        Stop-Service Tomcat8
        Write-Output "Stopping MySQL..."
        Stop-Service MySQL
    }
}

function Build-And-Deploy
{
    Check-Prereqs
    Set-Server-Running $false
    Write-Output "Deploying..."
    gradle assemble
    Remove-Item "C:\Program Files\Apache Software Foundation\Tomcat 8.0\webapps\ROOT.war" -ErrorAction SilentlyContinue
    Remove-Item "C:\Program Files\Apache Software Foundation\Tomcat 8.0\webapps\ROOT" -ErrorAction SilentlyContinue -Recurse
    Copy-Item "service\build\libs\service.war" "C:\Program Files\Apache Software Foundation\Tomcat 8.0\webapps\ROOT.war"
    Set-Server-Running $true
}

function Deploy-Database()
{
    Write-Output "Deploying MySQL database and tables..."

    # Pipe SQL statements from file into MySQL.
    Write-Output "source etc\db_schema_main.sql" | mysql --batch -u root

    Write-Output "MySQL database and table deployment completed."
}

function EntryPoint($cmdLine)
{
    Check-Is-Admin

    if ($cmdLine.Count -eq -not 0)
    {
        if ($cmdLine[0] -eq "prereq")
        {
            Check-Prereqs
            return
        }

        if ($cmdLine[0] -eq "deploy_database")
        {
            Check-Prereqs
            Deploy-Database
            return
        }

        if ($cmdLine[0] -eq "start_server")
        {
            Check-Prereqs
            Set-Server-Running($true)
            return;
        }

        if ($cmdLine[0] -eq "deploy")
        {
            Build-And-Deploy
            return
        }

        if ($cmdLine[0] -eq "stop_server")
        {
            Check-Prereqs
            Set-Server-Running($false)
            return
        }

        if ($cmdLine[0] -eq "build")
        {
            Check-Prereqs
            $(gradle build)
            return
        }

        if ($cmdLine[0] -eq "clean")
        {
            $(gradle clean)
            return
        }

        if ($cmdLine[0] -eq "test")
        {
            Check-Prereqs
            Write-Output $(gradle test)
            return
        }
    }

    Write-Help
}

# Launches script main function.
EntryPoint $args
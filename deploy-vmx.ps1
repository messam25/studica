# Deploy robot JAR to Studica VMX via SCP/SSH (user pi, password: raspberry).
#
# Prereqs:
#   1. PC on VMX Wi-Fi (Worldskills-1234 or VMX-1234) or Ethernet.
#   2. OpenSSH client (Windows: Settings > Apps > Optional features > Open SSH Client).
#   3. JAR built for VMX (raspbian). With this repo's default build, the JAR is for roboRIO and will NOT run on the Pi.
#      To get a VMX JAR: open project in WPILib VS Code, run "Change deploy target to VMX-Pi", then "WPILib: Build Robot Code".
#      Then run this script to copy that JAR to the robot.
#
# Usage: .\deploy-vmx.ps1   (or set $env:VMX_IP = "172.22.11.2" for Ethernet)

$ErrorActionPreference = "Stop"
$VMX_IP = if ($env:VMX_IP) { $env:VMX_IP } else { "10.12.34.2" }
$VMX_USER = "pi"
$JAR = Get-ChildItem -Path "build\libs" -Filter "*.jar" | Where-Object { $_.Name -notmatch "sources" } | Select-Object -First 1

if (-not $JAR) {
    Write-Host "No JAR in build\libs. Run: .\gradlew.bat build" -ForegroundColor Yellow
    exit 1
}

Write-Host "Deploying $($JAR.Name) to ${VMX_USER}@${VMX_IP} ..."
# Use sshpass if available, else user must type password when prompted.
$jarpath = $JAR.FullName
$remoteDir = "/home/pi/deploy"
$remoteJar = "$remoteDir/frc.jar"

# Create dir and copy (user will be prompted for password: raspberry)
ssh "${VMX_USER}@${VMX_IP}" "mkdir -p $remoteDir"
scp "$jarpath" "${VMX_USER}@${VMX_IP}:${remoteJar}"
if ($LASTEXITCODE -ne 0) { Write-Host "SCP failed. Is OpenSSH installed? Password is: raspberry" -ForegroundColor Red; exit 1 }
Write-Host "JAR copied. To run on VMX: ssh ${VMX_USER}@${VMX_IP} 'cd $remoteDir && java -jar frc.jar'" -ForegroundColor Green

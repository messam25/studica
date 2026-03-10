#!/usr/bin/env bash
# Deploy robot JAR to Studica VMX via SCP (user pi, password: raspberry).
# Prereqs: PC on VMX Wi-Fi (VMX-1234) or Ethernet; SSH in PATH.
# Usage: ./deploy-vmx.sh   or  VMX_IP=172.22.11.2 ./deploy-vmx.sh

set -e
VMX_IP="${VMX_IP:-10.12.34.2}"
VMX_USER="pi"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

JAR=$(find build/libs -maxdepth 1 -name '*.jar' ! -name '*-sources*.jar' | head -n1)
if [ -z "$JAR" ]; then
  echo "No JAR in build/libs. Run: ./gradlew build"
  exit 1
fi

REMOTE_DIR="/home/pi/deploy"
REMOTE_JAR="$REMOTE_DIR/frc.jar"

echo "Deploying $(basename "$JAR") to ${VMX_USER}@${VMX_IP} ..."
ssh "${VMX_USER}@${VMX_IP}" "mkdir -p $REMOTE_DIR"
scp "$JAR" "${VMX_USER}@${VMX_IP}:${REMOTE_JAR}"
echo "Done. On VMX run: ssh ${VMX_USER}@${VMX_IP} 'cd $REMOTE_DIR && java -jar frc.jar'"

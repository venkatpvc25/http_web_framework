#!/usr/bin/env bash
# run-dev.sh
# Start the app once with HotswapAgent; keep it running for hot-swap reloads.

set -euo pipefail

# ----------------- EDIT IF NEEDED -----------------
MAIN_CLASS="com.example.myapp.App"
HOTSWAP_AGENT_JAR="server/hotswap-agent.jar"   # <-- set this
USE_DCEVM=false                                       # set to true if you installed DCEVM
# -------------------------------------------------

# Build once
mvn -DskipTests package

# Prepare classpath with dependencies (one-time; overwritten each run)
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt > /dev/null

# Compose classpath: target/classes + runtime dependencies
CP="target/classes:$(cat cp.txt)"

echo "Classpath prepared. Starting JVM with HotswapAgent..."
echo "MAIN_CLASS=${MAIN_CLASS}"
echo "HOTSWAP_AGENT_JAR=${HOTSWAP_AGENT_JAR}"
echo "USE_DCEVM=${USE_DCEVM}"

if [ ! -f "${HOTSWAP_AGENT_JAR}" ]; then
  echo "ERROR: HotswapAgent jar not found at ${HOTSWAP_AGENT_JAR}"
  echo "Download from https://repo1.maven.org/maven2/org/hotswapagent/hotswap-agent/ then set HOTSWAP_AGENT_JAR."
  exit 2
fi

if [ "${USE_DCEVM}" = "true" ]; then
  echo "Running with DCEVM + HotswapAgent"
  java -XXaltjvm=dcevm -javaagent:"${HOTSWAP_AGENT_JAR}" -cp "${CP}" "${MAIN_CLASS}"
else
  echo "Running with standard HotSpot + HotswapAgent"
  java -javaagent:"${HOTSWAP_AGENT_JAR}" -cp "${CP}" "${MAIN_CLASS}"
fi

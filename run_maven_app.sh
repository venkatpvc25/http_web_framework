#!/bin/bash

# --- Check input ---
if [ -z "$1" ]; then
  echo "Usage: ./kill_port.sh <port>"
  exit 1
fi

PORT=$1

# --- Find process using the port ---
PID=$(lsof -ti:$PORT)

if [ -n "$PID" ]; then
  echo "🛑 Killing process $PID on port $PORT..."
  kill -9 $PID
  echo "✅ Port $PORT is now free."
else
  echo "✅ No process running on port $PORT."
fi

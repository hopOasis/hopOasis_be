#!/bin/bash

# Name of the JAR file
JAR_FILE="target/hop_oasis-0.0.1-SNAPSHOT.jar"

# Docker image for GraalVM
DOCKER_IMAGE="ghcr.io/graalvm/jdk-community:21"

# Output directory for the config files
OUTPUT_DIR="src/main/resources/META-INF/native-image"

# Run the Docker container with native-image-agent
docker run \
  -v "$(pwd)":/workspace \
  -w /workspace \
  $DOCKER_IMAGE \
  timeout 60s java -agentlib:native-image-agent=config-merge-dir=$OUTPUT_DIR -jar $JAR_FILE

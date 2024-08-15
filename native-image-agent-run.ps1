# Set the JAR file name
$jarFile = "target/hop_oasis-0.0.1-SNAPSHOT.jar"

# Set the Docker image for GraalVM
$dockerImage = "ghcr.io/graalvm/jdk-community:21"

# Set the output directory for the config files
$outputDir = "src/main/resources/META-INF/native-image"

# Path to the .env-local file
$envFile = ".env-local"

# Run the Docker container with native-image-agent
docker run --rm `
  --env-file $envFile `
  -v ${PWD}:/workspace `
  -w /workspace `
  $dockerImage `
  timeout 60s java -agentlib:native-image-agent=config-merge-dir=$outputDir -jar $jarFile

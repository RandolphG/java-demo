#!/usr/bin/env bash

# Maven build
function maven_build() {
    echo "Compiling the source..."

    mvn clean package

    printf "\n\n\n"
}

# Build the container for testing locally
function build_container() {
    maven_build

    echo "Building Docker image..."

    if [ -z "$DOCKER_IMAGE_TAG" ]; then
        DOCKER_IMAGE_TAG="test"
        echo "DOCKER_IMAGE_TAG environment variable is not set. Setting to default: $DOCKER_IMAGE_TAG"
    fi

    if [ -z "$DOCKER_IMAGE_REPO" ]; then
        DOCKER_IMAGE_REPO="cloudos"
        echo "DOCKER_IMAGE_REPO environment variable is not set. Setting to default: $DOCKER_IMAGE_REPO"
    fi

    APP_NAME="cosv3_hello_world_example"
    DOCKER_IMAGE_NAME=$DOCKER_IMAGE_REPO/$APP_NAME:$DOCKER_IMAGE_TAG

    docker build -t ${DOCKER_IMAGE_NAME} .
}

# run the container for testing
function run_container() {

    if [ -z "$DOCKER_IMAGE_TAG" ]; then
        DOCKER_IMAGE_TAG="test"
        echo "DOCKER_IMAGE_TAG environment variable is not set. Setting to default: $DOCKER_IMAGE_TAG"
    fi

    if [ -z "$DOCKER_IMAGE_REPO" ]; then
        DOCKER_IMAGE_REPO="cloudos"
        echo "DOCKER_IMAGE_REPO environment variable is not set. Setting to default: $DOCKER_IMAGE_REPO"
    fi

    if [ -z "$PORT" ]; then
        PORT="8080"
        echo "PORT environment variable is not set. Setting to default: $PORT"
    fi

    DOCKER_CONTAINER_NAME="milky-way"
    APP_NAME="cosv3_hello_world_example"
    DOCKER_IMAGE_NAME=$DOCKER_IMAGE_REPO/$APP_NAME:$DOCKER_IMAGE_TAG

    # Stop and remove any running Docker containers with the same name
    docker rm -f ${DOCKER_CONTAINER_NAME}

    docker run -d \
        --name ${DOCKER_CONTAINER_NAME} \
        --publish ${PORT}:${PORT} \
        --env PORT="${PORT}" \
        ${DOCKER_IMAGE_NAME}
}

if [ "$1" = "package" ]; then
    maven_build
elif [ "$1" = "build" ]; then
    build_container
elif [ "$1" = "run" ]; then
    run_container
else
    echo "Invalid choice [$1]... Please select one of 'package' 'build' 'run'"
fi

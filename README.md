# Dodo-Bank

This repository contains the code and the API documentation of a simple banking application.

## Requirements

* Docker
* JDK 8 or newer & Maven for development.

## API Documentation

The API documentation is located at `<DOMAIN>/swagger`.

E.g., when running the application locally, it is located at `http://localhost:8080/swagger`.

## Running the application locally

* Tests: `./mvnw test`.
* Build: `./mvnw install`.
* Start the service locally: `./mvnw exec:exec`.

## Building the Docker image

* Run `./mvnw clean install` to build the application. 
* Run `docker build -t dodo-bank .` to build a docker image.
* Run `docker run -p 8080:8080 dodo-bank` to run the docker image.

### Technologies

This application is based on Micronaut framework. The most notable ones are:

* [Micronaut](https://micronaut.io/)
* [Kotlin Test](https://github.com/kotlintest/kotlintest)
* [Spek](https://www.spekframework.org/)


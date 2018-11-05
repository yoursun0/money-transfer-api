# Money Transfer API #
Money Transfer API for creating transaction and retrieve balance

## Introduction ##

This README document specifies the main functional features of Money Transfer API.
It also describes what steps are necessary to get the application up and running.

## Functional features ##

In a nutshell, the Money Transfer API provide following features:

 1. Retrieve account balance

 2. Create transaction
* Perform transaction and return its result
* The transaction result should include the balance after transaction, by calling API #1 via HTTP in API #2

## Dependencies ##

This is a Springboot project built by maven. The following dependencies are required to run the application.

* JDK 8 standard library
* jUnit 4
* MongoDB
* Embedded MongoDB for unit testing
* Apache Logback for server logging

Refer to pom.xml for details of maven dependencies

## Execute the program ##

Run maven clean install to deploy all the dependencies of the project.
Then compile and run Main.java as a simple Java console application.

## Unit Testing ##
This project uses jUnit 4 for unit testing
test coverage: 100%

## Project author ##

* Helic Leung
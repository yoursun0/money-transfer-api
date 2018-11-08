# Money Transfer API #
Money Transfer API for creating transaction and retrieve balance

## Introduction ##

This README document specifies the main functional features of Money Transfer API.
It also describes what installation steps are necessary to get the application up and running.

## Functional features ##

In a nutshell, the Money Transfer API provide following features:

 1. Retrieve account balance

 2. Create transaction for money transfer
* Perform transaction and return the result 
* The transaction result should include the balance after transaction, by calling API #1 via HTTP in API #2

Basic error handling is supported, such as avoiding transfer of money from an account with not enough credit, etc.

## Dependencies ##

This is a Springboot project built by maven. The following third party library is used in the application.

* JDK 8 standard library
* jUnit 4
* JPA for MySQL database
* Embedded HSQL for unit testing
* Apache common lang3
* Apache Logback for server logging

Maven wrapper is included in this project.

Refer to pom.xml for details of maven dependencies.

## Installation ##

 1. You need to have a MySQL database server running, and allow port 3306 for JDBC connection by the application.

 2. Connect to your MySQL database server through a MySQL client, execute the data batching script in `/src/main/resources/db/ddl.sql` for preparation of tables and sample data

 3. Checkout the project repository
 
 4. Run maven clean install to deploy all the dependencies of the project.
 
 5. Compile and run MoneyTransferApplication.class as a springboot application
 
 6. You can test the following APIs locally by using POSTMAN (https://www.getpostman.com/)
 
 * http://localhost:8080/v1/money/account/521877048123
 
 * http://localhost:8080/v1/money/transaction

## API specification ##
Refer to https://app.swaggerhub.com/apis/helicleung/money-transfer-api/1.0

## Unit Testing ##
This project uses jUnit 4 for unit testing

You can run the unit test by the following command:

`java -cp <junit-jar>;<hamcrest-jar>;. org.junit.runner.JUnitCore  TestClass1 TestClass2`

Or you can run all the test cases by maven:

`mvn test`

## Project author ##

* Helic Leung
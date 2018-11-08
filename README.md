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

Maven wrapper is included in this project to facilitate maven build process.

Refer to pom.xml for details of maven dependencies.

## Installation ##

 1. You need to have a MySQL database server running, and allow port 3306 for JDBC connection by the application.

 2. Connect to your MySQL database server through a MySQL client, execute the data batching script in `/src/main/resources/db/ddl.sql` for preparation of tables and sample data

 3. Checkout the project repository
 
 4. Run `mvn package` to package the project using maven

 5. Launch spring boot web application by this command: `java -jar target/money-transfer-0.0.1-SNAPSHOT.jar`

 6. You can test the following APIs locally by using curl or POSTMAN (https://www.getpostman.com/)
 
 * GET  http://localhost:8080/v1/money/account/521877048123
 
 * POST http://localhost:8080/v1/money/transaction

Please refer to API specification below for the request parameter details

## API specification ##
Refer to https://app.swaggerhub.com/apis/helicleung/money-transfer-api/1.0

## Sample data ##
Sample data is provided at MySQL script `/src/main/resources/db/ddl.sql`
The initial account records are shown as below:

| id            | name            | balance         | currency  | created_ts          | updated_ts          |
| ------------- | --------------- | --------------- | --------- | ------------------- | ------------------- |
| 477333222431  | WONG SHEUNG     | 99999.0000      | HKD       | 2018-01-02 11:11:11 |	2018-01-02 11:11:11 |
| 521877048123  | CHAN TAI MAN    | 1000.0000       | HKD       | 2018-01-01 11:11:11 |	2018-01-01 11:11:11 |
| 987654321000  | CHEUNG SIU MING | 7777.0000       | HKD       | 2018-07-01 11:11:11 |	2018-07-01 11:11:11 |

## Unit Testing ##
This project uses jUnit 4 for unit testing

You can run the unit test by the following command:

`java -cp <junit-jar>;<hamcrest-jar>;. org.junit.runner.JUnitCore  TestClass1 TestClass2`

Or you can run all the test cases by maven:

`mvn test`

## Project author ##

* Helic Leung
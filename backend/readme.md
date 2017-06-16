### Development Environment set up ###

Prerequisites - Mysql (5.7.11+) and Java 8. Gradle included in repo, no need to install separately. 

* Create development and test databases and user:
  
```
create database expenses;
grant all privileges on expenses.* to 'dev'@'localhost' identified by '123';
create database expenses_test;
grant all privileges on expenses_test.* to 'dev'@'localhost' identified by '123';

```
* Clone to empty directory
* Run 
```
./gradlew flywayMigrate generateMainJooqSchemaSource eclipse shadowJar
```
This command should populate database with initial schema, generate Java DB classes from the schema, build solution (including uber jar), and generate eclipse project and settings (could be skipped if no plans to develop in eclipse; adding idea support should be simple). All tests should pass.
* If using eclipse - import project generated and run tests as JUnit 4 test cases - all should pass. In order to debug from Eclipse run com.engagetech.expenses.Application as a java application.
* In order to run binaries execute 
```
java -jar build/libs/expenses-all.jar
```
Application will connect development database "expenses" configured above with dev/123 credentials and will listen port 4567. Frontend configuration in root repository folder is changed to access that default backend url (http://localhost:4567). 
* The expenses-all.jar file is the only distribution of the backend. Copy it to server host and run with parameters like below to connect to production database and listen different port: 
```
java -DdbUrl=jdbc:mysql://somerds.cxplmsbpogqp.eu-west-2.rds.amazonaws.com:3306/expenses -DdbUser=someuser -DdbPassword=somepassword -DhttpPort=9090 -jar expenses-all.jar
```
* Full rebuild command below. Use with care, flywayClean drops database tables.
```
gradle clean flywayClean flywayMigrate generateMainJooqSchemaSource build cleanEclipse eclipse shadowJar
```


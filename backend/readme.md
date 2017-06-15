### Development Environment set up ###

* Prerequisites
- Mysql (5.7.11+)
- Java 8
- Gradle included in repo, no need to install separately

* Create development and test databases and user:
  
```
#!sql

create database expenses;
grant all privileges on expenses.* to 'dev'@'localhost' identified by '123';
create database expenses_test;
grant all privileges on expenses_test.* to 'dev'@'localhost' identified by '123';

```
* Clone to empty directory
* Run "./gradlew flywayMigrate generateMainJooqSchemaSource eclipse shadowJar" - this command should populate database with initial schema, generate Java DB classes from the schema, build solution (including uber jar), and generate eclipse project and settings (could be skipped if no plans to develop in eclipse; adding idea support should be simple). All tests should pass.
* If using eclipse - import project generated and run tests as JUnit 4 test cases - all should pass. In order to debug from Eclipse run com.engagetech.expenses.Application as a java application.
* In order to run binaries execute "java -jar build/libs/expenses-all.jar". The expenses-all.jar file is the only distribution. Copy it to server host 
Full rebuild - "gradle clean flywayClean flywayMigrate generateMainJooqSchemaSource build cleanEclipse eclipse shadowJar" - use with care, flywayClean drops database tables.
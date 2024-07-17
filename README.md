
# Projet Openclassroom: Testez-une-application-full-stack

 

Generated with Java 8 and Angular CLI version 14.1.0.




## Installation

Clone the repository

```bash
    git clone https://github.com/aymen2255/P5-Full-Stack-testing.git
```

#### Install Backend

```bash
    cd back
    mvn clean install
```

#### MySQL

SQL script for creating the schema is available ressources/sql/script.sql

By default the admin account is:

    login: yoga@studio.com
    password: test!1234


#### Install Frontend

```bash
    cd front
    npm install
```

## Running Tests

#### Backend

Launch unit test with:
```bash
    mvn clean test
```
Generate the code coverage with:
```bash
    mvn jacoco:report
```
You can see coverage report inside
```bash
    back/target/site/jacoco/index.html
``` 

#### Frontend
Launch unit test with:
```bash
    npm run test
```
Generate the code coverage with:
```bash
    npm run test --coverage
```
You can see coverage report inside
```bash
    front/coverage/jest/lcov-report/index.html
``` 

Launch end to end test with:
```bash
    npm run e2e
```
Generate the code coverage with:
```bash
    npm run e2e:coverage
```
You can see coverage report inside
```bash
    front/coverage/lcov-report/index.html
``` 
#Homework Pauls Žunda

## Installation & Running

* To run tests directly from source (cmd): `gradlew tests`

* To run application directly from source (cmd): `gradlew  bootRun`

* To create and run .jar container:

1. run (cmd): `gradlew clean build`
2. locate .jar: ./build/libs/homework-0.0.1-SNAPSHOT.jar
3. run (cmd): `java -jar homework-0.0.1-SNAPSHOT.jar`
  
## Usage

1. BaseUrl is http://127.0.0.1:8080
2. To create a loan POST amount and days(term) to /rest/loans/apply ;Example: `{"amount": 300, "days": 20}`
3. To extend a term for the loan POST date to /rest/{loan_id}/extend ; Example: `{"date": "2016-06-01"}`
4. To get all user loans GET url /rest/loans


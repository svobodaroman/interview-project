# Getting Started

### Setup project

* There is need for Postgres DB, you can set it by docker-compose up -d
* For compile you can use IntelliJ IDEA or mvn package
* For starting app you can use IntelliJ IDEA or  java -jar .\target\interview-0.0.1-SNAPSHOT.jar

### How it works

* You can send requests to app using Postman or http://localhost:8080/swagger-ui/index.html
* It supports create of new match, update of existing match score, and getting Scores of a match.
* Also you can subscribe to simple websocket notifications using http://localhost:8080/index.html (it subcribes automatically) and watch notifications about score changes in browsers console.




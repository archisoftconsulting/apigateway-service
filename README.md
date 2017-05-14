[<img src="https://img.shields.io/travis/playframework/play-java-starter-example.svg"/>](https://travis-ci.org/playframework/play-java-starter-example)

# Apigateway Microservice Starter

This is a starter application for apigateway. The purpose of the API gateway is to handle external traffic.
Services should communicate directly without a gateway/ESB-style mediator.

## Running

Run this using [sbt](http://www.scala-sbt.org/):

```
sbt run
```

And then go to 

1. http://localhost:9000 to see the running web application.
2. http://localhost:9000/api/userservice to see the response
3. http://localhost:9000/api/greetingservice to see the response
4. Edit the endpoints.conf and goto /api/userservice and /api/greetingservice to see the response.


## Controllers

There are several demonstration files available in this template.

- HomeController.java:

  Shows how to handle simple HTTP requests.

- ApigatewayController.java

  Receive user request and do the following:
  
 		/* TODO:
		 * 1. get user requested location,methods
		 * 2. Check if user requested URL location and methods exist in endpoint.conf file(permission control)
		 * C,R,U,D = POST,GET,PUT/PATCH,DELETE
		 * 3. oAuth authentication check
		 * 4. blacklist check
		 * 5. rate limiting check
		 * 6. process the request according to location and methods.
		 * 7. Circuit breaker check
		 * 8. Dispatch the request
		 */
		 
## Dockerize the application
```
sbt docker:publishLocal
docker run -d -p 9000:9000 apigateway-service:1.0-SNAPSHOT
docker start apigateway-service:1.0-SNAPSHOT
browse http://localhost:9000 and http://localhost:9000/api
```


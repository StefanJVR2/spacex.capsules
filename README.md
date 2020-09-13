# Space X Capsule API
This API acts as a proxy for https://api.spacexdata.com/v3/capsules/, it makes of an in-memory database to speed up results.
There are two ways to add a capsule to the in-memory store, either by performing a GET on `/capsules/{capsule_serial}` or a POST, the main 
difference being that the GET will store and return the data, while the POST will only store the data.

## Prerequisites
* Docker has to be installed locally, docker desktop install can be found at https://www.docker.com/products/docker-desktop
* JAVA_HOME Environment variable has to be set

## Getting Started
To start the application run the following commands in command prompt
* Clone repository: `git clone https://github.com/StefanJVR2/spacex.capsules.git`
* Compile and build docker image: `mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=capgemini/spacex.capsules`
* Run app `docker run -p 8080:8080 -t capgemini/spacex.capsules`

## Tests
To run tests run `mvnw clean compile test`

## Postman collection
A postman collection with all available calls can be found [here](./postman/SpaceX%20Capsule%20API%20Test%20Collection.postman_collection.json)


## OpenAPI
![image info](./assets/openapi.png)
The OpenAPI Specification (OAS) defines a standard, language-agnostic interface to RESTful APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection. When properly defined, a consumer can understand and interact with the remote service with a minimal amount of implementation logic.
An OpenAPI definition can then be used by documentation generation tools to display the API, code generation tools to generate servers and clients in various programming languages, testing tools, and many other use cases.
(https://swagger.io/specification/)


**In this project the API is defined [here](./contracts/service-spacex-capsules.yaml) and an API Interface is generated using [this](https://github.com/OpenAPITools/openapi-generator) maven plugin.**


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/htmlsingle/#production-ready)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.3.3.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [OpenAPI](https://swagger.io/docs/specification/about/)
* [OpenAPI Maven Plugin](https://openapi-generator.tech/docs/plugins/)
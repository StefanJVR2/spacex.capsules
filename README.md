# Space X Capsule API
This API acts as a proxy for https://api.spacexdata.com/v3/capsules/, it makes of an in-memory database to speed up results.
There are two ways to add a capsule to the in-memory store, either by performing a GET on `/capsules/{capsule_serial}` or a POST, the main 
difference being that the GET will store and return the data, while the POST will only store the data.

## Getting Started


 

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
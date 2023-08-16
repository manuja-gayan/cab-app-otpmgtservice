# otpmgtservice
Microservice for handling OTP generation and validation.

## Features
* OTP generation with standard algorithm
* Validate user submitted OTP
* Send OTP as SMS to user
* Highly scalable.

## Supported versions

system will support the following versions.  
Other versions might also work, but we have not tested it.

* Java 8, 11
* Spring Boot 2.7.5

## Building and running

To build and test, you can run:

```sh
$ cd cab-app-otpmgtservice
# build the application
$ mvn clean install
# run the application
$ java -jar target/otpmgtservice-0.0.1.jar
```

## Contributing

Bug reports and pull requests are welcome :)
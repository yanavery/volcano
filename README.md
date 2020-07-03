# Volcano
Volcano Sample SpringBoot/SpringData Application.

## Purpose
The purpose of this application is to demonstrate a simple and minimalist `Booking` application using a simple REST API. The application is built using Java 14, SpringBoot, SpringData and an H2 in-memory database.

The application allows for listing of booking availabilities as well as creating/updating/deleting of reservations.

## Code Location
Most of the code can be found at the following location:

* `./src/main/java/*`

## Dependencies
This demo requires the following to be installed on the computer runnning the application:

* Java 14
* Maven

## General Disclaimer
The general idea behind the application implementation was to keep things simple. This way, more
focus and emphasis is made on the actual problem at hand, which is the implementation of a REST API using the Java programming language.

Notable examples where there could be improvements:

* Data exchanges are done over a non secure `http` connection instead of `https`.
* There is no application login nor any authorization rules around API accesses.
* There is no internationalization.
* Uses an in-memory database (H2) and has no caching enabled.
* Unit test coverage is light and could greatly be improved.
* And the list goes on ... :-)

## Data
Everytime the application is stopped and re-started the data added thru the application API is lost as it's not persisted to disk. Upon re-starting the application, the in-memory database is seeded with few sample invoice records, so the application is demo-able out of the gate.

## Build
The application is built and executed using standard Maven/SpringBoot commands and targets.

Build and launch the application with the following command, from the main folder:

`mvn spring-boot:run`

## API Quick Access

Here are some sample cURL fragments that can be used to access the API:

* Finding availabilities:
```
    curl http://localhost:8080/v1/availabilities
```
```
    curl http://localhost:8080/v1/availabilities?startDate=2020-07-10&endDate=2020-07-20
```
* Creating a new reservation:
```
    curl -X POST -H 'Content-Type: application/json' -d \
    '{
        "customer": {
            "email": "yan_avery@yopmail.com",
            "name": "Yan Avery"
        },
        "checkIn": "2020-07-12",
        "checkOut": "2020-07-14"
    }' \
    http://localhost:8080/v1/reservations
```
* Updating an existing reservation:
```
    curl -X PUT -H 'Content-Type: application/json' -d \
    '{
        "checkIn": "2020-07-14",
        "checkOut": "2020-07-16"
    }' \
    http://localhost:8080/v1/reservations/{uuid}
```
* Deleting an existing reservation:
```
    curl -X DELETE http://localhost:8080/v1/reservations/{uuid}
```

## Testing
Although coverage is not thorough, some unit and integration tests were put together and can be found at the following location:

* `./src/test/java/*`

## Database Console
If needed, the H2 database console becomes accessible at:

`http://localhost:8080/h2-console/`

* db url: jdbc:h2:mem:bookings
* username: volcano
* password: <none>

## Closing Words

That's it ... That should be everything that's needed to make this "simple" application work. If you have any questions, comments or feedback, please feel free to contact me at yanavery (at) gmail (dot) com.

Thank you!
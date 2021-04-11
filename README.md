# Notes Manager API

A REST API which enables CRUD operations to be performed on notes consisting of a title and 
description.

## Getting Started

### Prerequisites

 - JDK 11 or newer
 - Docker _(optional)_

### Project Organisation

Notes Manager is separated into to 2 projects:

- `notes-manager-web` - The core API consisting of the controller, models, services and unit tests.
- `notes-manager-acceptance-tests` - The integration test suite that is executed against the core API.


### Building the application

To build the project and run all test, execute the following command:

```bash
./gradlew clean build
```

### Running the application

To run the project, execute the following command:

```bash
./gradlew clean bootRun
```

### Testing the application

To solely run the tests, execute the following command:

```bash
./gradlew clean test
```

## Usage

When executed locally, Notes Manager will be available at: http://localhost:8080

Documentation is available at: https://turnerdaniel.github.io/notes-manager/

### Executable Jar

This project can be executed from a jar file that is created by the build process by running the 
following command:

```bash
java -jar ./notes-manager-web/build/libs/notes-manager-1.0.1.jar
```

### Docker

The application can be started quickly by executing:

```bash
docker-compose up
```

This will build the application and pull the MySQL image used to store the note data. These 
services will be started in separate containers and simulate a production-like environment where
data is persisted after restarts. The necessary volumes, ports and networks are all managed for you. 

Stop the application by running: 

```bash
docker-compose stop
```

The containers, networks and mounted volumes (which persist the DB data) can be removed by running:

```bash
docker-compose down -v
```

### Configuration

The application provides some configuration options which are provided below:

|         Parameter       |  Environment Variable  |         Values        | Description |
| ----------------------- | :--------------------: | --------------------- | ----------- |
| Dspring.profiles.active | SPRING_PROFILES_ACTIVE | `local`, `production` | Set the profile used by the application. _Defaults to `local`_ |
| Dnotes.database.host    | NOTES_DATABASE_HOST    | `string`              | Set the url and port used to connect to an external database host. Only used in the production profile. _Defaults to [localhost:3306](http://localhost:3306)._
| Dnotes.database.name    | NOTES_DATABASE_NAME    | `string`              | Set the name of the database to connect to. Only used in the production profile. _Defaults to `notes`_  

The parameters must be included before the jar file:

```bash
java -Dspring.profiles.active=production -jar ./notes-manager-web/build/libs/notes-manager-1.0.1.jar
```

The environment variables need to be set in bash or powershell respectively before the application
is executed:

```bash
export NOTES_DATABASE_HOST=mysql.server.com:3306
```

```powershell
$env:NOTES_DATABASE_NAME = 'note_db'
```

## Licence

Notes Manager is released under the [MIT license](LICENSE.md).

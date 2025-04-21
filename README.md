# spring-boot-grpc-sample
## Stack
- protobuf
- spring boot starter of `grpc-ecosystem/grpc-spring`
- h2 database

## Run
### Server
- generate code from proto file: `./gradlew :server:generateProto`
- start application: `./gradlew :server:bootRun`
  - server application port: `8080`
  - grpc endpoint port: `9090`

### Client
- generate code from proto file: `./gradlew :client:generateProto`
- start application: `./gradlew :client:bootRun`
  - client application port: `8081`

### Request to gRPC server via client application
- list people: `curl http://localhost:8081/api/people`
- create people: `curl -X POST http://localhost:8081/api/people -H "Content-Type: application/json" -d '{"name": "<name>", "age": <age>}'`

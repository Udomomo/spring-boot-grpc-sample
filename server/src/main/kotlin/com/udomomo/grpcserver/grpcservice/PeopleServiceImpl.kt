package com.udomomo.grpcserver.grpcservice

@GrpcService
class PeopleServiceImpl: PeopleServiceGrpcKt.PeopleServiceCoroutineImplBase() {
    override suspend fun getPerson(request: PersonRequest): PersonResponse {
        return PersonResponse.newBuilder()
            .setName("John Doe")
            .setAge(30)
            .build()
    }

    override suspend fun listPeople(request: ListPeopleRequest): ListPeopleResponse {
        val people = listOf(
            Person.newBuilder().setName("Alice").setAge(25).build(),
            Person.newBuilder().setName("Bob").setAge(28).build()
        )
        return ListPeopleResponse.newBuilder().addAllPeople(people).build()
    }
}
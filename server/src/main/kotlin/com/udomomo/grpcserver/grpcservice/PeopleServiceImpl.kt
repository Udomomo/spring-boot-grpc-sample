package com.udomomo.grpcserver.grpcservice

import com.google.protobuf.Empty
import com.udomomo.grpcserver.repository.PeopleRepository
import net.devh.boot.grpc.server.service.GrpcService
import people.PeopleOuterClass
import people.PeopleServiceGrpcKt

@GrpcService
class PeopleServiceImpl(
    private val peopleRepository: PeopleRepository
): PeopleServiceGrpcKt.PeopleServiceCoroutineImplBase() {
    override suspend fun createPeople(
        request: PeopleOuterClass.CreatePeopleRequest
    ): PeopleOuterClass.CreatePeopleResponse {
        peopleRepository.save(request.name, request.age)

        return PeopleOuterClass.CreatePeopleResponse.newBuilder()
            .setEmpty(Empty.getDefaultInstance())
            .build()
    }

    override suspend fun listPeople(
        request: PeopleOuterClass.ListPeopleRequest
    ): PeopleOuterClass.ListPeopleResponse {
        val peoples = peopleRepository.list().map {
            PeopleOuterClass.People.newBuilder()
                .setId(it.id)
                .setName(it.name)
                .setAge(it.age)
                .build()
        }

        return PeopleOuterClass.ListPeopleResponse.newBuilder()
            .addAllPeople(peoples)
            .build()
    }
}

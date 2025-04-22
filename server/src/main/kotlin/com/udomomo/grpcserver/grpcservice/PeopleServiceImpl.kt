package com.udomomo.grpcserver.grpcservice

import com.google.protobuf.Empty
import com.udomomo.grpcserver.repository.PeopleRepository
import net.devh.boot.grpc.server.service.GrpcService
import org.slf4j.LoggerFactory
import people.PeopleOuterClass
import people.PeopleServiceGrpcKt

@GrpcService
class PeopleServiceImpl(
    private val peopleRepository: PeopleRepository
): PeopleServiceGrpcKt.PeopleServiceCoroutineImplBase() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun createPeople(
        request: PeopleOuterClass.CreatePeopleRequest
    ): PeopleOuterClass.CreatePeopleResponse {
        logger.info("Receive createPeople request | name=${request.name}, age=${request.age}")

        peopleRepository.save(request.name, request.age)

        return PeopleOuterClass.CreatePeopleResponse.newBuilder()
            .setEmpty(Empty.getDefaultInstance())
            .build()
    }

    override suspend fun listPeople(
        request: PeopleOuterClass.ListPeopleRequest
    ): PeopleOuterClass.ListPeopleResponse {
        logger.info("Receive listPeople request")

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

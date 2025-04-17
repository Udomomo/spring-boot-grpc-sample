package com.udomomo.grpcclient.client

import com.udomomo.grpcclient.model.People
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service
import people.PeopleOuterClass
import people.PeopleOuterClass.ListPeopleRequest
import people.PeopleServiceGrpc

@Service
class PeopleServiceGrpcClient {
    @GrpcClient("peopleService")
    private lateinit var peopleServiceBlockingStub: PeopleServiceGrpc.PeopleServiceBlockingStub

    fun listPeople(): List<People> {
        return peopleServiceBlockingStub.listPeople(
            ListPeopleRequest.newBuilder().build()
        ).peopleList.map {
            People(
                id = it.id,
                name = it.name,
                age = it.age
            )
        }
    }

    fun createPeople(name: String, age: Int) {
        val request = PeopleOuterClass.CreatePeopleRequest.newBuilder()
            .setName(name)
            .setAge(age)
            .build()

        kotlin.runCatching {
            peopleServiceBlockingStub.createPeople(request)
        }
    }
}

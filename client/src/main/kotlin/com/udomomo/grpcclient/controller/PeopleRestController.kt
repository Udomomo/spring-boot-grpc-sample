package com.udomomo.grpcclient.controller

import com.udomomo.grpcclient.client.PeopleServiceGrpcClient
import com.udomomo.grpcclient.model.People
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/people")
class PeopleRestController(
    private val peopleServiceGrpcClient: PeopleServiceGrpcClient
) {
    @GetMapping("")
    fun list(): List<People> {
        return peopleServiceGrpcClient.listPeople()
    }

    @PostMapping("")
    fun create(@RequestBody request: PeopleRequest) {
        peopleServiceGrpcClient.createPeople(request.name, request.age)
    }
}

data class PeopleRequest(
    val name: String,
    val age: Int
)

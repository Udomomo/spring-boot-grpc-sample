package com.udomomo.grpcserver.repository

import org.springframework.stereotype.Repository

@Repository
class PeopleRepository {
    fun list(): List<PeopleVO> {
        return PeopleEntity.all()
            .map { it.toVO() }
            .toList()
    }

    fun save(name: String, age: Int) {
        PeopleEntity.new {
            this.name = name
            this.age = age
        }
        return
    }
}

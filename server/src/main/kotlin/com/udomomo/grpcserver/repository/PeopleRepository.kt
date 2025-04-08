package com.udomomo.grpcserver.repository

import org.springframework.stereotype.Repository

@Repository
class PeopleRepository {
    fun list(): List<PeopleVO> {
        return PeopleEntity.all()
            .map { it.toVO() }
            .toList()
    }

    fun save(people: PeopleVO): PeopleVO {
        val entity = PeopleEntity.new {
            name = people.name
            age = people.age
        }
        return entity.toVO()
    }
}

package com.udomomo.grpcserver.repository

import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class PeopleRepository {
    fun list(): List<PeopleVO> {
        return PeopleTable.selectAll().map {
            PeopleVO(
                it[PeopleTable.id].value,
                it[PeopleTable.peopleName],
                it[PeopleTable.age],
            )
        }
    }

    fun save(name: String, age: Int) {
        PeopleEntity.new {
            this.peopleName = name
            this.age = age
        }
        return
    }
}

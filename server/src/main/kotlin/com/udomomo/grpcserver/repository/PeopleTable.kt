package com.udomomo.grpcserver.repository

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object PeopleTable : IntIdTable("test") {
    val name = varchar("name", 255)
    val age = integer("age")
}

class PeopleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PeopleEntity>(PeopleTable)

    var name by PeopleTable.name
    var age by PeopleTable.age
}

data class PeopleVO(
    val id: Int,
    val name: String,
    val age: Int
)

fun PeopleEntity.toVO(): PeopleVO {
    return PeopleVO(
        id = this.id.value,
        name = this.name,
        age = this.age
    )
}
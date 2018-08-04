package co.zsmb.staticsite.data

import org.springframework.data.mongodb.repository.MongoRepository

interface CustomPageRepository : MongoRepository<CustomPage, String> {

    fun findByName(name: String): CustomPage?

    fun deleteByName(name: String): Int

}

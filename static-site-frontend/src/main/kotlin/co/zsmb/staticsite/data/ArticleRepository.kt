package co.zsmb.staticsite.data

import org.springframework.data.mongodb.repository.MongoRepository

interface ArticleRepository : MongoRepository<Article, String> {

    fun findByUrl(id: String): Article?

    fun findAllByOrderByPublishDateDesc(): List<Article>

}

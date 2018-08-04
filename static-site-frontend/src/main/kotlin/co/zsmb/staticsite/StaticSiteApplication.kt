package co.zsmb.staticsite

import co.zsmb.staticsite.data.ArticleRepository
import co.zsmb.staticsite.data.CustomPageRepository
import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.SecurityCollection
import org.apache.tomcat.util.descriptor.web.SecurityConstraint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletRequest


@SpringBootApplication
class StaticSiteApplication

fun main(args: Array<String>) {
    runApplication<StaticSiteApplication>(*args)
}

@Configuration
class SecurityConfiguration {

    @Bean
    fun servletContainer(): ServletWebServerFactory {
        val tomcat = object : TomcatServletWebServerFactory() {
            override fun postProcessContext(context: Context) {
                val securityConstraint = SecurityConstraint()
                securityConstraint.userConstraint = "CONFIDENTIAL"
                val collection = SecurityCollection()
                collection.addPattern("/*")
                securityConstraint.addCollection(collection)
                context.addConstraint(securityConstraint)
            }
        }
        tomcat.addAdditionalTomcatConnectors(redirectConnector())
        return tomcat
    }

    private fun redirectConnector(): Connector {
        val connector = Connector("org.apache.coyote.http11.Http11NioProtocol")
        connector.scheme = "http"
        connector.port = 80
        connector.secure = false
        connector.redirectPort = 443
        return connector
    }

}

@Controller
class IndexController(
        private val customPageRepository: CustomPageRepository,
        private val articleRepository: ArticleRepository
) {

    @GetMapping("/")
    fun index(request: HttpServletRequest, model: Model): String {
        model.addAttribute("metadata", Metadata())
        model.addAttribute("articles", articleRepository.findAllByOrderByPublishDateDesc())
        return "blog"
    }

    @GetMapping("/*")
    fun dynamic(request: HttpServletRequest, model: Model): String {
        val path = request.servletPath.trim('/')

        val metadata = Metadata()
        model.addAttribute("metadata", metadata)

        return getCustomPage(model, path, metadata)
                ?: getArticle(model, path, metadata)
                ?: "error"
    }

    private fun getArticle(model: Model, path: String, metadata: Metadata): String? {
        val article = articleRepository.findByUrl(path)
        if (article != null) {
            metadata.title = article.title
            model.addAttribute("title", article.title)
            model.addAttribute("content", Markdown.render(article.content))
            return "article"
        }
        return null
    }

    private fun getCustomPage(model: Model, path: String, metadata: Metadata): String? {
        val customPage = customPageRepository.findByName(path)
        if (customPage != null) {
            metadata.title = customPage.name.toLowerCase().capitalize()
            model.addAttribute("content", Markdown.render(customPage.content))
            return "customPage"
        }
        return null
    }

}

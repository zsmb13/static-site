package co.zsmb.staticsite

import co.zsmb.staticsite.data.ArticleRepository
import co.zsmb.staticsite.data.CustomPageRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletRequest


@SpringBootApplication
class StaticSiteApplication

fun main(args: Array<String>) {
    runApplication<StaticSiteApplication>(*args)
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

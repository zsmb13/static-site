package co.zsmb.staticsite.controllers

import co.zsmb.staticsite.data.ArticleRepository
import co.zsmb.staticsite.data.CustomPageRepository
import co.zsmb.staticsite.util.formatForDisplay
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.io.File
import java.net.URLConnection
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Controller
class SiteController(
        private val customPageRepository: CustomPageRepository,
        private val articleRepository: ArticleRepository
) {

    @GetMapping("/")
    fun index(request: HttpServletRequest, model: Model): String {
        model.addAttribute("metadata", Metadata())

        val currentTimeMs = System.currentTimeMillis()
        val articles = articleRepository.findAllByOrderByPublishDateDesc()
                .filter { it.publishDate.time <= currentTimeMs }
        model.addAttribute("articles", articles)

        return "blog"
    }

    @GetMapping("/files/{fileName}")
    fun getFile(@PathVariable("fileName") fileName: String, response: HttpServletResponse) {
        val file = File("/files", fileName)

        if (!file.exists()) {
            response.sendError(404, "File files/$fileName not found")
            return
        }

        val contentType = URLConnection.guessContentTypeFromName(file.name)
        response.contentType = contentType
        response.outputStream.use { output ->
            file.inputStream().use { input ->
                input.copyTo(output)
            }
        }
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
        val article = articleRepository.findByUrl(path) ?: return null

        metadata.title = article.title
        metadata.description = article.summary

        model.addAttribute("title", article.title)
        model.addAttribute("content", Markdown.render(article.content))
        model.addAttribute("date", article.publishDate.formatForDisplay())

        return "article"
    }

    private fun getCustomPage(model: Model, path: String, metadata: Metadata): String? {
        val customPage = customPageRepository.findByName(path) ?: return null

        metadata.title = customPage.name.toLowerCase().capitalize()

        model.addAttribute("content", Markdown.render(customPage.content))

        return "customPage"
    }

}

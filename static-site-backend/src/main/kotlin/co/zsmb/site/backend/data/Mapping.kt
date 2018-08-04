package co.zsmb.site.backend.data

import co.zsmb.site.backend.Markdown

private fun String.renderMarkdown() = Markdown.render(this)

fun Article.toSummary() = ArticleSummary(
        title = title,
        url = url,
        summary = summary.renderMarkdown(),
        publishDate = publishDate.time,
        id = id!!
)

fun Article.toDetail() = ArticleDetail(
        title = title,
        content = content.renderMarkdown(),
        publishDate = publishDate.time,
        lastModificationDate = lastModificationDate?.time,
        id = id!!
)

fun CustomPage.render() = OldCommonCustomPage(
        name = name,
        content = content.renderMarkdown()
)

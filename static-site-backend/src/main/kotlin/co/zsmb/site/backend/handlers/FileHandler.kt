package co.zsmb.site.backend.handlers

import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.Part
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.io.File

@Component
@PreAuthorize("hasRole('ADMIN')")
class FileHandler {

    fun upload(request: ServerRequest): Mono<ServerResponse> {
        // Not used in this example, but useful for logging, etc
//        val id = request.pathVariable("id")

        println("x")

        return request.body(BodyExtractors.toMultipartData()).flatMap { parts ->
            val map: Map<String, Part> = parts.toSingleValueMap()
            val filePart: FilePart = map["file"]!! as FilePart
            // Note cast to "FilePart" above

            // Save file to disk
            val fileName = filePart.filename()
            val file = File("myFiles/$fileName")
            filePart.transferTo(file)

            ServerResponse.ok().syncBody("file upload ok")
        }
    }

}

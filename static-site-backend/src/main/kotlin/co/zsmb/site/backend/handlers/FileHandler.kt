package co.zsmb.site.backend.handlers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.io.File
import java.util.stream.Collectors

@Component
@PreAuthorize("hasRole('ADMIN')")
class FileHandler {

    companion object {
        private val ROOT_FOLDER = File("/files")
    }

    fun upload(request: ServerRequest): Mono<ServerResponse> {
        val outputFile = File(ROOT_FOLDER, request.pathVariable("fileName"))

        val out = outputFile.outputStream()

        println("Opened output stream")

        return request.body(BodyExtractors.toDataBuffers())
                .doOnEach { it ->
                    val buffer = it.get() ?: return@doOnEach
                    buffer.asInputStream().use { input ->
                        val copied = input.copyTo(out, bufferSize = 4 * DEFAULT_BUFFER_SIZE)
                        println("Writing $copied bytes")
                    }
                }
                .collect(Collectors.counting())
                .doOnTerminate {
                    out.close()
                    println("Closing out stream")
                }
                .flatMap { ServerResponse.ok().syncBody("file upload ok") }
    }

}

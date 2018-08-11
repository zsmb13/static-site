package co.zsmb.staticsite

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class StaticSiteApplication

fun main(args: Array<String>) {
    runApplication<StaticSiteApplication>(*args)
}

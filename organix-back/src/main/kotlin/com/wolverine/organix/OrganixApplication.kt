package com.wolverine.organix

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class OrganixApplication

fun main(args: Array<String>) {
    runApplication<OrganixApplication>(*args)
}

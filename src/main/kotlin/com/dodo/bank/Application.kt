package com.dodo.bank

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License

@OpenAPIDefinition(
        info = Info(
                title = "Dodo Bank",
                version = "1.0",
                description = "Dodo Bank Transfer API",
                license = License(name = "Apache 2.0"),
                contact = Contact(name = "Doruk Coskun", email = "dcoskun@protonmail.com")
        )
)
object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.dodo.bank")
                .mainClass(Application.javaClass)
                .start()
    }
}
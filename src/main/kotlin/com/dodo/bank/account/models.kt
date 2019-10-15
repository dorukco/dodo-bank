package com.dodo.bank.account

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.swagger.v3.oas.annotations.media.Schema
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@Schema(name = "AccountRequest", description = "Account request")
data class AccountCreateRequest(
        @field:NotBlank
        val name: String?,
        @field:NotNull
        val amount: BigDecimal?
)

@Schema(name = "AccountResponse", description = "Account response")
data class AccountResponse(
        @field:NotNull
        val id: Long,
        @field:NotBlank
        val name: String,
        @field:NotNull
        @field:JsonSerialize(using = MoneySerializer::class)
        val amount: BigDecimal
)

class MoneySerializer : JsonSerializer<BigDecimal>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: BigDecimal, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeNumber(value.setScale(2, RoundingMode.HALF_UP))
    }
}
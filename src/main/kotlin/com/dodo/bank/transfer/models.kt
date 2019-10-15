package com.dodo.bank.transfer

import com.dodo.bank.account.MoneySerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.validation.constraints.NotNull

@Schema(name = "TransferRequest", description = "Transfer request")
data class TransferCreateRequest(
        @field:NotNull
        val fromAccount: Long?,
        @field:NotNull
        val toAccount: Long?,
        @field:NotNull
        val amount: BigDecimal?
)

@Schema(name = "TransferResponse", description = "Transfer response")
data class TransferResponse(
        @field:NotNull
        val id: Long,
        @field:NotNull
        val fromAccount: Long,
        @field:NotNull
        val toAccount: Long,
        @field:NotNull
        @field:JsonSerialize(using = MoneySerializer::class)
        val amount: BigDecimal,
        @field:NotNull
        val transferDate: OffsetDateTime
)
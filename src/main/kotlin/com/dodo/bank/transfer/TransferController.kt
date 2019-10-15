package com.dodo.bank.transfer

import com.dodo.bank.exception.ApiError
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import javax.validation.Valid

@Validated
@Controller("/transfer")
open class TransferController(private val transferRepository: TransferRepository) {

    @Get("/{id}", produces = [MediaType.APPLICATION_JSON])
    @Operation(summary = "Returns a transfer by given id")
    @ApiResponses(
            ApiResponse(responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = TransferResponse::class))]),
            ApiResponse(responseCode = "404", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = ApiError::class))]),
            ApiResponse(responseCode = "500", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = ApiError::class))])
    )
    open fun getTransfer(id: Long): TransferResponse? = transferRepository.findById(id)?.let {
        TransferResponse(it.id, it.fromAccount, it.toAccount, it.amount, it.transferDate)
    }

    @Get("/", produces = [MediaType.TEXT_JSON])
    @Operation(summary = "Returns all transfers")
    @ApiResponses(
            ApiResponse(responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = TransferResponse::class))]),
            ApiResponse(responseCode = "500", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = ApiError::class))])
    )
    open fun getTransfers(): List<TransferResponse>? = transferRepository.findAll().map {
        TransferResponse(it.id, it.fromAccount, it.toAccount, it.amount, it.transferDate)
    }

    @Post("/", produces = [MediaType.TEXT_JSON])
    @Operation(summary = "Execute a transfer")
    @ApiResponses(
            ApiResponse(responseCode = "201", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = TransferResponse::class))]),
            ApiResponse(responseCode = "400", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = ApiError::class))]),
            ApiResponse(responseCode = "500", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = ApiError::class))])
    )
    open fun executeTransfer(@Body @Valid request: TransferCreateRequest): HttpResponse<TransferResponse> {
        val transfer = transferRepository.createTransfer(request.fromAccount!!, request.toAccount!!,
                request.amount!!).let { TransferResponse(it.id, it.fromAccount, it.toAccount, it.amount, it.transferDate) }

        return HttpResponse.created(transfer)
    }

}
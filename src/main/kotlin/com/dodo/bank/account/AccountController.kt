package com.dodo.bank.account

import com.dodo.bank.exception.ApiError
import io.micronaut.http.HttpResponse
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
@Controller("/account")
open class AccountController(private val accountRepository: AccountRepository) {

    @Get("/{id}")
    @Operation(summary = "Returns an account by given id")
    @ApiResponses(
            ApiResponse(responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = AccountResponse::class))]),
            ApiResponse(responseCode = "404", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = ApiError::class))]),
            ApiResponse(responseCode = "500", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = ApiError::class))])
    )
    open fun getAccount(id: Long): AccountResponse? = accountRepository.findById(id)?.let {
        AccountResponse(it.id, it.name, it.amount)
    }

    @Get("/")
    @Operation(summary = "Returns all accounts")
    @ApiResponses(
            ApiResponse(responseCode = "200", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = AccountResponse::class))]),
            ApiResponse(responseCode = "500", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = ApiError::class))])
    )
    open fun getAccounts(): List<AccountResponse>? = accountRepository.findAll().map {
        AccountResponse(it.id, it.name, it.amount)
    }

    @Post("/")
    @Operation(summary = "Create an account")
    @ApiResponses(
            ApiResponse(responseCode = "201", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = AccountResponse::class))]),
            ApiResponse(responseCode = "500", content = [Content(mediaType = "application/json", schema = Schema(
                    implementation = ApiError::class))])
    )
    open fun saveAccount(@Body @Valid request: AccountCreateRequest): HttpResponse<AccountResponse> {
        val account = accountRepository.save(request.name!!, request.amount!!).let {
            AccountResponse(it.id, it.name, it.amount)
        }

        return HttpResponse.created(account)
    }

}
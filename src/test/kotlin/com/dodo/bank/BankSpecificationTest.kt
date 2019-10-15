package com.dodo.bank

import com.dodo.bank.account.AccountCreateRequest
import com.dodo.bank.account.AccountResponse
import com.dodo.bank.transfer.TransferCreateRequest
import com.dodo.bank.transfer.TransferResponse
import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class BankSpecificationTest : Spek({
    describe("Setting up the server") {
        val embeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
        val client = HttpClient.create(embeddedServer.url)

        describe("Account operations") {

            val firstAccount = AccountCreateRequest("test account 1", BigDecimal("150.50"))
            val secondAccount = AccountCreateRequest("test account 2", BigDecimal("250.50"))

            it("Try to create an account") {
                val request: HttpRequest<Any> = HttpRequest.POST("/account", firstAccount)
                val response: HttpResponse<Any> = client.toBlocking().exchange(request)
                assertEquals(HttpStatus.CREATED, response.status)
            }

            it("Try to get a known account") {
                val request: HttpRequest<Any> = HttpRequest.GET("/account/1")
                val response: HttpResponse<AccountResponse> = client.toBlocking().exchange(request,
                        AccountResponse::class.java)
                assertEquals(HttpStatus.OK, response.status)
                assertEquals(AccountResponse(1, firstAccount.name!!, firstAccount.amount!!), response.body())
            }

            it("Try to get an unknown account and fail") {
                val request: HttpRequest<Any> = HttpRequest.GET("/account/2")
                val exception = assertFailsWith(HttpClientResponseException::class) {
                    client.toBlocking().exchange(request, AccountResponse::class.java)
                }
                assertEquals(HttpStatus.NOT_FOUND, exception.response.status)
            }

            it("Try to create another account") {
                val request: HttpRequest<Any> = HttpRequest.POST("/account", secondAccount)
                val response: HttpResponse<Any> = client.toBlocking().exchange(request)
                assertEquals(HttpStatus.CREATED, response.status)
            }

            it("Try to get accounts") {
                val request: HttpRequest<Any> = HttpRequest.GET("/account")
                val response: HttpResponse<List<*>> = client.toBlocking().exchange(request, Argument.of
                (List::class.java, AccountResponse::class.java))
                assertEquals(HttpStatus.OK, response.status)
                assertEquals(2, response.body()?.size)
            }
        }

        describe("Transfer operations") {

            val transfer = TransferCreateRequest(1, 2, BigDecimal("50.50"))

            it("Try to execute a transfer") {
                val request: HttpRequest<Any> = HttpRequest.POST("/transfer", transfer)
                val response: HttpResponse<TransferResponse> = client.toBlocking().exchange(request,
                        TransferResponse::class.java)
                assertEquals(HttpStatus.CREATED, response.status)
                assertEquals(1, response.body()?.fromAccount)
                assertEquals(2, response.body()?.toAccount)
                assertEquals(BigDecimal("50.50"), response.body()?.amount)
                assertNotNull(response.body()?.transferDate)
            }

            it("Try to get a transfer") {
                val request: HttpRequest<Any> = HttpRequest.GET("/transfer/1")
                val response: HttpResponse<TransferResponse> = client.toBlocking().exchange(request,
                        TransferResponse::class.java)
                assertEquals(HttpStatus.OK, response.status)
                assertEquals(1, response.body()?.fromAccount)
                assertEquals(2, response.body()?.toAccount)
                assertEquals(BigDecimal("50.50"), response.body()?.amount)
                assertNotNull(response.body()?.transferDate)
            }

            it("Try to execute another transfer") {
                val request: HttpRequest<Any> = HttpRequest.POST("/transfer", transfer)
                val response: HttpResponse<TransferResponse> = client.toBlocking().exchange(request,
                        TransferResponse::class.java)
                assertEquals(HttpStatus.CREATED, response.status)
                assertEquals(1, response.body()?.fromAccount)
                assertEquals(2, response.body()?.toAccount)
                assertEquals(BigDecimal("50.50"), response.body()?.amount)
                assertNotNull(response.body()?.transferDate)
            }

            it("Try to execute another transfer and fail") {
                val request: HttpRequest<Any> = HttpRequest.POST("/transfer", transfer)
                val exception = assertFailsWith(HttpClientResponseException::class) {
                    client.toBlocking().exchange(request, TransferResponse::class.java)
                }
                assertEquals(HttpStatus.BAD_REQUEST, exception.response.status)
            }

            it("Try to get transfers") {
                val request: HttpRequest<Any> = HttpRequest.GET("/transfer")
                val response: HttpResponse<List<*>> = client.toBlocking().exchange(request, Argument.of
                (List::class.java, TransferResponse::class.java))
                assertEquals(HttpStatus.OK, response.status)
                assertEquals(2, response.body()?.size)
            }
        }

        describe("Final Account Checking") {
            it("Check the balance of the sender account") {
                val request: HttpRequest<Any> = HttpRequest.GET("/account/1")
                val response: HttpResponse<AccountResponse> = client.toBlocking().exchange(request,
                        AccountResponse::class.java)
                assertEquals(HttpStatus.OK, response.status)
                assertEquals(BigDecimal("49.50"), response.body()?.amount)
            }

            it("Check the balance of the receiver account") {
                val request: HttpRequest<Any> = HttpRequest.GET("/account/2")
                val response: HttpResponse<AccountResponse> = client.toBlocking().exchange(request,
                        AccountResponse::class.java)
                assertEquals(HttpStatus.OK, response.status)
                assertEquals(BigDecimal("351.50"), response.body()?.amount)
            }
        }

        afterGroup {
            client.close()
            embeddedServer.close()
        }
    }

})
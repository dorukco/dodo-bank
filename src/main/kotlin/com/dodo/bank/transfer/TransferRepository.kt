package com.dodo.bank.transfer

import com.dodo.bank.account.AccountRepository
import com.dodo.bank.exception.ApiError
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.spring.tx.annotation.Transactional
import java.math.BigDecimal
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Singleton
open class TransferRepository(@param:CurrentSession @field:PersistenceContext private val entityManager: EntityManager,
                              private val accountRepository: AccountRepository) {

    @Transactional(readOnly = true)
    open fun findAll(): List<Transfer> {
        return entityManager.createQuery("SELECT t FROM Transfer as t", Transfer::class.java).resultList
    }

    @Transactional(readOnly = true)
    open fun findById(id: Long): Transfer? {
        return entityManager.find(Transfer::class.java, id) ?: throw ApiError.ofMessage(
                "Transfer id $id cannot be found.").asNotFound()
    }

    @Transactional
    open fun createTransfer(from: Long, to: Long, amount: BigDecimal): Transfer {
        accountRepository.deposit(from, amount.negate()).also {
            if (it == 0) throw ApiError.ofMessage("Account " +
                    "id $from cannot be used within this transfer.").asBadRequest()
        }
        accountRepository.deposit(to, amount).also {
            if (it == 0) throw ApiError.ofMessage("Account " +
                    "id $to cannot be used within this transfer.").asBadRequest()
        }
        val transfer = Transfer(from, to, amount)
        entityManager.persist(transfer)
        return transfer
    }
}
package com.dodo.bank.account

import com.dodo.bank.exception.ApiError
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.spring.tx.annotation.Transactional
import java.math.BigDecimal
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Singleton
open class AccountRepository(@param:CurrentSession @field:PersistenceContext private val entityManager: EntityManager) {
    @Transactional(readOnly = true)
    open fun findById(id: Long): Account? {
        return entityManager.find(Account::class.java, id) ?: throw ApiError.ofMessage(
                "Account id $id cannot be found.").asNotFound()
    }

    @Transactional(readOnly = true)
    open fun findAll(): List<Account> {
        return entityManager.createQuery("SELECT g FROM Account AS g", Account::class.java).resultList
    }

    @Transactional
    open fun save(name: String, amount: BigDecimal): Account {
        val account = Account(name, amount)
        entityManager.persist(account)
        return account
    }

    @Transactional
    open fun deposit(id: Long, amount: BigDecimal): Int {
        return entityManager.createQuery(
                "UPDATE Account SET amount = amount + :amount WHERE id = :id AND 0 <= (amount + :amount)")
                .setParameter("amount", amount)
                .setParameter("id", id)
                .executeUpdate()
    }
}
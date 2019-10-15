package com.dodo.bank.account

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "account")
data class Account(
        @Id
        @GeneratedValue(generator = "account_sequence", strategy = GenerationType.SEQUENCE)
        @SequenceGenerator(name = "account_sequence", allocationSize=1)
        val id: Long,

        @Column(name = "name", nullable = false)
        val name: String,

        @Column(name = "amount", nullable= false, precision=10, scale=2)
        val amount: BigDecimal
) {
    constructor(name: String, amount: BigDecimal) : this(0, name, amount)
}
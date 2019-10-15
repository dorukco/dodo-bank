package com.dodo.bank.transfer

import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@Table(name = "transfer")
data class Transfer(
        @Id
        @GeneratedValue(generator = "transfer_sequence", strategy = GenerationType.SEQUENCE)
        @SequenceGenerator(name = "transfer_sequence", allocationSize = 1)
        val id: Long,

        @Column(name = "from_account", nullable = false)
        val fromAccount: Long,

        @Column(name = "to_account", nullable = false)
        val toAccount: Long,

        @Column(name = "amount", nullable = false, precision = 10, scale = 2)
        val amount: BigDecimal,

        @Column(name = "transfer_datetime", columnDefinition = "TIMESTAMP WITH TIME ZONE")
        val transferDate: OffsetDateTime
) {
    constructor(from: Long, to: Long, amount: BigDecimal) : this(0, from, to, amount, OffsetDateTime.now())
}
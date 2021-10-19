package io.laurabrehm.dkbtechassignment.ports

import io.laurabrehm.dkbtechassignment.model.Account
import io.laurabrehm.dkbtechassignment.model.Transaction

interface AccountRepository {

    fun findAccount(iban: String): Account?
    fun save(account: Account): String
    fun save(transaction: Transaction)
    fun getHistory(iban: String): List<Transaction>
}
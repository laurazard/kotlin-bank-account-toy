package io.laurabrehm.dkbtechassignment.adapters

import io.laurabrehm.dkbtechassignment.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class InMemoryAccountRepositoryTest {

    @Test
    fun `saves a new account and assigns it an iban`() {
        val inMemoryAccountRepository = InMemoryAccountRepository()
        val accountToSave = Account(
            type = AccountType.CHECKING,
            balance = 134
        )

        inMemoryAccountRepository.save(accountToSave)

        assertEquals(1, inMemoryAccountRepository.accounts.size)
        assertEquals(34, inMemoryAccountRepository.accounts.first().iban.length)
    }

    @Test
    fun `finds existing account`() {
        val inMemoryAccountRepository = InMemoryAccountRepository()
        val accountToSave = Account(
            type = AccountType.CHECKING,
            balance = 134
        )
        val iban = inMemoryAccountRepository.save(accountToSave)

        val retrievedAccount = inMemoryAccountRepository.findAccount(iban)

        assertEquals(accountToSave.copy(iban = iban), retrievedAccount)
    }

    @Test
    fun `updates account when saving if it already exists`() {
        val inMemoryAccountRepository = InMemoryAccountRepository()
        val accountToSave = Account(
            type = AccountType.CHECKING,
            balance = 134
        )
        val iban = inMemoryAccountRepository.save(accountToSave)
        inMemoryAccountRepository.save(accountToSave.copy(iban = iban, balance = 200))

        val retrievedAccount = inMemoryAccountRepository.findAccount(iban)

        assertEquals(200, retrievedAccount!!.balance)
        assertEquals(1, inMemoryAccountRepository.accounts.size)
    }

    @Test
    fun `saves transaction successfully`() {
        val inMemoryAccountRepository = InMemoryAccountRepository()
        val sendingAccountIban = inMemoryAccountRepository.save(Account(
            type = AccountType.CHECKING,
            balance = 542
        ))
        val receivingAccountIban = inMemoryAccountRepository.save(Account(
            type = AccountType.CHECKING,
            balance = 200
        ))
        val transactionToSave = Transaction(
            sendingAccountIban = sendingAccountIban,
            receivingAccountIban = receivingAccountIban,
            amount = 100
        )

        inMemoryAccountRepository.save(transactionToSave)

        assertEquals(1, inMemoryAccountRepository.transactions.size)
        assertEquals(transactionToSave, inMemoryAccountRepository.transactions.first())
    }

    @Test
    fun `fails to save transaction when one account does not exist`() {
        val inMemoryAccountRepository = InMemoryAccountRepository()
        val sendingAccountIban = inMemoryAccountRepository.save(Account(
            type = AccountType.CHECKING,
            balance = 1000
        ))

        assertThrows<AccountNotFoundException> {
            inMemoryAccountRepository.save(Transaction(
                sendingAccountIban = sendingAccountIban,
                receivingAccountIban = "an iban that does not exist",
                amount = 500
            ))
        }
    }

    @Test
    fun `gets all transactions pertaining to one account`() {
        val inMemoryAccountRepository = InMemoryAccountRepository()
        val accountIban1 = inMemoryAccountRepository.save(Account(
            type = AccountType.CHECKING,
            balance = 1000
        ))
        val accountIban2 = inMemoryAccountRepository.save(Account(
            type = AccountType.CHECKING,
            balance = 1000
        ))
        val accountIban3 = inMemoryAccountRepository.save(Account(
            type = AccountType.CHECKING,
            balance = 1000
        ))
        val relatedTransactions = listOf(
            Transaction(
                sendingAccountIban = accountIban1,
                receivingAccountIban = accountIban2,
                amount = 300
            ),
            Transaction(
                sendingAccountIban = accountIban2,
                receivingAccountIban = accountIban1,
                amount = 200
            ),
            Transaction(
                sendingAccountIban = accountIban1,
                receivingAccountIban = accountIban3,
                amount = 38
            )
        )

        relatedTransactions.forEach {
            inMemoryAccountRepository.save(it)
        }
        inMemoryAccountRepository.save(
            Transaction(
                sendingAccountIban = accountIban2,
                receivingAccountIban = accountIban3,
                amount = 50
            )
        )

        assertEquals(relatedTransactions, inMemoryAccountRepository.getHistory(accountIban1))
    }

}
package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.AccountNotFoundException
import io.laurabrehm.dkbtechassignment.model.Transaction
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class GetHistoryTest {

    @Test
    fun `calls repository with correct iban and gets history`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        val historyTransactions = listOf(Transaction(
            sendingAccountIban = "a-valid-iban",
            receivingAccountIban = "another-valid-iban",
            amount = 30
        ))
        every { mockRepository.getHistory("a-valid-iban") } returns historyTransactions
        val getHistory = GetHistory(mockRepository)

        val history = getHistory("a-valid-iban")

        assertEquals(historyTransactions, history)
    }

    @Test
    fun `throws AccountNotFoundException when account does not exist`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        every { mockRepository.findAccount("an-iban-that-does-not-exist") } returns null
        val getHistory = GetHistory(mockRepository)

        assertThrows<AccountNotFoundException> {
            getHistory("an-iban-that-does-not-exist")
        }
    }
}
package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.Account
import io.laurabrehm.dkbtechassignment.model.AccountNotFoundException
import io.laurabrehm.dkbtechassignment.model.AccountType
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class GetBalanceTest {

    @Test
    fun `calls repository and returns balance`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        every { mockRepository.findAccount("an iban") } returns Account(
            iban = "an iban",
            type = AccountType.LOAN,
            balance = 1642
        )
        val getBalance = GetBalance(mockRepository)

        val balance = getBalance("an iban")

        assertEquals(1642, balance)
    }

    @Test
    fun `throws AccountNotFoundException when account does not exist`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        every { mockRepository.findAccount("an iban") } returns null
        val getBalance = GetBalance(mockRepository)

        val exception = assertThrows<AccountNotFoundException> {
            getBalance("an iban")
        }

        assertEquals("could not find an account with iban: an iban", exception.message)
    }
}
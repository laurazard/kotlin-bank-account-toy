package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.Account
import io.laurabrehm.dkbtechassignment.model.AccountNotFoundException
import io.laurabrehm.dkbtechassignment.model.AccountType
import io.laurabrehm.dkbtechassignment.model.FrozenAccountException
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class DepositTest {

    @Test
    fun `updates account with new balance`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        every { mockRepository.findAccount("an iban") } returns Account(
            iban = "an iban",
            type = AccountType.CHECKING,
            balance = 543
        )
        val deposit = Deposit(mockRepository)

        deposit("an iban", 200)

        verify { mockRepository.save(
            Account(
                iban = "an iban",
                type = AccountType.CHECKING,
                balance = 743
            ))
        }
    }

    @Test
    fun `throws AccountNotFoundException when no account exists with provided IBAN`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        every { mockRepository.findAccount(any()) } returns null
        val deposit = Deposit(mockRepository)

        val exception = assertThrows<AccountNotFoundException> {
            deposit("an iban", 100)
        }

        assertEquals("could not find account with provided IBAN", exception.message)
    }

    @Test
    fun `throws FrozenAccountException when account is locked`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        every { mockRepository.findAccount("an-iban") } returns Account(
            iban = "an-iban",
            type = AccountType.CHECKING,
            balance = 3000,
            locked = true
        )
        val deposit = Deposit(mockRepository)

        assertThrows<FrozenAccountException> {
            deposit("an-iban", 100)
        }
    }

}
package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.*
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class TransferTest {

    @Test
    fun `creates and correctly saves transfer`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        every { mockRepository.findAccount("an-iban") } returns Account(
            iban = "an-iban",
            type = AccountType.CHECKING,
            balance = 1000
        )
        every { mockRepository.findAccount("a-different-iban") } returns Account(
            iban = "a-different-iban",
            type = AccountType.CHECKING,
            balance = 200
        )
        val transfer = Transfer(mockRepository)

        transfer(
            sendingAccountIban = "an-iban",
            receivingAccountIban = "a-different-iban",
            amount = 100
        )

        verify {
            mockRepository.save(Account(
                iban = "an-iban",
                type = AccountType.CHECKING,
                balance = 900
            ))
        }
        verify {
            mockRepository.save(Account(
                iban = "a-different-iban",
                type = AccountType.CHECKING,
                balance = 300
            ))
        }
        verify { mockRepository.save(Transaction(
            sendingAccountIban = "an-iban",
            receivingAccountIban = "a-different-iban",
            amount = 100
        )) }
    }

    @Test
    fun `throws AccountNotFoundException when there is no account with that iban`() {
        val mockRepository = mockk<AccountRepository>(relaxed = true)
        every { mockRepository.findAccount("an-iban") } returns null
        val transfer = Transfer(mockRepository)

        assertThrows<AccountNotFoundException> {
            transfer("an-iban", "another-iban", 200)
        }
    }

    @Test
    fun `throws FrozenAccountException when performing a transfer to or from a locked account`() {
        val mockRepository = mockk<AccountRepository>(relaxed = true)
        every { mockRepository.findAccount("an-iban") } returns Account(
            iban = "an-iban",
            type = AccountType.CHECKING,
            balance = 400,
            locked = true
        )
        every { mockRepository.findAccount("another-iban") } returns Account(
            iban = "another-iban",
            type = AccountType.CHECKING,
            balance = 1500,
            locked = false
        )
        val transfer = Transfer(mockRepository)

        assertThrows<FrozenAccountException> {
            transfer("an-iban", "another-iban", 300)
        }
    }

    @Test
    fun `throws InvalidTransferException when performing a transfer from a savings account to an unrelated checking account`() {
        val mockRepository = mockk<AccountRepository>(relaxed = true)
        every { mockRepository.findAccount("checking-iban") } returns Account(
            iban = "checking-iban",
            type = AccountType.CHECKING,
            balance = 400,
            locked = false
        )
        every { mockRepository.findAccount("savings-iban") } returns Account(
            iban = "savings-iban",
            type = AccountType.SAVINGS,
            balance = 1500,
            locked = false,
            checkingAccount = "checking-iban"
        )
        every { mockRepository.findAccount("other-iban") } returns Account(
            iban = "other-iban",
            type = AccountType.CHECKING,
            balance = 800,
            locked = false
        )
        val transfer = Transfer(mockRepository)

        assertThrows<InvalidTransferException> {
            transfer("savings-iban", "other-iban", 1000)
        }
    }

    @Test
    fun `throws InvalidTransferException when transferring money out of a private loan account`() {
        val mockRepository = mockk<AccountRepository>(relaxed = true)
        every { mockRepository.findAccount("loan-iban") } returns Account(
            iban = "loan-iban",
            type = AccountType.LOAN,
            balance = 1000000,
            locked = false
        )
        every { mockRepository.findAccount("checking-iban") } returns Account(
            iban = "checking-iban",
            type = AccountType.CHECKING,
            balance = 1500,
            locked = false,
        )
        val transfer = Transfer(mockRepository)

        assertThrows<InvalidTransferException> {
            transfer("loan-iban", "checking-iban", 1000)
        }
    }
}

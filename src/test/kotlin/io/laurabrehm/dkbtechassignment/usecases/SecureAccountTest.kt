package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.Account
import io.laurabrehm.dkbtechassignment.model.AccountNotFoundException
import io.laurabrehm.dkbtechassignment.model.AccountType
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SecureAccountTest {

    @Test
    fun `updates repository with locked account`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        val account = Account(
            iban = "an-iban",
            type = AccountType.CHECKING,
            balance = 4100
        )
        every { mockRepository.findAccount("an-iban") } returns account
        every { mockRepository.save(any<Account>()) } returns "an-iban"
        val secureAccount = SecureAccount(mockRepository)

        secureAccount("an-iban", true)

        verify { mockRepository.save(account.copy(locked = true)) }
    }

    @Test
    fun `updates repository with unlocked account`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        val account = Account(
            iban = "an-iban",
            type = AccountType.CHECKING,
            balance = 4100
        )
        every { mockRepository.findAccount("an-iban") } returns account
        every { mockRepository.save(any<Account>()) } returns "an-iban"
        val secureAccount = SecureAccount(mockRepository)

        secureAccount("an-iban", false)

        verify { mockRepository.save(account.copy(locked = false)) }
    }

    @Test
    fun `throws AccountNotFoundException when account does not exist`() {
        val mockRepository: AccountRepository = mockk(relaxed = true)
        every { mockRepository.findAccount("an-iban") } returns null
        val secureAccount = SecureAccount(mockRepository)

        assertThrows<AccountNotFoundException> {
            secureAccount("an-iban", true)
        }

        verify(exactly = 0) { mockRepository.save(any<Account>())}
    }
}
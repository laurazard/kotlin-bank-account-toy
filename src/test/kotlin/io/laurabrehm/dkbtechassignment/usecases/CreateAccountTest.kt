package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.Account
import io.laurabrehm.dkbtechassignment.model.AccountType
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CreateAccountTest {

    @Test
    fun `calls repository and creates new account`() {
        val mockAccountRepository: AccountRepository = mockk(relaxed = true)
        every { mockAccountRepository.save(any<Account>()) } returns "a".repeat(34)
        val createAccount = CreateAccount(repository = mockAccountRepository)

        val iban = createAccount(AccountType.CHECKING)

        assertEquals("a".repeat(34), iban)
        verify { mockAccountRepository.save(Account(type = AccountType.CHECKING, balance = 0)) }
    }
}
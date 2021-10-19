package io.laurabrehm.dkbtechassignment.handlers

import io.laurabrehm.dkbtechassignment.model.AccountType
import io.laurabrehm.dkbtechassignment.usecases.CreateAccount
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateAccountHandler(private val createAccount: CreateAccount) {

    @PostMapping("/account")
    fun createAccount(): CreateAccountResponse =
        CreateAccountResponse(
            iban = createAccount(AccountType.CHECKING)
        )
}

data class CreateAccountResponse(
    val iban: String
)
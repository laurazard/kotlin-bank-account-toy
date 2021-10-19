package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.Account
import io.laurabrehm.dkbtechassignment.model.AccountType
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import org.springframework.stereotype.Component

@Component
class CreateAccount(private val repository: AccountRepository) {

    operator fun invoke(accountType: AccountType): String {
        val newAccount = Account(type = accountType, balance = 0)
        return repository.save(newAccount)
    }
}

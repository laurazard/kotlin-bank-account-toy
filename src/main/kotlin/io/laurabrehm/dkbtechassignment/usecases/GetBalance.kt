package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.AccountNotFoundException
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import org.springframework.stereotype.Component

@Component
class GetBalance(private val repository: AccountRepository) {

    operator fun invoke(iban: String): Int {
        val account = repository.findAccount(iban)
            ?: throw AccountNotFoundException("could not find an account with iban: $iban")

        return account.balance
    }
}
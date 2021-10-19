package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.AccountNotFoundException
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import org.springframework.stereotype.Component

@Component
class SecureAccount(private val repository: AccountRepository) {

    operator fun invoke(iban: String, locked: Boolean) {
        val account = repository.findAccount(iban)
            ?: throw AccountNotFoundException("")

        repository.save(account.copy(locked = locked))
    }
}
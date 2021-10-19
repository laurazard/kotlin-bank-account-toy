package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.AccountNotFoundException
import io.laurabrehm.dkbtechassignment.model.Transaction
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import org.springframework.stereotype.Component

@Component
class GetHistory(private val repository: AccountRepository) {

    operator fun invoke(iban: String): List<Transaction> {
        repository.findAccount(iban)
            ?: throw AccountNotFoundException("")

        return repository.getHistory(iban)
    }
}
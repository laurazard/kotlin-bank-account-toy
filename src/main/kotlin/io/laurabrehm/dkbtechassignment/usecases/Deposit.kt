package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.AccountNotFoundException
import io.laurabrehm.dkbtechassignment.model.FrozenAccountException
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import org.springframework.stereotype.Component

@Component
class Deposit(private val repository: AccountRepository) {

    operator fun invoke(iban: String, amount: Int) {
        val accountToDeposit = repository.findAccount(iban)
            ?: throw AccountNotFoundException("could not find account with provided IBAN")

        require(!accountToDeposit.locked) {
            throw FrozenAccountException()
        }

        repository.save(accountToDeposit.copy(balance = accountToDeposit.balance + amount))
    }
}

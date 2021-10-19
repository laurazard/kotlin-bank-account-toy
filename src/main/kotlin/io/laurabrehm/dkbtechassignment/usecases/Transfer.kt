package io.laurabrehm.dkbtechassignment.usecases

import io.laurabrehm.dkbtechassignment.model.*
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import org.springframework.stereotype.Component

@Component
class Transfer(private val repository: AccountRepository) {

    operator fun invoke(
        sendingAccountIban: String,
        receivingAccountIban: String,
        amount: Int
    ) {
        val sendingAccount = repository.findAccount(sendingAccountIban) ?:
            throw AccountNotFoundException("could not find account with iban: $sendingAccountIban")
        val receivingAccount = repository.findAccount(receivingAccountIban) ?:
            throw AccountNotFoundException("could not find account with iban: $receivingAccountIban")

        require(!sendingAccount.locked && !receivingAccount.locked) {
            throw FrozenAccountException()
        }

        require(!sendingAccount.isSavingsAccount() || sendingAccount.checkingAccount == receivingAccountIban) {
            throw InvalidTransferException("transferring money from a savings account to an unrelated account is not valid")
        }

        require(!sendingAccount.isPrivateLoanAccount()) {
            throw InvalidTransferException("transferring money from a private loan account is not valid")
        }

        repository.save(sendingAccount.copy(balance = sendingAccount.balance - amount))
        repository.save(receivingAccount.copy(balance = receivingAccount.balance + amount))
        repository.save(Transaction(sendingAccountIban, receivingAccountIban, amount))
    }
}

class InvalidTransferException(message: String): Exception(message)

fun Account.isSavingsAccount() = this.type == AccountType.SAVINGS
fun Account.isPrivateLoanAccount() = this.type == AccountType.LOAN
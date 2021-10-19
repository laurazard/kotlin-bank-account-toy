package io.laurabrehm.dkbtechassignment.adapters

import io.laurabrehm.dkbtechassignment.model.Account
import io.laurabrehm.dkbtechassignment.model.AccountNotFoundException
import io.laurabrehm.dkbtechassignment.model.InvalidTransactionException
import io.laurabrehm.dkbtechassignment.model.Transaction
import io.laurabrehm.dkbtechassignment.ports.AccountRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class InMemoryAccountRepository: AccountRepository {

    val accounts = mutableListOf<Account>()
    val transactions = mutableListOf<Transaction>()

    override fun findAccount(iban: String): Account? =
        accounts.find { account -> account.iban == iban }

    override fun save(account: Account): String {
        return if(account.iban.isNotEmpty()) {
            accounts.mapInPlace { old -> if (old.iban == account.iban) account else old}
            account.iban
        } else {
            val newAccount = account.copy(iban = generateUniqueIBAN())
            accounts.add(newAccount)
            newAccount.iban
        }
    }

    override fun save(transaction: Transaction) {
        findAccount(transaction.sendingAccountIban)
            ?: throw AccountNotFoundException("could not find an account with iban: ${transaction.sendingAccountIban}")
        findAccount(transaction.receivingAccountIban)
            ?: throw AccountNotFoundException("could not find an account with iban: ${transaction.receivingAccountIban}")

        transactions.add(transaction)
    }

    override fun getHistory(iban: String): List<Transaction> =
        transactions.filter { transaction -> transaction.receivingAccountIban == iban || transaction.sendingAccountIban == iban }


    private tailrec fun generateUniqueIBAN(): String {
        val newIban = UUID.randomUUID().toString().substring(0, 34)

        return if(findAccount(newIban) == null){
            newIban
        } else {
            generateUniqueIBAN()
        }
    }
}

inline fun <T> MutableList<T>.mapInPlace(mutator: (T)->T) {
    val iterate = this.listIterator()
    while (iterate.hasNext()) {
        val oldValue = iterate.next()
        val newValue = mutator(oldValue)
        if (newValue !== oldValue) {
            iterate.set(newValue)
        }
    }
}
package io.laurabrehm.dkbtechassignment.model

data class Account(
    val iban: String = "",
    val type: AccountType,
    val balance: Int,
    val locked: Boolean = false,
    val checkingAccount: String? = null
) {
    init {
        val valid = (type == AccountType.SAVINGS && checkingAccount != null)
                || (type != AccountType.SAVINGS && checkingAccount == null)
        require(valid) {
            throw InvalidAccountException("Savings account must be tied to a checking account")
        }
    }
}

enum class AccountType {
    CHECKING, SAVINGS, LOAN
}

class AccountNotFoundException(message: String) : Exception(message)
class FrozenAccountException : Exception()
class InvalidAccountException(message: String) : Exception(message)

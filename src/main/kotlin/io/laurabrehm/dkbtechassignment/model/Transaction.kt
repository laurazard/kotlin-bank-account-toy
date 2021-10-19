package io.laurabrehm.dkbtechassignment.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val sendingAccountIban: String,
    val receivingAccountIban: String,
    val amount: Int
)

class InvalidTransactionException(): Exception()
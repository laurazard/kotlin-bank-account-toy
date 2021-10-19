package io.laurabrehm.dkbtechassignment.handlers

import io.laurabrehm.dkbtechassignment.usecases.Deposit
import kotlinx.serialization.Serializable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DepositHandler(private val deposit: Deposit) {

    @PostMapping("/deposit")
    fun makeDeposit(@RequestBody request: DepositRequest) = deposit(
        iban = request.iban,
        amount = request.amount
    )
}

@Serializable
data class DepositRequest(
    val iban: String,
    val amount: Int
)
package io.laurabrehm.dkbtechassignment.handlers

import io.laurabrehm.dkbtechassignment.usecases.GetBalance
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetBalanceHandler(
    private val getBalance: GetBalance
) {

    @GetMapping("/account/{iban}/balance")
    fun getAccountBalance(@PathVariable iban: String) =
        BalanceResponse(
            balance = getBalance(iban)
        )
}


data class BalanceResponse(
    val balance: Int
)
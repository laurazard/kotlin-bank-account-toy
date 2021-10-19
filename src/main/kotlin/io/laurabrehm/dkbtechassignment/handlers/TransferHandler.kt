package io.laurabrehm.dkbtechassignment.handlers

import io.laurabrehm.dkbtechassignment.usecases.Transfer
import kotlinx.serialization.Serializable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TransferHandler(private val transfer: Transfer) {

    @PostMapping("/transfer")
    fun createTransfer(@RequestBody request: TransferRequest) =
        transfer(
            sendingAccountIban = request.sendingIban,
            receivingAccountIban = request.receivingIban,
            amount = request.amount
        )
}

@Serializable
data class TransferRequest(
    val sendingIban: String,
    val receivingIban: String,
    val amount: Int
)
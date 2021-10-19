package io.laurabrehm.dkbtechassignment.handlers

import io.laurabrehm.dkbtechassignment.usecases.SecureAccount
import kotlinx.serialization.Serializable
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SecureAccountHandler(
    private val secureAccount: SecureAccount
) {

    @PutMapping("/account/{iban}")
    fun secureLockAccount(@PathVariable iban: String, @RequestBody request: SecureAccountRequest): Unit =
        secureAccount(iban, request.lock)
}

@Serializable
data class SecureAccountRequest(
    val lock: Boolean
)
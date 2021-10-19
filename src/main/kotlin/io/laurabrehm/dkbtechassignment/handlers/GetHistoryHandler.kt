package io.laurabrehm.dkbtechassignment.handlers

import io.laurabrehm.dkbtechassignment.usecases.GetHistory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetHistoryHandler(
    private val getHistory: GetHistory
) {

    @GetMapping("/account/{iban}/history")
    fun getAccountHistory(@PathVariable iban: String) =
        getHistory(iban)

}

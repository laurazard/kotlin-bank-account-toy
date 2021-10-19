package io.laurabrehm.dkbtechassignment.handlers

import com.ninjasquad.springmockk.MockkBean
import io.laurabrehm.dkbtechassignment.model.Transaction
import io.laurabrehm.dkbtechassignment.usecases.*
import io.mockk.every
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
internal class GetHistoryHandlerTest(
    @Autowired private val mockMvc: MockMvc
) {
    @MockkBean
    private lateinit var createAccount: CreateAccount
    @MockkBean
    private lateinit var deposit: Deposit
    @MockkBean
    private lateinit var getBalance: GetBalance
    @MockkBean
    private lateinit var getHistory: GetHistory
    @MockkBean
    private lateinit var secureAccount: SecureAccount
    @MockkBean
    private lateinit var transfer: Transfer

    @Test
    fun `gets account history by iban`() {
        val transactions = listOf(
            Transaction(
                sendingAccountIban = "sending-account-iban",
                receivingAccountIban = "receiving-account-iban",
                amount = 300
            )
        )
        every { getHistory("an-iban") } returns transactions

        mockMvc.get("/account/an-iban/history")
            .andExpect {
                status { isOk() }
                content { json(Json.encodeToString(transactions)) }
            }
    }
}
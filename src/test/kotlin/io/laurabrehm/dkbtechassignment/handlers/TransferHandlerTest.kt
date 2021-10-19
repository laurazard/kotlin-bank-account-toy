package io.laurabrehm.dkbtechassignment.handlers

import com.ninjasquad.springmockk.MockkBean
import io.laurabrehm.dkbtechassignment.usecases.*
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest
internal class TransferHandlerTest(
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
    fun `creates a transfer`() {
        every { transfer("an-iban", "another-iban", 300) } just runs

        mockMvc.post("/transfer") {
            content = Json.encodeToString(TransferRequest(
                sendingIban = "an-iban",
                receivingIban = "another-iban",
                amount = 300
            ))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        verify { transfer("an-iban", "another-iban", 300) }
    }
}
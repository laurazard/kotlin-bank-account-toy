package io.laurabrehm.dkbtechassignment.handlers

import com.ninjasquad.springmockk.MockkBean
import io.laurabrehm.dkbtechassignment.usecases.*
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put

@WebMvcTest
internal class SecureAccountHandlerTest(
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
    fun `locks account`() {
        every { secureAccount("an-iban", true) } just runs

        mockMvc.put("/account/an-iban") {
            content = Json.encodeToString(SecureAccountRequest(lock = true))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        verify { secureAccount("an-iban", true) }
    }

    @Test
    fun `unlocks account`() {
        every { secureAccount("an-iban", false) } just runs

        mockMvc.put("/account/an-iban") {
            content = Json.encodeToString(SecureAccountRequest(lock = false))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }

        verify { secureAccount("an-iban", false) }
    }
}
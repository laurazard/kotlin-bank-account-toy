package io.laurabrehm.dkbtechassignment.handlers

import com.ninjasquad.springmockk.MockkBean
import io.laurabrehm.dkbtechassignment.usecases.*
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest
internal class CreateAccountHandlerTest(
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
    fun `creates account`() {
        every { createAccount(any()) } returns "an-iban"
        mockMvc.post("/account")
            .andExpect {
                status { isOk() }
                content { string("{\"iban\":\"an-iban\"}") }
            }
    }
}
package io.laurabrehm.dkbtechassignment.handlers

import com.ninjasquad.springmockk.MockkBean
import io.laurabrehm.dkbtechassignment.usecases.*
import io.mockk.every
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.mockito.AdditionalMatchers.eq
import org.mockito.ArgumentMatchers.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import javax.management.Query.eq

@WebMvcTest
internal class GetBalanceHandlerTest(
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
    fun `gets account balance`() {
        every { getBalance("an-iban") } returns 300

        mockMvc.get("/account/an-iban/balance")
            .andExpect {
                status { isOk() }
                jsonPath("$.balance", Matchers.equalTo(300))
            }
    }
}
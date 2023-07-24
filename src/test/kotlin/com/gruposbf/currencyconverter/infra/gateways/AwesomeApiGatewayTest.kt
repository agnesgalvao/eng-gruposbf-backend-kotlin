package com.gruposbf.currencyconverter.infra.gateways

import com.gruposbf.currencyconverter.infra.gateways.exceptions.ClientException
import com.gruposbf.currencyconverter.infra.gateways.resources.AwesomeApiResponse
import com.gruposbf.currencyconverter.infra.repositories.resources.CurrencyEntity.CurrencyName.DOLLAR
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.rmi.UnexpectedException

class AwesomeApiGatewayTest {

    private val restTemplate = mockk<RestTemplate>(relaxed = true)

    private val baseUrl = "https://economia.awesomeapi.com.br"

    private val awesomeApiGateway = AwesomeApiGateway(restTemplate, baseUrl)

    private val url = "$baseUrl/${DOLLAR.code}-BRL/1"

    private val currencyCode = DOLLAR.code

    @Test
    fun `returns list of awesome response`() {
        val response = arrayOf(AwesomeApiResponse(BigDecimal.TEN.toString()))

        every { restTemplate.getForEntity(url,
            Array<AwesomeApiResponse>::class.java) } returns ResponseEntity(
            response, OK)

        val result = awesomeApiGateway.getLastValue(currencyCode)

        assertThat(result).isEqualTo(listOf(AwesomeApiResponse(BigDecimal.TEN.toString())))
    }

    @Test
    fun `throws Client Exception`() {
        every { restTemplate.getForEntity(url,
            Array<AwesomeApiResponse>::class.java) } throws RestClientException("")

        assertThatThrownBy { awesomeApiGateway.getLastValue(currencyCode) }
            .isInstanceOf(ClientException::class.java)
            .hasMessage("error: get for currency $currencyCode is not available.")
    }
    @Test
    fun `throws unexpected Exception when response is not expected`() {
        every { restTemplate.getForEntity(url,
            Array<AwesomeApiResponse>::class.java) } returns ResponseEntity(OK)

        assertThatThrownBy { awesomeApiGateway.getLastValue(DOLLAR.code) }
            .isInstanceOf(UnexpectedException::class.java)
            .hasMessage("error: can not parser response for get $currencyCode.")
    }
}

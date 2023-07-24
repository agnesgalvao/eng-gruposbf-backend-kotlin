package com.gruposbf.currencyconverter.infra.gateways

import com.gruposbf.currencyconverter.infra.gateways.exceptions.ClientException
import com.gruposbf.currencyconverter.infra.gateways.resources.AwesomeApiResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.rmi.UnexpectedException

@Component
class AwesomeApiGateway(
    private val restTemplate: RestTemplate,
    @Value("\${external-urls.awesome-api-url}")
    private val baseUrl: String,
) {

    fun getLastValue(currencyCode: String): List<AwesomeApiResponse> {
        val url = "$baseUrl/$currencyCode-BRL/1"

        try {
            return restTemplate.getForEntity(url, Array<AwesomeApiResponse>::class.java).body?.toList()
                ?: throw UnexpectedException("error: can not parser response for get $currencyCode.")
        } catch (exception: RestClientException) {
            throw ClientException("error: get for currency $currencyCode is not available.")
        }
    }
}

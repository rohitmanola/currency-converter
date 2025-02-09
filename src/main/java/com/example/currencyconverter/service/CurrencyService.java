package com.example.currencyconverter.service;

import com.example.currencyconverter.exception.CurrencyNotFoundException;
import com.example.currencyconverter.exception.ExternalApiException;
import com.example.currencyconverter.model.ConversionRequest;
import com.example.currencyconverter.model.ConversionResponse;
import com.example.currencyconverter.model.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    @Autowired
    private RestTemplate restTemplate;

    // Inject the API URL from application.properties
    @Value("${exchange.rate.api.url}")
    private String exchangeRateApiUrl;

    // Fetch exchange rates for the given base currency
    public ExchangeRateResponse getExchangeRates(String base) {
        try {
            ExchangeRateResponse response = restTemplate.getForObject(exchangeRateApiUrl, ExchangeRateResponse.class, base);
            if (response == null) {
                throw new ExternalApiException("Failed to retrieve exchange rates from external API.");
            }
            if (!"success".equalsIgnoreCase(response.getResult())) {
                throw new ExternalApiException("External API error: " + response.getErrorType());
            }
            return response;
        } catch (RestClientException e) {
            throw new ExternalApiException("Failed to fetch exchange rates from external API.", e);
        }
    }

    // Convert the given amount from one currency to another using live rates
    public ConversionResponse convertCurrency(ConversionRequest request) {
        ExchangeRateResponse ratesResponse = getExchangeRates(request.getFrom());

        if (ratesResponse == null || ratesResponse.getConversionRates() == null) {
            throw new ExternalApiException("Failed to retrieve exchange rates.");
        }

        Double rate = ratesResponse.getConversionRates().get(request.getTo());
        if (rate == null) {
            throw new CurrencyNotFoundException("Invalid target currency code: " + request.getTo());
        }

        double convertedAmount = request.getAmount() * rate;
        return new ConversionResponse(request.getFrom(), request.getTo(), request.getAmount(), convertedAmount);
    }
}
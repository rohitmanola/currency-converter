package com.example.currencyconverter.service;

import com.example.currencyconverter.model.ConversionRequest;
import com.example.currencyconverter.model.ConversionResponse;
import com.example.currencyconverter.model.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExchangeRates() {
        String base = "USD";
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setBase("USD");
        mockResponse.setDate("2025-02-08");
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.95);
        mockResponse.setRates(rates);

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class), eq(base)))
                .thenReturn(mockResponse);

        ExchangeRateResponse response = currencyService.getExchangeRates(base);
        assertNotNull(response);
        assertEquals("USD", response.getBase());
        assertEquals(0.95, response.getRates().get("EUR"));
    }

    @Test
    void testConvertCurrency() {
        // Setup mock for USD base conversion rates
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setBase("USD");
        mockResponse.setDate("2025-02-08");
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.95);
        mockResponse.setRates(rates);

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class), eq("USD")))
                .thenReturn(mockResponse);

        ConversionRequest request = new ConversionRequest();
        request.setFrom("USD");
        request.setTo("EUR");
        request.setAmount(100);

        ConversionResponse response = currencyService.convertCurrency(request);
        assertNotNull(response);
        assertEquals("USD", response.getFrom());
        assertEquals("EUR", response.getTo());
        assertEquals(100, response.getAmount());
        assertEquals(95.0, response.getConvertedAmount(), 0.001);
    }

    @Test
    void testConvertCurrency_InvalidCurrency() {
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setBase("USD");
        mockResponse.setDate("2025-02-08");
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 0.95);
        mockResponse.setRates(rates);

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class), eq("USD")))
                .thenReturn(mockResponse);

        ConversionRequest request = new ConversionRequest();
        request.setFrom("USD");
        request.setTo("ABC"); // Invalid currency code
        request.setAmount(100);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyService.convertCurrency(request);
        });
        String expectedMessage = "Invalid target currency code: ABC";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}

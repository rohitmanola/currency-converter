package com.example.currencyconverter.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversionRequest {
    private String from;
    private String to;
    private double amount;
}

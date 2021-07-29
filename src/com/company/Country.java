package com.company;

public class Country {

    private String countryCode;
    private Integer ibanLength;

    public Country(String countryCode, Integer ibanLength) {
        this.countryCode = countryCode;
        this.ibanLength = ibanLength;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Integer getIbanLength() {
        return ibanLength;
    }
}

package com.alvarorivas.finalproject.model.users;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String country;
    private String city;
    private String street;
    private int postalCode;

    public Address() {
    }

    public Address(String country, String city, String street, int postalCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
}

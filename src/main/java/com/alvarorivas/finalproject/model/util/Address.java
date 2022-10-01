package com.alvarorivas.finalproject.model.util;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {

    @NotNull
    private String address;

    public Address(String address) {
        this.address = address;
    }

    public Address() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

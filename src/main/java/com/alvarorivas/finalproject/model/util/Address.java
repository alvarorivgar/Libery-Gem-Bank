package com.alvarorivas.finalproject.model.util;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return Objects.equals(address, address1.address);
    }
}

package com.leftovers.user.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "id")
public class Customer extends Account {
    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false, referencedColumnName = "id")
    private Address address;

    public Customer(Address address) {
        this.setType("C");
        this.address = address;
    }

    public Customer() {
        this.setType("C");
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s', email='%s', phoneNo='%s', address=%s]",
                getId(), getFirstName(), getLastName(), getEmail(), getPhoneNo(), getAddress().toString()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getAddress(), customer.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAddress());
    }
}

package com.leftovers.user.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@SuperBuilder
@Entity
@Table(name = "tbl_customer")
@PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "id")
public class Customer extends Account {
    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id", nullable = false, referencedColumnName = "id")
    private Address address;

    protected Customer(final Customer.CustomerBuilder<?, ?> b) {
        super(b.type(AccountType.CUSTOMER));
        this.address = b.address;
    }

    public Customer(Address address) {
        this.setType(AccountType.CUSTOMER);
        this.address = address;
    }

    public Customer() {
        this.setType(AccountType.CUSTOMER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer customer = (Customer) o;
        return getId() != null && Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

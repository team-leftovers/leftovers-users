package com.leftovers.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_driver")
@PrimaryKeyJoinColumn(name = "account_id" , referencedColumnName = "id")
@SuperBuilder
@Getter
@Setter
public class Driver extends Account {
    @Column(name = "license_plate" , nullable = false)
    private String licensePlate;
    @Column(name = "rating" , nullable = false)
    private double rating;

    protected Driver(final Driver.DriverBuilder<?, ?> b) {
        super(b.type(AccountType.DRIVER));
        this.licensePlate = b.licensePlate;
        this.rating = b.rating;
    }

    public Driver(String licensePlate, double rating) {
        this.licensePlate = licensePlate;
        this.rating = rating;
    }

    public Driver() {
        this.setType(AccountType.DRIVER);
    }
}

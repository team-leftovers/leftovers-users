package com.leftovers.user.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "zip_code", nullable = false)
    private Integer zipCode;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "house_number", length = 5)
    private String houseNumber;

    @Column(name = "unit_number", length = 5)
    private String unitNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

    public Double getLongitude() {
        return longitude;
    }

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

    public Integer getZipCode() {
        return zipCode;
    }

	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}

    public String getCountry() {
        return country;
    }

	public void setCountry(String country) {
		this.country = country;
	}

    public String getStreetAddress() {
        return streetAddress;
    }

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

    public String getHouseNumber() {
        return houseNumber;
    }

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

    public String getUnitNumber() {
        return unitNumber;
    }

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

    @Override
    public String toString() {
        return String.format(
                "Address[id=%d, latitude=%s, longitude=%s, zipCode=%d, country='%s', streetAddress='%s', houseNumber='%s', unitNumber='%s']",
                id, latitude, longitude, zipCode, country, streetAddress, houseNumber, unitNumber
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return getId().equals(address.getId()) && getLatitude().equals(address.getLatitude()) && getLongitude().equals(address.getLongitude()) && getZipCode().equals(address.getZipCode()) && getCountry().equals(address.getCountry()) && getStreetAddress().equals(address.getStreetAddress()) && Objects.equals(getHouseNumber(), address.getHouseNumber()) && Objects.equals(getUnitNumber(), address.getUnitNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLatitude(), getLongitude(), getZipCode(), getCountry(), getStreetAddress(), getHouseNumber(), getUnitNumber());
    }
}
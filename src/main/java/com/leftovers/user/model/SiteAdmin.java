package com.leftovers.user.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@SuperBuilder
@Entity
@Table(name = "tbl_site_admin")
@PrimaryKeyJoinColumn(name = "account_id", referencedColumnName = "id")
public class SiteAdmin extends Account {

    protected SiteAdmin(final SiteAdmin.SiteAdminBuilder<?, ?> b) {
        super(b.type(AccountType.SITE_ADMIN));
    }

    public SiteAdmin() {
        this.setType(AccountType.SITE_ADMIN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SiteAdmin siteAdmin = (SiteAdmin) o;
        return getId() != null && Objects.equals(getId(), siteAdmin.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

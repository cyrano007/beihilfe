package com.poolix.beihilfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Rechnung.
 */
@Entity
@Table(name = "rechnung")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Rechnung implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "rechnung_id", nullable = false)
    private String rechnungID;

    @NotNull
    @Column(name = "austellungs_date", nullable = false)
    private LocalDate austellungsDate;

    @NotNull
    @Column(name = "bezahl_date", nullable = false)
    private String bezahlDate;

    @Column(name = "betrag", precision = 10, scale = 2)
    private BigDecimal betrag;

    @ManyToOne
    @JsonIgnoreProperties("")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRechnungID() {
        return rechnungID;
    }

    public Rechnung rechnungID(String rechnungID) {
        this.rechnungID = rechnungID;
        return this;
    }

    public void setRechnungID(String rechnungID) {
        this.rechnungID = rechnungID;
    }

    public LocalDate getAustellungsDate() {
        return austellungsDate;
    }

    public Rechnung austellungsDate(LocalDate austellungsDate) {
        this.austellungsDate = austellungsDate;
        return this;
    }

    public void setAustellungsDate(LocalDate austellungsDate) {
        this.austellungsDate = austellungsDate;
    }

    public String getBezahlDate() {
        return bezahlDate;
    }

    public Rechnung bezahlDate(String bezahlDate) {
        this.bezahlDate = bezahlDate;
        return this;
    }

    public void setBezahlDate(String bezahlDate) {
        this.bezahlDate = bezahlDate;
    }

    public BigDecimal getBetrag() {
        return betrag;
    }

    public Rechnung betrag(BigDecimal betrag) {
        this.betrag = betrag;
        return this;
    }

    public void setBetrag(BigDecimal betrag) {
        this.betrag = betrag;
    }

    public User getUser() {
        return user;
    }

    public Rechnung user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rechnung rechnung = (Rechnung) o;
        if (rechnung.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rechnung.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rechnung{" +
            "id=" + getId() +
            ", rechnungID='" + getRechnungID() + "'" +
            ", austellungsDate='" + getAustellungsDate() + "'" +
            ", bezahlDate='" + getBezahlDate() + "'" +
            ", betrag=" + getBetrag() +
            "}";
    }
}

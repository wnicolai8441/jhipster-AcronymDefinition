package com.acronym.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubContext.
 */
@Entity
@Table(name = "sub_context")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubContext implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subContext")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subContext", "user", "contexts" }, allowSetters = true)
    private Set<Acronym> acronyms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubContext id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SubContext name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Acronym> getAcronyms() {
        return this.acronyms;
    }

    public void setAcronyms(Set<Acronym> acronyms) {
        if (this.acronyms != null) {
            this.acronyms.forEach(i -> i.setSubContext(null));
        }
        if (acronyms != null) {
            acronyms.forEach(i -> i.setSubContext(this));
        }
        this.acronyms = acronyms;
    }

    public SubContext acronyms(Set<Acronym> acronyms) {
        this.setAcronyms(acronyms);
        return this;
    }

    public SubContext addAcronym(Acronym acronym) {
        this.acronyms.add(acronym);
        acronym.setSubContext(this);
        return this;
    }

    public SubContext removeAcronym(Acronym acronym) {
        this.acronyms.remove(acronym);
        acronym.setSubContext(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubContext)) {
            return false;
        }
        return getId() != null && getId().equals(((SubContext) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubContext{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

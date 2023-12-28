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
 * A Acronym.
 */
@Entity
@Table(name = "acronym")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Acronym implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "term_or_acronym", nullable = false)
    private String termOrAcronym;

    @Column(name = "name")
    private String name;

    @Column(name = "definition")
    private String definition;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_acronym__context",
        joinColumns = @JoinColumn(name = "acronym_id"),
        inverseJoinColumns = @JoinColumn(name = "context_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "acronyms", "subContexts" }, allowSetters = true)
    private Set<Context> contexts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Acronym id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTermOrAcronym() {
        return this.termOrAcronym;
    }

    public Acronym termOrAcronym(String termOrAcronym) {
        this.setTermOrAcronym(termOrAcronym);
        return this;
    }

    public void setTermOrAcronym(String termOrAcronym) {
        this.termOrAcronym = termOrAcronym;
    }

    public String getName() {
        return this.name;
    }

    public Acronym name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return this.definition;
    }

    public Acronym definition(String definition) {
        this.setDefinition(definition);
        return this;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Acronym user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Context> getContexts() {
        return this.contexts;
    }

    public void setContexts(Set<Context> contexts) {
        this.contexts = contexts;
    }

    public Acronym contexts(Set<Context> contexts) {
        this.setContexts(contexts);
        return this;
    }

    public Acronym addContext(Context context) {
        this.contexts.add(context);
        return this;
    }

    public Acronym removeContext(Context context) {
        this.contexts.remove(context);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Acronym)) {
            return false;
        }
        return getId() != null && getId().equals(((Acronym) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Acronym{" +
            "id=" + getId() +
            ", termOrAcronym='" + getTermOrAcronym() + "'" +
            ", name='" + getName() + "'" +
            ", definition='" + getDefinition() + "'" +
            "}";
    }
}

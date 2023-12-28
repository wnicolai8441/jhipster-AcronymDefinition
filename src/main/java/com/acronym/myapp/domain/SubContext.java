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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_sub_context__context",
        joinColumns = @JoinColumn(name = "sub_context_id"),
        inverseJoinColumns = @JoinColumn(name = "context_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "acronyms", "subContexts" }, allowSetters = true)
    private Set<Context> contexts = new HashSet<>();

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

    public Set<Context> getContexts() {
        return this.contexts;
    }

    public void setContexts(Set<Context> contexts) {
        this.contexts = contexts;
    }

    public SubContext contexts(Set<Context> contexts) {
        this.setContexts(contexts);
        return this;
    }

    public SubContext addContext(Context context) {
        this.contexts.add(context);
        return this;
    }

    public SubContext removeContext(Context context) {
        this.contexts.remove(context);
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

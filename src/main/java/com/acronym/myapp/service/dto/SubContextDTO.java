package com.acronym.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.acronym.myapp.domain.SubContext} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubContextDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Set<ContextDTO> contexts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ContextDTO> getContexts() {
        return contexts;
    }

    public void setContexts(Set<ContextDTO> contexts) {
        this.contexts = contexts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubContextDTO)) {
            return false;
        }

        SubContextDTO subContextDTO = (SubContextDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subContextDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubContextDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", contexts=" + getContexts() +
            "}";
    }
}

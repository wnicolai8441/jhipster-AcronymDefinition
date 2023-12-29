package com.acronym.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.acronym.myapp.domain.SubContext} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubContextDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private ContextDTO context;

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

    public ContextDTO getContext() {
        return context;
    }

    public void setContext(ContextDTO context) {
        this.context = context;
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
            ", context=" + getContext() +
            "}";
    }
}
package com.acronym.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.acronym.myapp.domain.Context} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContextDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContextDTO)) {
            return false;
        }

        ContextDTO contextDTO = (ContextDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contextDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContextDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

package com.acronym.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.acronym.myapp.domain.Acronym} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AcronymDTO implements Serializable {

    private Long id;

    @NotNull
    private String termOrAcronym;

    private String name;

    private String definition;

    @Lob
    private byte[] image;

    private String imageContentType;
    private SubContextDTO subContext;

    private UserDTO user;

    private Set<ContextDTO> contexts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTermOrAcronym() {
        return termOrAcronym;
    }

    public void setTermOrAcronym(String termOrAcronym) {
        this.termOrAcronym = termOrAcronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public SubContextDTO getSubContext() {
        return subContext;
    }

    public void setSubContext(SubContextDTO subContext) {
        this.subContext = subContext;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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
        if (!(o instanceof AcronymDTO)) {
            return false;
        }

        AcronymDTO acronymDTO = (AcronymDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, acronymDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AcronymDTO{" +
            "id=" + getId() +
            ", termOrAcronym='" + getTermOrAcronym() + "'" +
            ", name='" + getName() + "'" +
            ", definition='" + getDefinition() + "'" +
            ", image='" + getImage() + "'" +
            ", subContext=" + getSubContext() +
            ", user=" + getUser() +
            ", contexts=" + getContexts() +
            "}";
    }
}

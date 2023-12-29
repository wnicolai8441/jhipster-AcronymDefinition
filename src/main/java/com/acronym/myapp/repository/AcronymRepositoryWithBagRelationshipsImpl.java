package com.acronym.myapp.repository;

import com.acronym.myapp.domain.Acronym;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AcronymRepositoryWithBagRelationshipsImpl implements AcronymRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Acronym> fetchBagRelationships(Optional<Acronym> acronym) {
        return acronym.map(this::fetchContexts);
    }

    @Override
    public Page<Acronym> fetchBagRelationships(Page<Acronym> acronyms) {
        return new PageImpl<>(fetchBagRelationships(acronyms.getContent()), acronyms.getPageable(), acronyms.getTotalElements());
    }

    @Override
    public List<Acronym> fetchBagRelationships(List<Acronym> acronyms) {
        return Optional.of(acronyms).map(this::fetchContexts).orElse(Collections.emptyList());
    }

    Acronym fetchContexts(Acronym result) {
        return entityManager
            .createQuery("select acronym from Acronym acronym left join fetch acronym.contexts where acronym.id = :id", Acronym.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Acronym> fetchContexts(List<Acronym> acronyms) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, acronyms.size()).forEach(index -> order.put(acronyms.get(index).getId(), index));
        List<Acronym> result = entityManager
            .createQuery("select acronym from Acronym acronym left join fetch acronym.contexts where acronym in :acronyms", Acronym.class)
            .setParameter("acronyms", acronyms)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

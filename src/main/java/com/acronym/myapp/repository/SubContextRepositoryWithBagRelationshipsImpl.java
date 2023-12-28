package com.acronym.myapp.repository;

import com.acronym.myapp.domain.SubContext;
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
public class SubContextRepositoryWithBagRelationshipsImpl implements SubContextRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SubContext> fetchBagRelationships(Optional<SubContext> subContext) {
        return subContext.map(this::fetchContexts);
    }

    @Override
    public Page<SubContext> fetchBagRelationships(Page<SubContext> subContexts) {
        return new PageImpl<>(fetchBagRelationships(subContexts.getContent()), subContexts.getPageable(), subContexts.getTotalElements());
    }

    @Override
    public List<SubContext> fetchBagRelationships(List<SubContext> subContexts) {
        return Optional.of(subContexts).map(this::fetchContexts).orElse(Collections.emptyList());
    }

    SubContext fetchContexts(SubContext result) {
        return entityManager
            .createQuery(
                "select subContext from SubContext subContext left join fetch subContext.contexts where subContext.id = :id",
                SubContext.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<SubContext> fetchContexts(List<SubContext> subContexts) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, subContexts.size()).forEach(index -> order.put(subContexts.get(index).getId(), index));
        List<SubContext> result = entityManager
            .createQuery(
                "select subContext from SubContext subContext left join fetch subContext.contexts where subContext in :subContexts",
                SubContext.class
            )
            .setParameter("subContexts", subContexts)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

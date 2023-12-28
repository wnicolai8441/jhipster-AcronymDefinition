package com.acronym.myapp.domain;

import static com.acronym.myapp.domain.ContextTestSamples.*;
import static com.acronym.myapp.domain.SubContextTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.acronym.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SubContextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubContext.class);
        SubContext subContext1 = getSubContextSample1();
        SubContext subContext2 = new SubContext();
        assertThat(subContext1).isNotEqualTo(subContext2);

        subContext2.setId(subContext1.getId());
        assertThat(subContext1).isEqualTo(subContext2);

        subContext2 = getSubContextSample2();
        assertThat(subContext1).isNotEqualTo(subContext2);
    }

    @Test
    void contextTest() throws Exception {
        SubContext subContext = getSubContextRandomSampleGenerator();
        Context contextBack = getContextRandomSampleGenerator();

        subContext.addContext(contextBack);
        assertThat(subContext.getContexts()).containsOnly(contextBack);

        subContext.removeContext(contextBack);
        assertThat(subContext.getContexts()).doesNotContain(contextBack);

        subContext.contexts(new HashSet<>(Set.of(contextBack)));
        assertThat(subContext.getContexts()).containsOnly(contextBack);

        subContext.setContexts(new HashSet<>());
        assertThat(subContext.getContexts()).doesNotContain(contextBack);
    }
}

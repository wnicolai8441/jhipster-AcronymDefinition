package com.acronym.myapp.domain;

import static com.acronym.myapp.domain.AcronymTestSamples.*;
import static com.acronym.myapp.domain.ContextTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.acronym.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AcronymTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Acronym.class);
        Acronym acronym1 = getAcronymSample1();
        Acronym acronym2 = new Acronym();
        assertThat(acronym1).isNotEqualTo(acronym2);

        acronym2.setId(acronym1.getId());
        assertThat(acronym1).isEqualTo(acronym2);

        acronym2 = getAcronymSample2();
        assertThat(acronym1).isNotEqualTo(acronym2);
    }

    @Test
    void contextTest() throws Exception {
        Acronym acronym = getAcronymRandomSampleGenerator();
        Context contextBack = getContextRandomSampleGenerator();

        acronym.addContext(contextBack);
        assertThat(acronym.getContexts()).containsOnly(contextBack);

        acronym.removeContext(contextBack);
        assertThat(acronym.getContexts()).doesNotContain(contextBack);

        acronym.contexts(new HashSet<>(Set.of(contextBack)));
        assertThat(acronym.getContexts()).containsOnly(contextBack);

        acronym.setContexts(new HashSet<>());
        assertThat(acronym.getContexts()).doesNotContain(contextBack);
    }
}

package com.acronym.myapp.domain;

import static com.acronym.myapp.domain.AcronymTestSamples.*;
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
    void acronymTest() throws Exception {
        SubContext subContext = getSubContextRandomSampleGenerator();
        Acronym acronymBack = getAcronymRandomSampleGenerator();

        subContext.addAcronym(acronymBack);
        assertThat(subContext.getAcronyms()).containsOnly(acronymBack);
        assertThat(acronymBack.getSubContext()).isEqualTo(subContext);

        subContext.removeAcronym(acronymBack);
        assertThat(subContext.getAcronyms()).doesNotContain(acronymBack);
        assertThat(acronymBack.getSubContext()).isNull();

        subContext.acronyms(new HashSet<>(Set.of(acronymBack)));
        assertThat(subContext.getAcronyms()).containsOnly(acronymBack);
        assertThat(acronymBack.getSubContext()).isEqualTo(subContext);

        subContext.setAcronyms(new HashSet<>());
        assertThat(subContext.getAcronyms()).doesNotContain(acronymBack);
        assertThat(acronymBack.getSubContext()).isNull();
    }
}

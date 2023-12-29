package com.acronym.myapp.domain;

import static com.acronym.myapp.domain.ContextTestSamples.*;
import static com.acronym.myapp.domain.SubContextTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.acronym.myapp.web.rest.TestUtil;
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

        subContext.setContext(contextBack);
        assertThat(subContext.getContext()).isEqualTo(contextBack);

        subContext.context(null);
        assertThat(subContext.getContext()).isNull();
    }
}

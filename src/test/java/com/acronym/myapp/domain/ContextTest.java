package com.acronym.myapp.domain;

import static com.acronym.myapp.domain.AcronymTestSamples.*;
import static com.acronym.myapp.domain.ContextTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.acronym.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContextTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Context.class);
        Context context1 = getContextSample1();
        Context context2 = new Context();
        assertThat(context1).isNotEqualTo(context2);

        context2.setId(context1.getId());
        assertThat(context1).isEqualTo(context2);

        context2 = getContextSample2();
        assertThat(context1).isNotEqualTo(context2);
    }

    @Test
    void acronymTest() throws Exception {
        Context context = getContextRandomSampleGenerator();
        Acronym acronymBack = getAcronymRandomSampleGenerator();

        context.addAcronym(acronymBack);
        assertThat(context.getAcronyms()).containsOnly(acronymBack);
        assertThat(acronymBack.getContexts()).containsOnly(context);

        context.removeAcronym(acronymBack);
        assertThat(context.getAcronyms()).doesNotContain(acronymBack);
        assertThat(acronymBack.getContexts()).doesNotContain(context);

        context.acronyms(new HashSet<>(Set.of(acronymBack)));
        assertThat(context.getAcronyms()).containsOnly(acronymBack);
        assertThat(acronymBack.getContexts()).containsOnly(context);

        context.setAcronyms(new HashSet<>());
        assertThat(context.getAcronyms()).doesNotContain(acronymBack);
        assertThat(acronymBack.getContexts()).doesNotContain(context);
    }
}

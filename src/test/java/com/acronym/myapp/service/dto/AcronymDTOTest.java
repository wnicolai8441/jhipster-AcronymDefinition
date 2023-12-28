package com.acronym.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.acronym.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcronymDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcronymDTO.class);
        AcronymDTO acronymDTO1 = new AcronymDTO();
        acronymDTO1.setId(1L);
        AcronymDTO acronymDTO2 = new AcronymDTO();
        assertThat(acronymDTO1).isNotEqualTo(acronymDTO2);
        acronymDTO2.setId(acronymDTO1.getId());
        assertThat(acronymDTO1).isEqualTo(acronymDTO2);
        acronymDTO2.setId(2L);
        assertThat(acronymDTO1).isNotEqualTo(acronymDTO2);
        acronymDTO1.setId(null);
        assertThat(acronymDTO1).isNotEqualTo(acronymDTO2);
    }
}

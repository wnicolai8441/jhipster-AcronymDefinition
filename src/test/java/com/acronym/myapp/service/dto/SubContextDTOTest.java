package com.acronym.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.acronym.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubContextDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubContextDTO.class);
        SubContextDTO subContextDTO1 = new SubContextDTO();
        subContextDTO1.setId(1L);
        SubContextDTO subContextDTO2 = new SubContextDTO();
        assertThat(subContextDTO1).isNotEqualTo(subContextDTO2);
        subContextDTO2.setId(subContextDTO1.getId());
        assertThat(subContextDTO1).isEqualTo(subContextDTO2);
        subContextDTO2.setId(2L);
        assertThat(subContextDTO1).isNotEqualTo(subContextDTO2);
        subContextDTO1.setId(null);
        assertThat(subContextDTO1).isNotEqualTo(subContextDTO2);
    }
}

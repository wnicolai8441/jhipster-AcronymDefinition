package com.acronym.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class AcronymMapperTest {

    private AcronymMapper acronymMapper;

    @BeforeEach
    public void setUp() {
        acronymMapper = new AcronymMapperImpl();
    }
}

package com.acronym.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class ContextMapperTest {

    private ContextMapper contextMapper;

    @BeforeEach
    public void setUp() {
        contextMapper = new ContextMapperImpl();
    }
}

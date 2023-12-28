package com.acronym.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubContextTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SubContext getSubContextSample1() {
        return new SubContext().id(1L).name("name1");
    }

    public static SubContext getSubContextSample2() {
        return new SubContext().id(2L).name("name2");
    }

    public static SubContext getSubContextRandomSampleGenerator() {
        return new SubContext().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}

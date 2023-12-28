package com.acronym.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AcronymTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Acronym getAcronymSample1() {
        return new Acronym().id(1L).termOrAcronym("termOrAcronym1").name("name1").definition("definition1");
    }

    public static Acronym getAcronymSample2() {
        return new Acronym().id(2L).termOrAcronym("termOrAcronym2").name("name2").definition("definition2");
    }

    public static Acronym getAcronymRandomSampleGenerator() {
        return new Acronym()
            .id(longCount.incrementAndGet())
            .termOrAcronym(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .definition(UUID.randomUUID().toString());
    }
}

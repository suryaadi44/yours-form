package com.yourstechnology.form.utils.idGenerator;

import java.util.EnumSet;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;

import com.fasterxml.uuid.Generators;

public class UuidGenerator implements BeforeExecutionGenerator {
    @Override
    public EnumSet<EventType> getEventTypes() {
        return EventTypeSets.INSERT_ONLY;
    }

    @Override
    public Object generate(
            SharedSessionContractImplementor session,
            Object object,
            Object currentValue,
            EventType eventType) {
        return Generators.timeBasedEpochRandomGenerator().generate();
    }
}
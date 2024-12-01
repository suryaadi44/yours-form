package com.yourstechnology.form.config;

import java.util.TimeZone;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
class TimeZoneLocaleBeanFactoryPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware, Ordered {
    private Environment env;

    @Override
    public void postProcessBeanFactory(final @NonNull ConfigurableListableBeanFactory ignored) throws BeansException {
        TimeZone.setDefault(
                TimeZone.getTimeZone(env.getProperty("server.time-zone", "UTC")));
    }

    @Override
    public void setEnvironment(final @NonNull Environment environment) {
        this.env = environment;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
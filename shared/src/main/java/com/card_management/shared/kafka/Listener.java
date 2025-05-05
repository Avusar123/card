package com.card_management.shared.kafka;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Service
@Transactional
@Validated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Listener {
}

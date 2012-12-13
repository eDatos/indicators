package es.gobcan.istac.indicators.web.validator;

import org.springframework.validation.Errors;

/**
 */
public class DummyValidator implements org.springframework.validation.Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return true; // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void validate(Object target, Errors errors) {
        // To change body of implemented methods use File | Settings | File Templates.
    }
}

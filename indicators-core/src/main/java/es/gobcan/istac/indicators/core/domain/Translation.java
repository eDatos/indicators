package es.gobcan.istac.indicators.core.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Translations by code
 */
@Entity
@Table(name = "TB_TRANSLATIONS", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODE"})})
public class Translation extends TranslationBase {
    private static final long serialVersionUID = 1L;

    public Translation() {
    }
}

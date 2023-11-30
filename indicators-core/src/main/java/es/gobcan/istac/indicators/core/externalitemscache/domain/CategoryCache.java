package es.gobcan.istac.indicators.core.externalitemscache.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity representing CategoryCache.
 * <p>
 * This class is responsible for the domain object related
 * business logic for CategoryCache. Properties and associations are
 * implemented in the generated base class {@link es.gobcan.istac.indicators.core.externalitemscache.domain.CategoryCacheBase}.
 */
@Entity
@Table(name = "TB_CATEGORY_CACHE")
public class CategoryCache extends CategoryCacheBase {
    private static final long serialVersionUID = 1L;

    public CategoryCache() {
    }
}

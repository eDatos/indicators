package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.ConditionRoot;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersionProperties;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;

/**
 * Repository implementation for IndicatorsSystemVersion
 */
@Repository("indicatorsSystemVersionRepository")
public class IndicatorsSystemVersionRepositoryImpl extends IndicatorsSystemVersionRepositoryBase {

    public IndicatorsSystemVersionRepositoryImpl() {
    }

    @Override
    public IndicatorsSystemVersion retrieveIndicatorsSystemVersion(String uuid, String versionNumber) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        parameters.put("versionNumber", versionNumber);
        List<IndicatorsSystemVersion> result = findByQuery("from IndicatorsSystemVersion isv where isv.indicatorsSystem.uuid = :uuid and isv.versionNumber = :versionNumber", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<IndicatorsSystemVersion> retrieveIndicatorsSystemPublishedForIndicator(String indicatorUuid) throws MetamacException {

        ConditionRoot<IndicatorsSystemVersion> criteria = ConditionalCriteriaBuilder.criteriaFor(IndicatorsSystemVersion.class);
        
        // Restrictions
        criteria.withProperty(IndicatorsSystemVersionProperties.procStatus()).eq(IndicatorsSystemProcStatusEnum.PUBLISHED);
        criteria.withProperty(new LeafProperty<IndicatorsSystemVersion>("childrenAllLevels.indicatorInstance.indicator", "uuid", false, IndicatorsSystemVersion.class)).eq(indicatorUuid);
        
        // Distinct
        criteria.distinctRoot();
        
        List<ConditionalCriteria> conditions = criteria.build();
        PagingParameter pagingParameter = PagingParameter.noLimits();

        // Find
        PagedResult<IndicatorsSystemVersion> result = findByCondition(conditions, pagingParameter);
        return result.getValues();
    }
}

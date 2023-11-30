package es.gobcan.istac.indicators.web.shared.external;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public class RestWebCriteriaUtils {

    public static SrmExternalResourceRestCriteria buildItemSchemeWebCriteria(SrmExternalResourceRestCriteria itemSchemeRestCriteria, TypeExternalArtefactsEnum[] types) {
        itemSchemeRestCriteria.setExternalArtifactType(types[0]);

        return itemSchemeRestCriteria;
    }

    public static SrmItemRestCriteria buildItemWebCriteria(SrmItemRestCriteria itemRestCriteria, TypeExternalArtefactsEnum[] types) {
        itemRestCriteria.setExternalArtifactType(types[0]);
        return itemRestCriteria;
    }
}

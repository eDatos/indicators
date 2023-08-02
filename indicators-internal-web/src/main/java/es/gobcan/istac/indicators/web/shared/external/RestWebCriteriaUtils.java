package es.gobcan.istac.indicators.web.shared.external;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public class RestWebCriteriaUtils {

    public static SrmExternalResourceRestCriteria buildItemSchemeWebCriteria(SrmExternalResourceRestCriteria itemSchemeRestCriteria, TypeExternalArtefactsEnum[] types) {
        if (isCategoryElementType(types[0])) {
            itemSchemeRestCriteria.setExternalArtifactType(types[0]);
        }
        return itemSchemeRestCriteria;
    }

    private static boolean isCategoryElementType(TypeExternalArtefactsEnum type) {
        return TypeExternalArtefactsEnum.CATEGORY_ELEMENT.equals(type);
    }

    public static SrmItemRestCriteria buildItemWebCriteria(SrmItemRestCriteria itemRestCriteria, TypeExternalArtefactsEnum[] types) {
        if (isCategoryElementType(types[0])) {
            itemRestCriteria.setExternalArtifactType(types[0]);
        }
        return itemRestCriteria;
    }
}

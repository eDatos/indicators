package es.gobcan.istac.indicators.web.shared.external;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;

public class OrganisationSchemeRestCriteria extends SrmExternalResourceRestCriteria {

    private static final long           serialVersionUID = 1L;

    private TypeExternalArtefactsEnum[] organisationSchemeTypes;

    public OrganisationSchemeRestCriteria() {
    }

    public TypeExternalArtefactsEnum[] getOrganisationSchemeTypes() {
        return organisationSchemeTypes;
    }

    public void setOrganisationSchemeTypes(TypeExternalArtefactsEnum[] organisationSchemeTypes) {
        this.organisationSchemeTypes = organisationSchemeTypes;
    }
}

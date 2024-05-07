package es.gobcan.istac.indicators.web.shared.external;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;

public class OrganisationRestCriteria extends SrmItemRestCriteria {

    private static final long           serialVersionUID = 1L;

    private TypeExternalArtefactsEnum[] organisationTypes;

    public OrganisationRestCriteria() {
    }

    public TypeExternalArtefactsEnum[] getOrganisationTypes() {
        return organisationTypes;
    }

    public void setOrganisationTypes(TypeExternalArtefactsEnum[] organisationTypes) {
        this.organisationTypes = organisationTypes;
    }
}

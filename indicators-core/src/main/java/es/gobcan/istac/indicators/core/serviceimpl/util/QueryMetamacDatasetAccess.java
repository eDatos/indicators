package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentations;

public class QueryMetamacDatasetAccess extends CommonMetamacDatasetAccess {

    protected Query query;
    public QueryMetamacDatasetAccess(Query query, Map<String, String> variableElementsByCode, List<String> geographicalDimensionsId) throws MetamacException {
        super();
        this.query = query;
        initialize(variableElementsByCode, geographicalDimensionsId);
    }

    @Override
    protected DimensionRepresentations getDimensions() {
        return this.query.getData().getDimensions();
    }
    @Override
    protected Attributes getMetadataAttributes() {
        return this.query.getMetadata().getAttributes();
    }
    @Override
    protected Data getData() {
        return this.query.getData();
    }

}

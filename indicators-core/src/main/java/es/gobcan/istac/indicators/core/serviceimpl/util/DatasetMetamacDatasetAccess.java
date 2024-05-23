package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionRepresentations;

public class DatasetMetamacDatasetAccess extends CommonMetamacDatasetAccess {

    protected Dataset dataset;

    public DatasetMetamacDatasetAccess(Dataset dataset, Map<String, String> variableElementsByCode, List<String> geographicalDimensionsId) throws MetamacException {
        super();
        this.dataset = dataset;
        initialize(variableElementsByCode, geographicalDimensionsId);

    }

    @Override
    protected DimensionRepresentations getDimensions() {
        return this.dataset.getData().getDimensions();
    }
    @Override
    protected Attributes getMetadataAttributes() {
        return this.dataset.getMetadata().getAttributes();
    }
    @Override
    protected Data getData() {
        return this.dataset.getData();
    }

}

package es.gobcan.istac.indicators.rest.mapper;

import es.gobcan.istac.indicators.core.domain.*;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.rest.types.*;

import java.util.List;

public interface Do2TypeMapper {

    // Indicators systems
    public IndicatorsSystemType indicatorsSystemDoToType(final IndicatorsSystemVersion source, final String baseURL);
    public IndicatorsSystemBaseType indicatorsSystemDoToBaseType(final IndicatorsSystemVersion source, final String baseURL);
    public List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(final List<IndicatorsSystemVersion> sources, final String baseURL);
    public IndicatorsSystemHistoryType indicatorsSystemHistoryDoToType(final IndicatorsSystemHistory systemHistory, final String baseURL);

    // Indicators Instance
    public IndicatorInstanceType indicatorsInstanceDoToType(final IndicatorInstance source, final String baseURL);
    public List<IndicatorInstanceBaseType> indicatorsInstanceDoToBaseType(final List<IndicatorInstance> sources, final String baseURL);
    
    // Indicator
    public IndicatorType indicatorDoToType(final IndicatorVersion sources, final String baseURL);
    public List<IndicatorBaseType> indicatorDoToBaseType(final List<IndicatorVersion> sources, final String baseURL);
    
    // Granularities
    public MetadataGranularityType geographicalGranularityDoToType(GeographicalGranularity geographicalGranularity);
    public List<MetadataGranularityType> geographicalGranularityDoToType(List<GeographicalGranularity> geographicalGranularities);
    public List<MetadataGranularityType> timeGranularityDoToType(List<TimeGranularity> timeGranularities);
    
    // Subjects
    public SubjectType subjectDoToType(final SubjectIndicatorResult subject, List<IndicatorVersion> indicators, String baseUrl);
    public List<SubjectBaseType> subjectDoToBaseType(List<SubjectIndicatorResult> subjects, String baseUrl);
    
    // Data
    public DataType createDataType(DataTypeRequest dataTypeRequest, boolean includeObservationMetadata);

    // GeographicalValues
    public List<GeographicalValueType> geographicalValuesDoToType(List<GeographicalValue> geographicalValues);

    void indicatorDoToMetadataType(IndicatorVersion source, MetadataType target, String baseURL) throws Exception;

    void indicatorsInstanceDoToMetadataType(IndicatorInstance source, MetadataType target, String baseURL);
}

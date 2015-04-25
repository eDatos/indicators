package es.gobcan.istac.indicators.rest.mapper;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;
import es.gobcan.istac.indicators.core.vo.GeographicalValueVO;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.types.GeographicalValueType;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemHistoryType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;
import es.gobcan.istac.indicators.rest.types.MetadataType;
import es.gobcan.istac.indicators.rest.types.SubjectBaseType;
import es.gobcan.istac.indicators.rest.types.SubjectType;

public interface Do2TypeMapper {

    // Indicators systems
    IndicatorsSystemType indicatorsSystemDoToType(final IndicatorsSystemVersion source, final String baseURL);
    IndicatorsSystemBaseType indicatorsSystemDoToBaseType(final IndicatorsSystemVersion source, final String baseURL);
    List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(final List<IndicatorsSystemVersion> sources, final String baseURL);
    IndicatorsSystemHistoryType indicatorsSystemHistoryDoToType(final IndicatorsSystemHistory systemHistory, final String baseURL);

    // Indicators Instance
    IndicatorInstanceType indicatorsInstanceDoToType(final IndicatorInstance source, final String baseURL);
    List<IndicatorInstanceBaseType> indicatorsInstanceDoToBaseType(final List<IndicatorInstance> sources, final String baseURL);

    // Indicator
    IndicatorType indicatorDoToType(final IndicatorVersion sources, final String baseURL);
    List<IndicatorBaseType> indicatorDoToBaseType(final List<IndicatorVersion> sources, final String baseURL);

    // Granularities
    MetadataGranularityType geographicalGranularityDoToType(GeographicalGranularity geographicalGranularity);
    List<MetadataGranularityType> geographicalGranularityDoToType(List<GeographicalGranularity> geographicalGranularities);
    List<MetadataGranularityType> timeGranularityDoToType(List<TimeGranularity> timeGranularities);

    // Subjects
    SubjectType subjectDoToType(final SubjectIndicatorResult subject, List<IndicatorVersion> indicators, String baseUrl);
    List<SubjectBaseType> subjectDoToBaseType(List<SubjectIndicatorResult> subjects, String baseUrl);

    // Data
    DataType createDataType(DataTypeRequest dataTypeRequest, boolean includeObservationMetadata);

    // GeographicalValues
    List<GeographicalValueType> geographicalValuesDoToType(List<GeographicalValue> geographicalValues);

    List<GeographicalValueType> geographicalValuesVOToType(List<GeographicalValueVO> geographicalValues);

    void indicatorDoToMetadataType(IndicatorVersion source, MetadataType target, String baseURL) throws MetamacException;

    void indicatorsInstanceDoToMetadataType(IndicatorInstance source, MetadataType target, String baseURL);
}

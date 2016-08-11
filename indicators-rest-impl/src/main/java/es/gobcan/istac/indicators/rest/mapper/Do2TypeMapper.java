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
    IndicatorsSystemType indicatorsSystemDoToType(final IndicatorsSystemVersion source);
    IndicatorsSystemBaseType indicatorsSystemDoToBaseType(final IndicatorsSystemVersion source);
    List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(final List<IndicatorsSystemVersion> sources);
    IndicatorsSystemHistoryType indicatorsSystemHistoryDoToType(final IndicatorsSystemHistory systemHistory);

    // Indicators Instance
    IndicatorInstanceType indicatorsInstanceDoToType(final IndicatorInstance source);
    List<IndicatorInstanceBaseType> indicatorsInstanceDoToBaseType(final List<IndicatorInstance> sources);

    // Indicator
    IndicatorType indicatorDoToType(final IndicatorVersion sources);
    List<IndicatorBaseType> indicatorDoToBaseType(final List<IndicatorVersion> sources);

    // Granularities
    MetadataGranularityType geographicalGranularityDoToType(GeographicalGranularity geographicalGranularity);
    List<MetadataGranularityType> geographicalGranularityDoToType(List<GeographicalGranularity> geographicalGranularities);
    List<MetadataGranularityType> timeGranularityDoToType(List<TimeGranularity> timeGranularities);

    // Subjects
    SubjectType subjectDoToType(final SubjectIndicatorResult subject, List<IndicatorVersion> indicators);
    List<SubjectBaseType> subjectDoToBaseType(List<SubjectIndicatorResult> subjects);

    // Data
    DataType createDataType(DataTypeRequest dataTypeRequest, boolean includeObservationMetadata);

    // GeographicalValues
    List<GeographicalValueType> geographicalValuesDoToType(List<GeographicalValue> geographicalValues);

    List<GeographicalValueType> geographicalValuesVOToType(List<GeographicalValueVO> geographicalValues);

    void indicatorDoToMetadataType(IndicatorVersion source, MetadataType target) throws MetamacException;

    void indicatorsInstanceDoToMetadataType(IndicatorInstance source, MetadataType target);
}

package es.gobcan.istac.indicators.rest.mapper;

import java.util.List;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.domain.TimeGranularity;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceType;
import es.gobcan.istac.indicators.rest.types.IndicatorType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemType;
import es.gobcan.istac.indicators.rest.types.MetadataGranularityType;
import es.gobcan.istac.indicators.rest.types.ThemeType;

public interface Do2TypeMapper {

    // Indicators systems
    public IndicatorsSystemType indicatorsSystemDoToType(final IndicatorsSystemVersion source, final String baseURL);
    public IndicatorsSystemBaseType indicatorsSystemDoToBaseType(final IndicatorsSystemVersion source, final String baseURL);
    public List<IndicatorsSystemBaseType> indicatorsSystemDoToBaseType(final List<IndicatorsSystemVersion> sources, final String baseURL);

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
    public List<ThemeType> subjectDoToType(List<Subject> subjects);

}

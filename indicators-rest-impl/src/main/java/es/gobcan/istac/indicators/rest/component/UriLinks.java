package es.gobcan.istac.indicators.rest.component;

import javax.annotation.PostConstruct;

import org.siemac.metamac.rest.utils.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;

@Component
public class UriLinks {

    @Autowired
    IndicatorsConfigurationService configurationService;

    private String                 indicatorsApiExternalEndpointV10;

    @PostConstruct
    public void init() throws Exception {
        String indicatorsApiExternalEndpoint = configurationService.retrieveIndicatorsExternalApiUrlBase();
        indicatorsApiExternalEndpointV10 = RestUtils.createLink(indicatorsApiExternalEndpoint, IndicatorsRestConstants.API_VERSION_1_0);
    }

    // ----------------------------------------------------------------------------------------------------
    // INDICATORS
    // ----------------------------------------------------------------------------------------------------

    // /vX/indicators
    public String getIndicatorsLink() {
        return RestUtils.createLink(indicatorsApiExternalEndpointV10, IndicatorsRestConstants.API_INDICATORS_INDICATORS);
    }

    // /vX/indicators/{id}
    public String getIndicatorLink(String id) {
        return RestUtils.createLink(getIndicatorsLink(), id);
    }

    // /vX/indicators/{id}/data
    public String getIndicatorDataLink(String id) {
        return RestUtils.createLink(getIndicatorLink(id), IndicatorsRestConstants.API_INDICATORS_INDICATORS_DATA);
    }

    // /vX/indicators/{id}/data + parameters
    public String getIndicatorDataSelfLink(String indicatorCode, String fields, String representation, String granularity) {
        String link = getIndicatorDataLink(indicatorCode);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_FIELDS, fields);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_REPRESENTATION, representation);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_GRANULARITY, granularity);
        return link;
    }

    // ----------------------------------------------------------------------------------------------------
    // INDICATORS SYSTEMS
    // ----------------------------------------------------------------------------------------------------

    // /vX/indicatorsSystems
    public String getIndicatorsSystemsLink() {
        return RestUtils.createLink(indicatorsApiExternalEndpointV10, IndicatorsRestConstants.API_INDICATORS_INDICATORS_SYSTEMS);
    }

    // /vX/indicatorSystem/{id}
    public String getIndicatorSystemLink(String id) {
        return RestUtils.createLink(getIndicatorsSystemsLink(), id);
    }

    // /vX/indicatorsSystems/{id}/indicatorsInstances
    public String getIndicatorInstancesLink(String id) {
        return RestUtils.createLink(getIndicatorSystemLink(id), IndicatorsRestConstants.API_INDICATORS_INDICATORS_INSTANCES);
    }

    // /vX/indicatorsSystems/{id}/indicatorsInstances/{idIndicatorInstance}
    public String getIndicatorInstanceLink(String idIndicatorSystem, String idIndicatorInstance) {
        return RestUtils.createLink(getIndicatorInstancesLink(idIndicatorSystem), idIndicatorInstance);
    }

    // /vX/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{idIndicatorInstance}/data
    public String getIndicatorInstanceDataLink(String idIndicatorSystem, String idIndicatorInstance) {
        return RestUtils.createLink(getIndicatorInstanceLink(idIndicatorSystem, idIndicatorInstance), IndicatorsRestConstants.API_INDICATORS_INDICATORS_INSTANCES_DATA);
    }

    // /vX/indicatorsSystems/{idIndicatorSystem}/indicatorsInstances/{idIndicatorInstance}/data + parameters
    public String getIndicatorInstanceDataSelfLink(String idIndicatorSystem, String idIndicatorInstance, String fields, String representation, String granularity) {
        String link = getIndicatorInstanceDataLink(idIndicatorSystem, idIndicatorInstance);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_FIELDS, fields);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_REPRESENTATION, representation);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_GRANULARITY, granularity);
        return link;
    }

    // ----------------------------------------------------------------------------------------------------
    // SUBJECTS
    // ----------------------------------------------------------------------------------------------------

    // /vX/subjects
    public String getSubjectsLink() {
        return RestUtils.createLink(indicatorsApiExternalEndpointV10, IndicatorsRestConstants.API_INDICATORS_SUBJECTS);
    }

    // /vX/subjects/{id}
    public String getSubjectLink(String id) {
        return RestUtils.createLink(getSubjectsLink(), id);

    }

    // ----------------------------------------------------------------------------------------------------
    // GEOGRAPHICAL VALUES
    // ----------------------------------------------------------------------------------------------------

    // /vX/geographicalValues
    public String getGeographicalValuesLink() {
        return RestUtils.createLink(indicatorsApiExternalEndpointV10, IndicatorsRestConstants.API_INDICATORS_GEOGRAPHICAL_VALUES);

    }

    // /vX/geographicalValues + parameters
    public String getGeographicalValuesSelfLink(String subjectCode, String systemCode, String geographicalGranularityCode) {
        String link = getGeographicalValuesLink();
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_SUBJECT_CODE, subjectCode);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_SYSTEM_CODE, systemCode);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.GEOGRAPHICAL_GRANULARITY_CODE, geographicalGranularityCode);
        return link;
    }

    // ----------------------------------------------------------------------------------------------------
    // GEOGRAPHCIAL GRANULARITIES
    // ----------------------------------------------------------------------------------------------------

    // /vX/geographicGranularities
    public String getGeographicalGranularitiesLink() {
        return RestUtils.createLink(indicatorsApiExternalEndpointV10, IndicatorsRestConstants.API_INDICATORS_GEOGRAPHIC_GRANULARITIES);

    }

    // /vX/geographicGranularities + parameters
    public String getGeographicalGranularitiesSelfLink(String subjectCode, String systemCode) {
        String link = getGeographicalGranularitiesLink();
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_SUBJECT_CODE, subjectCode);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_SYSTEM_CODE, systemCode);
        return link;
    }

    // ----------------------------------------------------------------------------------------------------
    // TIME GRANULARITIES
    // ----------------------------------------------------------------------------------------------------

    // /vX/timeGranularities
    public String getTimeGranularitiesLink() {
        return RestUtils.createLink(indicatorsApiExternalEndpointV10, IndicatorsRestConstants.API_INDICATORS_TIME_GRANULARITIES);
    }

}

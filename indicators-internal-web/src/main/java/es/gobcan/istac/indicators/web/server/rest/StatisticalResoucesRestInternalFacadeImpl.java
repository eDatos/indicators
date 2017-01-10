package es.gobcan.istac.indicators.web.server.rest;

import static es.gobcan.istac.indicators.web.server.utils.MetamacWebCriteriaUtils.buildQueryForQueryVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionParameters;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Queries;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Query;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.web.common.server.rest.utils.RestExceptionUtils;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.service.RestApiLocator;
import es.gobcan.istac.indicators.core.service.StatisticalResoucesRestInternalService;
import es.gobcan.istac.indicators.web.server.utils.ExternalItemWebUtils;

@Component
public class StatisticalResoucesRestInternalFacadeImpl implements StatisticalResoucesRestInternalFacade {

    @Autowired
    private RestApiLocator       restApiLocator;

    @Autowired
    private RestExceptionUtils   restExceptionUtils;
    
    @Autowired
    private StatisticalResoucesRestInternalService statisticalResoucesRestInternalService;

    @Autowired
    private ConfigurationService configurationService;
    
    @Override
    public ExternalItemsResult findQueries(ServiceContext serviceContext, int firstResult, int maxResult, MetamacWebCriteria criteria) throws MetamacWebException {
        try {
            String query = buildQueryForQueryVersion(criteria);
            String limit = String.valueOf(maxResult);
            String offset = String.valueOf(firstResult);
            String orderBy = null;
            
            Queries findQueriesResult = statisticalResoucesRestInternalService.findQueries(query, orderBy, limit, offset, null);

            List<ExternalItemDto> externalItemDtos = buildExternalItemDtosFromResources(findQueriesResult.getQueries(), TypeExternalArtefactsEnum.QUERY);

            ExternalItemsResult result = ExternalItemWebUtils.createExternalItemsResultFromListBase(findQueriesResult, externalItemDtos);
            return result;
        } catch (Exception e) {
            throw manageSrmInternalRestException(serviceContext, e);
        }
    }
    
    @Override
    public DataStructureDto retrieveDataDefinitionFromQuery(ServiceContext serviceContext, String queryUrn) throws MetamacWebException {
        try {
            // TODO METAMAC-2503 con DATOS y METADATOS, si no se necesitan datos, OPTIMIZAR
            String languageDefault = configurationService.retrieveLanguageDefault();
            Query query = statisticalResoucesRestInternalService.retrieveQueryByUrn(queryUrn, Arrays.asList(languageDefault), StatisticalResoucesRestInternalService.QueryFetchEnum.ALL);

            DataStructureDto dataStructureDto = es.gobcan.istac.indicators.web.server.utils.DtoUtils.createDataStructureDto(query);
            return dataStructureDto;
        } catch (Exception e) {
            throw manageSrmInternalRestException(serviceContext, e);
        }
    }
    
    //
    // EXCEPTION HANDLERS
    //

    private MetamacWebException manageSrmInternalRestException(ServiceContext ctx, Exception e) throws MetamacWebException {
        return restExceptionUtils.manageMetamacRestException(ctx, e, CommonServiceExceptionParameters.API_STATISTICAL_OPERATIONS_INTERNAL, restApiLocator.getStatisticalOperationsRestFacadeV10());
    }
    
    private List<ExternalItemDto> buildExternalItemDtosFromResources(List<ResourceInternal> resources, TypeExternalArtefactsEnum type) {
        List<ExternalItemDto> results = new ArrayList<ExternalItemDto>();
        for (ResourceInternal resource : resources) {
            results.add(buildExternalItemDtoFromResource(resource, type));
        }
        return results;
    }
    
    private ExternalItemDto buildExternalItemDtoFromResource(ResourceInternal resource, TypeExternalArtefactsEnum type) {
        ExternalItemDto externalItemDto = new ExternalItemDto();
        externalItemDto.setCode(resource.getId());
        externalItemDto.setCodeNested(resource.getNestedId());
        externalItemDto.setUri(resource.getSelfLink().getHref());
        externalItemDto.setUrn(resource.getUrn());
        externalItemDto.setType(type);
        externalItemDto.setTitle(org.siemac.metamac.web.common.server.utils.DtoUtils.getInternationalStringDtoFromInternationalString(resource.getName()));
        externalItemDto.setManagementAppUrl(resource.getManagementAppLink());
        return externalItemDto;
    }

}

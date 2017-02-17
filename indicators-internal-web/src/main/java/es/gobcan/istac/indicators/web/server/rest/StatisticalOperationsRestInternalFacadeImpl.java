package es.gobcan.istac.indicators.web.server.rest;

import static es.gobcan.istac.indicators.web.server.utils.MetamacWebCriteriaUtils.buildQueryStatisticalOperation;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionParameters;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.OperationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.web.common.server.rest.utils.RestExceptionUtils;
import org.siemac.metamac.web.common.server.utils.DtoUtils;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.service.RestApiLocator;
import es.gobcan.istac.indicators.web.server.utils.ExternalItemWebUtils;

@Component
public class StatisticalOperationsRestInternalFacadeImpl implements StatisticalOperationsRestInternalFacade {

    @Autowired
    private RestApiLocator     restApiLocator;

    @Autowired
    private RestExceptionUtils restExceptionUtils;

    @Override
    public Operation retrieveOperation(ServiceContext serviceContext, String operationCode) throws MetamacWebException {
        try {
            return restApiLocator.getStatisticalOperationsRestFacadeV10().retrieveOperationById(operationCode);
        } catch (Exception e) {
            throw manageSrmInternalRestException(serviceContext, e);
        }
    }

    @Override
    public Operations findOperationsIndicatorsSystem(ServiceContext serviceContext, int firstResult, int maxResult) throws MetamacWebException {
        try {
            String query = OperationCriteriaPropertyRestriction.IS_INDICATORS_SYSTEM + " " + OperationType.EQ.name() + " \"" + Boolean.TRUE + "\"";

            // Pagination
            String limit = String.valueOf(maxResult);
            String offset = String.valueOf(firstResult);

            Operations findOperationsResult = restApiLocator.getStatisticalOperationsRestFacadeV10().findOperations(query, null, limit, offset);
            return findOperationsResult;
        } catch (Exception e) {
            throw manageSrmInternalRestException(serviceContext, e);
        }
    }
    
    @Override
    public ExternalItemsResult findOperations(ServiceContext serviceContext, int firstResult, int maxResult, MetamacWebCriteria criteria) throws MetamacWebException {
        try {
            String query = buildQueryStatisticalOperation(criteria);
            String limit = String.valueOf(maxResult);
            String offset = String.valueOf(firstResult);
            String orderBy = null;
            
            Operations findOperationsResult = restApiLocator.getStatisticalOperationsRestFacadeV10().findOperations(query, orderBy, limit, offset);

            List<ExternalItemDto> externalItemDtos = buildExternalItemDtosFromResources(findOperationsResult.getOperations(), TypeExternalArtefactsEnum.STATISTICAL_OPERATION);

            ExternalItemsResult result = ExternalItemWebUtils.createExternalItemsResultFromListBase(findOperationsResult, externalItemDtos);
            return result;
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
        externalItemDto.setTitle(DtoUtils.getInternationalStringDtoFromInternationalString(resource.getName()));
        externalItemDto.setManagementAppUrl(resource.getManagementAppLink());
        return externalItemDto;
    }

}

package es.gobcan.istac.indicators.rest;

import java.io.OutputStream;

import javax.ws.rs.core.Response.Status;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;

import es.gobcan.istac.indicators.rest.enume.PlainTextTypeEnum;
import es.gobcan.istac.indicators.rest.enume.ResourcesFormat;
import es.gobcan.istac.indicators.rest.exception.ExceptionUtils;
import es.gobcan.istac.indicators.rest.exception.RestIndicatorsCommonServiceExceptionType;

public class ExportResourceAccessToPlainText {

    public void exportResourceAccessToPlainText(ResourceAccess resourceAccess, PlainTextTypeEnum format, OutputStream os) throws MetamacException {
        try {
            PlainTextExporter exporter = new PlainTextExporter(format, resourceAccess);
            exporter.writeObservationsAndAttributesWithObservationAttachmentLevel(os);
        } catch (Exception e) {
            throw ExceptionUtils.manageException(e);
        }
    }

    public void checkMaxRowsInXlsxFormat(ResourceAccess resourceAccess, PlainTextTypeEnum format, String maxXlsxRows, String indicatorCode) throws RestException {
        if (ResourcesFormat.XLSX.name().equals(format.getExtension().toUpperCase()) && (getObservationsNumber(resourceAccess) > Long.parseLong(maxXlsxRows))) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestIndicatorsCommonServiceExceptionType.INDICATOR_OBSERVATIONS_EXCEED_MAX_FOR_XLSX,
                    indicatorCode);
            throw new RestException(exception, Status.NOT_FOUND);
        }
    }

    // return number of observations + 1 (header row)
    public Long getObservationsNumber(ResourceAccess resourceAccess) {
        // Long dimensionRows = Long.valueOf(indicatorSelection.getRows());
        // Long dimensionColumns = Long.valueOf(resourceAccess.getColumns());
        // return dimensionRows * dimensionColumns + 1;
        return 1L;
    }
}

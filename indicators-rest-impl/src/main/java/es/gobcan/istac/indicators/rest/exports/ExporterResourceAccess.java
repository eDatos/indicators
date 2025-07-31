package es.gobcan.istac.indicators.rest.exports;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.rest.dto.IndicatorSelection;
import es.gobcan.istac.indicators.rest.enume.ExportFormatEnum;
import es.gobcan.istac.indicators.rest.enume.ResourcesFormat;
import es.gobcan.istac.indicators.rest.exception.ExceptionUtils;
import es.gobcan.istac.indicators.rest.exception.RestIndicatorsCommonServiceExceptionType;
import es.gobcan.istac.indicators.rest.mapper.IndicatorSelectionMapper;
import es.gobcan.istac.indicators.rest.types.DataType;
import es.gobcan.istac.indicators.rest.util.ExportUtils;

public class ExporterResourceAccess {

    public static void exportResourceAccess(ResourceAccess resourceAccess, ExportFormatEnum format, OutputStream os) throws MetamacException {
        try {
            if (format.isXlsx()) {
                new ExcelExporter(resourceAccess).write(os);
            } else if (format.isPlainText()) {
                new PlainTextExporter(format, resourceAccess).writeObservationsAndAttributesWithObservationAttachmentLevel(os);
            } else {
                throw new IllegalArgumentException("Unsupported export format: " + format);
            }
        } catch (Exception e) {
            throw ExceptionUtils.manageException(e);
        }
    }

    public static void checkMaxRowsInXlsxFormat(ResourceAccess resourceAccess, ExportFormatEnum format, String maxXlsxRows, String indicatorCode) throws RestException {
        if (ResourcesFormat.XLSX.name().equals(format.getExtension().toUpperCase()) && (getObservationsNumber(resourceAccess) > Long.parseLong(maxXlsxRows))) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestIndicatorsCommonServiceExceptionType.INDICATOR_OBSERVATIONS_EXCEED_MAX_FOR_XLSX,
                    indicatorCode);
            throw new RestException(exception, Status.NOT_FOUND);
        }
    }

    // return number of observations + 1 (header row)
    public static long getObservationsNumber(ResourceAccess resourceAccess) {
        return resourceAccess.getRows() + 1;
    }

    public static ResponseEntity<byte[]> exportIndicatorResource(String indicatorCode, ExportFormatEnum format, ConfigurationService configurationService, ResourceAccess resourceAccess) throws MetamacException {
        try {

            byte[] content = exportDataToByteArray(resourceAccess, format, indicatorCode, configurationService.retrieveMaxXlsxRows());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", ExportUtils.getContentDisposition(indicatorCode, format.getExtension()));

            return new ResponseEntity<>(content, headers, HttpStatus.OK);

        } catch (Exception e) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_WRONG_PROC_STATUS, indicatorCode);
        }
    }

    public static byte[] exportDataToByteArray(ResourceAccess resourceAccess, ExportFormatEnum format, String indicatorCode, String maxXlsxRows) throws MetamacException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            exportResourceAccess(resourceAccess, format, byteArrayOutputStream);
            checkMaxRowsInXlsxFormat(resourceAccess, format, maxXlsxRows, indicatorCode);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw ExceptionUtils.manageException(e);
        } finally {
            IOUtils.closeQuietly(byteArrayOutputStream);
        }
    }
}

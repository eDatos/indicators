package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionInformation;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Implementation of IndicatorsService.
 */
@Service("indicatorsService")
public class IndicatorsServiceImpl extends IndicatorsServiceImplBase {

    public IndicatorsServiceImpl() {
    }

    @Override
    public IndicatorVersion createIndicatorVersion(ServiceContext ctx, Indicator indicator, IndicatorVersion indicatorDraft) throws MetamacException {

        // Save indicator
        indicator = getIndicatorRepository().save(indicator);

        // Save draft version
        indicatorDraft.setIndicator(indicator);
        indicatorDraft = getIndicatorVersionRepository().save(indicatorDraft);

        // Update indicator with draft version
        indicator.setProductionVersion(new IndicatorVersionInformation(indicatorDraft.getId(), indicatorDraft.getVersionNumber()));
        indicator.getVersions().add(indicatorDraft);
        getIndicatorRepository().save(indicatorDraft.getIndicator());

        return indicatorDraft;
    }

    @Override
    public Indicator retrieveIndicator(ServiceContext ctx, String uuid) throws MetamacException {
        Indicator indicator = getIndicatorRepository().retrieveIndicator(uuid);
        if (indicator == null) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND, uuid);
        }
        return indicator;
    }

    @Override
    public IndicatorVersion retrieveIndicatorVersion(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {
        IndicatorVersion indicatorVersion = getIndicatorVersionRepository().retrieveIndicatorVersion(uuid, versionNumber);
        if (indicatorVersion == null) {
            if (versionNumber == null) {
                throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND, uuid);
            } else {
                throw new MetamacException(ServiceExceptionType.INDICATOR_VERSION_NOT_FOUND, uuid, versionNumber);
            }
        }
        return indicatorVersion;
    }

    @Override
    public void updateIndicator(ServiceContext ctx, Indicator indicator) throws MetamacException {
        getIndicatorRepository().save(indicator);
    }

    @Override
    public void updateIndicatorVersion(ServiceContext ctx, IndicatorVersion indicatorVersion) throws MetamacException {
        getIndicatorVersionRepository().save(indicatorVersion);
    }

    @Override
    public void deleteIndicator(ServiceContext ctx, String uuid) throws MetamacException {
        Indicator indicator = retrieveIndicator(ctx, uuid);
        getIndicatorRepository().delete(indicator);
    }

    @Override
    public void deleteIndicatorVersion(ServiceContext ctx, String uuid, String versionNumber) throws MetamacException {
        IndicatorVersion indicatorVersion = retrieveIndicatorVersion(ctx, uuid, versionNumber);
        Indicator indicator = indicatorVersion.getIndicator();
        indicator.getVersions().remove(indicatorVersion);

        // Update
        getIndicatorRepository().save(indicator);
        getIndicatorVersionRepository().delete(indicatorVersion);
    }

    @Override
    public List<Indicator> findIndicators(ServiceContext ctx, String code) throws MetamacException {
        return getIndicatorRepository().findIndicators(code);
    }

    // TODO criteria
    @Override
    public List<IndicatorVersion> findIndicatorsVersions(ServiceContext ctx, String uriGopestat, IndicatorStateEnum state) throws MetamacException {
        return getIndicatorVersionRepository().findIndicatorsVersions(state);
    }

    @Override
    public DataSource createDataSource(ServiceContext ctx, DataSource dataSource) throws MetamacException {
        return getDataSourceRepository().save(dataSource);
    }

    @Override
    public DataSource retrieveDataSource(ServiceContext ctx, String uuid) throws MetamacException {
        DataSource dataSource = getDataSourceRepository().findDataSource(uuid);
        if (dataSource == null) {
            throw new MetamacException(ServiceExceptionType.DATA_SOURCE_NOT_FOUND, uuid);
        }
        return dataSource;
    }

    @Override
    public DataSource updateDataSource(ServiceContext ctx, DataSource dataSource) throws MetamacException {
        return getDataSourceRepository().save(dataSource);
    }

    @Override
    public void deleteDataSource(ServiceContext ctx, DataSource dataSource) throws MetamacException {
        getDataSourceRepository().delete(dataSource);
    }

    @Override
    public List<DataSource> findDataSources(ServiceContext ctx, String indicatorUuid, String indicatorVersionNumber) throws MetamacException {
        IndicatorVersion indicatorVersion = retrieveIndicatorVersion(ctx, indicatorUuid, indicatorVersionNumber);
        return indicatorVersion.getDataSources();
    }
}

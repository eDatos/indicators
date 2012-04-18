package es.gobcan.istac.indicators.core.dto;


/**
 * Dto for indicator
 */
public class IndicatorDto extends IndicatorDtoBase {
    private static final long serialVersionUID = 1L;

    public IndicatorDto() {
    }
    
    public boolean needsBePopulated() {
        boolean recentlyCreated = getDataRepositoryId() == null;
        boolean updateHasFailed = getNeedsUpdate();
        boolean modifiedAfterPopulate = getInconsistentData();
        return recentlyCreated || updateHasFailed || modifiedAfterPopulate;
    }
}

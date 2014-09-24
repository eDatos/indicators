package es.gobcan.istac.indicators.web.client.utils;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;

import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaOrderEnum;
import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.dto.TimeGranularityDto;
import es.gobcan.istac.indicators.core.dto.TimeValueDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.QuantityUnitSymbolPositionEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationMethodTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.RateDerivationRoundingEnum;
import es.gobcan.istac.indicators.core.enume.domain.TimeGranularityEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.enums.GeographicalSelectionTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.QuantityIndexBaseTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.TimeSelectionTypeEnum;
import es.gobcan.istac.indicators.web.client.widgets.RateDerivationForm;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

public class CommonUtils {

    public static LinkedHashMap<String, String> getIndicatorsValueMap(List<IndicatorDto> indicatorDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (IndicatorDto indicatorDto : indicatorDtos) {
            valueMap.put(indicatorDto.getUuid(), CommonWebUtils.getElementName(indicatorDto.getCode(), indicatorDto.getTitle()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getTimeGranularityValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (TimeGranularityEnum type : TimeGranularityEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().timeGranularityEnum() + type.getName()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getTimeGranularityValueMap(List<TimeGranularityDto> timeGranularityDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (TimeGranularityDto type : timeGranularityDtos) {
            valueMap.put(type.getGranularity().toString(), getCoreMessages().getString(getCoreMessages().timeGranularityEnum() + type.getGranularity().getName()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getTimeValueValueMap(List<TimeValueDto> timeValues) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (TimeValueDto timeValue : timeValues) {
            valueMap.put(timeValue.getTimeValue(), InternationalStringUtils.getLocalisedString(timeValue.getTitle()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getQuantityTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (QuantityTypeEnum type : QuantityTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + type.getName()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getDataSourceQuantityTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(QuantityTypeEnum.AMOUNT.toString(), getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + QuantityTypeEnum.AMOUNT.getName()));
        valueMap.put(QuantityTypeEnum.CHANGE_RATE.toString(), getCoreMessages().getString(getCoreMessages().quantityTypeEnum() + QuantityTypeEnum.CHANGE_RATE.getName()));
        return valueMap;
    }

    public static LinkedHashMap<String, String> getQuantityIndexBaseMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (QuantityIndexBaseTypeEnum type : QuantityIndexBaseTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().quantityIndexBaseTypeEnum() + type.getName()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getTimeSelectionTypeMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (TimeSelectionTypeEnum type : TimeSelectionTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().timeSelectionTypeEnum() + type.getName()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getGeographicalSelectionTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (GeographicalSelectionTypeEnum type : GeographicalSelectionTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().geographicalSelectionTypeEnum() + type.getName()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getGeographicalGranularituesValueMap(List<GeographicalGranularityDto> geographicalGranularityDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (GeographicalGranularityDto geographicalGranularityDto : geographicalGranularityDtos) {
            valueMap.put(geographicalGranularityDto.getUuid(), CommonWebUtils.getElementName(geographicalGranularityDto.getCode(), geographicalGranularityDto.getTitle()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getGeographicalValuesValueMap(List<GeographicalValueDto> geographicalValueDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (GeographicalValueDto geographicalValueDto : geographicalValueDtos) {
            valueMap.put(geographicalValueDto.getUuid(), CommonWebUtils.getElementName(geographicalValueDto.getCode(), geographicalValueDto.getTitle()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getSubjectsValueMap(List<SubjectDto> subjectDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        sortSubjectList(subjectDtos);
        for (SubjectDto subjectDto : subjectDtos) {
            valueMap.put(subjectDto.getCode(), InternationalStringUtils.getLocalisedString(subjectDto.getTitle()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getQuantityUnitsValueMap(List<QuantityUnitDto> units) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        sortQuantityUnitsList(units);
        for (QuantityUnitDto unit : units) {
            valueMap.put(unit.getUuid(), InternationalStringUtils.getLocalisedString(unit.getTitle()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getQuantityUnitsSymbolPositionValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (QuantityUnitSymbolPositionEnum position : QuantityUnitSymbolPositionEnum.values()) {
            valueMap.put(position.getName(), getCoreMessages().getString(getCoreMessages().quantityUnitSymbolPositionEnum() + position.getName()));
        }
        return valueMap;
    }

    public static InternationalStringDto getSubjectTitleFromCode(List<SubjectDto> subjectDtos, String code) {
        if (code != null) {
            for (SubjectDto subjectDto : subjectDtos) {
                if (code.equals(subjectDto.getCode())) {
                    return subjectDto.getTitle();
                }
            }
        }
        return null;
    }

    public static LinkedHashMap<String, String> getVersionTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (VersionTypeEnum type : VersionTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().versionTypeEnum() + type.getName()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getDataDefinitionValueMap(List<DataDefinitionDto> dataDefinitionDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (DataDefinitionDto dataBasic : dataDefinitionDtos) {
            valueMap.put(dataBasic.getUuid(), dataBasic.getName());
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getRateDerivationMethodTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        valueMap.put(RateDerivationForm.NOT_APPLICABLE, getCoreMessages().rateDerivationMethodTypeEnumNOT_APPLICABLE());
        for (RateDerivationMethodTypeEnum type : RateDerivationMethodTypeEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().rateDerivationMethodTypeEnum() + type.getName()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getRateDerivationRoundingValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (RateDerivationRoundingEnum type : RateDerivationRoundingEnum.values()) {
            valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().rateDerivationRoundingEnum() + type.getName()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getVariableCategoriesValueMap(List<String> codes, List<String> labels) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (int i = 0; i < codes.size(); i++) {
            valueMap.put(codes.get(i), codes.get(i) + " - " + labels.get(i));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getObsValueLValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        valueMap.put(DataSourceDto.OBS_VALUE, IndicatorsWeb.getConstants().dataSourceObsValue());
        return valueMap;
    }

    public static LinkedHashMap<String, String> getUnitMultiplierValueMap(List<UnitMultiplierDto> unitMultiplierDtos) {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (UnitMultiplierDto unitMultiplierDto : unitMultiplierDtos) {
            valueMap.put(unitMultiplierDto.getUnitMultiplier().toString(), InternationalStringUtils.getLocalisedString(unitMultiplierDto.getTitle()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getProcStatusValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(new String(), new String());
        for (IndicatorProcStatusEnum procStatus : IndicatorProcStatusEnum.values()) {
            valueMap.put(procStatus.toString(), getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + procStatus.getName()));
        }
        return valueMap;
    }

    public static LinkedHashMap<String, String> getIndicatorOrderValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        valueMap.put(IndicatorCriteriaOrderEnum.CODE.name(), getConstants().indicListHeaderIdentifier());
        valueMap.put(IndicatorCriteriaOrderEnum.TITLE.name(), getConstants().indicDetailTitle());
        valueMap.put(IndicatorCriteriaOrderEnum.PRODUCTION_PROC_STATUS.name(), getConstants().indicatorProductionEnvironmentProcStatus());
        valueMap.put(IndicatorCriteriaOrderEnum.DIFFUSION_PROC_STATUS.name(), getConstants().indicatorDiffusionEnvironmentProcStatus());
        return valueMap;
    }

    public static IndicatorCriteriaOrderEnum getIndicatorCriteriaOrderEnum(String value) {
        try {
            return IndicatorCriteriaOrderEnum.valueOf(value);
        } catch (Exception e) {
        }
        return null;
    }

    public static LinkedHashMap<String, String> getOrderTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put(OrderTypeEnum.ASC.name(), getConstants().orderASC());
        valueMap.put(OrderTypeEnum.DESC.name(), getConstants().orderDESC());
        return valueMap;
    }

    /**
     * Returns null if UUID parameter is blank
     * 
     * @param uuid
     * @return
     */
    public static String getUuidString(String uuid) {
        return StringUtils.isBlank(uuid) ? null : uuid;
    }

    public static String getIndicatorProcStatusName(IndicatorDto indicatorDto) {
        return getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + indicatorDto.getProcStatus().getName());
    }

    public static String getIndicatorProcStatusName(IndicatorProcStatusEnum indicatorProcStatus) {
        return getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + indicatorProcStatus.getName());
    }

    public static String getIndicatorSystemProcStatusName(IndicatorsSystemDtoWeb indicatorsSystemDtoWeb) {
        return getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + indicatorsSystemDtoWeb.getProcStatus().getName());
    }

    public static String getIndicatorSystemProcStatusName(IndicatorsSystemProcStatusEnum indicatorsSystemProcStatus) {
        return getCoreMessages().getString(getCoreMessages().indicatorProcStatusEnum() + indicatorsSystemProcStatus.getName());
    }

    public static IndicatorProcStatusEnum getIndicatorProcStatusEnum(String value) {
        try {
            return IndicatorProcStatusEnum.valueOf(value);
        } catch (Exception e) {
        }
        return null;
    }

    public static FormItemIfFunction getFalseIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return false;
            }
        };
    }

    public static String getIndicatorText(String indicatorCode, InternationalStringDto indicatorTitle) {
        if (indicatorCode != null) {
            return CommonWebUtils.getElementName(indicatorCode, indicatorTitle);
        }
        return new String();
    }

    public static InternationalStringDto getIndicatorTitle(IndicatorSummaryDto indicatorSummaryDto) {
        if (indicatorSummaryDto.getDiffusionVersion() != null && indicatorSummaryDto.getProductionVersion() == null) {
            return indicatorSummaryDto.getDiffusionVersion().getTitle();
        } else {
            return indicatorSummaryDto.getProductionVersion().getTitle();
        }
    }

    public static String getIndicatorVersionNumber(IndicatorSummaryDto indicatorSummaryDto) {
        if (indicatorSummaryDto.getDiffusionVersion() != null && indicatorSummaryDto.getProductionVersion() == null) {
            return indicatorSummaryDto.getDiffusionVersion().getVersionNumber();
        } else {
            return indicatorSummaryDto.getProductionVersion().getVersionNumber();
        }
    }

    public static Boolean getIndicatorNeedsBePopulated(IndicatorSummaryDto indicatorSummaryDto) {
        if (indicatorSummaryDto.getDiffusionVersion() != null && indicatorSummaryDto.getProductionVersion() == null) {
            return indicatorSummaryDto.getDiffusionVersion().getNeedsBePopulated();
        } else {
            return indicatorSummaryDto.getProductionVersion().getNeedsBePopulated();
        }
    }

    private static void sortSubjectList(List<SubjectDto> subjectDtos) {
        Collections.sort(subjectDtos, new Comparator<SubjectDto>() {

            @Override
            public int compare(SubjectDto first, SubjectDto second) {
                String firstText = InternationalStringUtils.getLocalisedString(first.getTitle());
                String secondText = InternationalStringUtils.getLocalisedString(second.getTitle());
                return firstText.compareTo(secondText);
            }
        });
    }

    private static void sortQuantityUnitsList(List<QuantityUnitDto> units) {
        Collections.sort(units, new Comparator<QuantityUnitDto>() {

            @Override
            public int compare(QuantityUnitDto first, QuantityUnitDto second) {
                String firstText = InternationalStringUtils.getLocalisedString(first.getTitle());
                String secondText = InternationalStringUtils.getLocalisedString(second.getTitle());
                return firstText.compareTo(secondText);
            }
        });
    }

    public static int getFirstResultToReloadAfterDeletion(PaginatedCheckListGrid listGrid) {
        int firstResult = 0;
        if (listGrid.getListGrid().getRecords() != null && listGrid.getListGrid().getRecords().length > 1) {
            firstResult = listGrid.getFirstResult();
        }
        return firstResult;
    }
}

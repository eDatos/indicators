package es.gobcan.istac.indicators.core.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataBasic;
import es.gobcan.istac.indicators.core.domain.DataSource;
import es.gobcan.istac.indicators.core.domain.DataSourceVariable;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.domain.Dimension;
import es.gobcan.istac.indicators.core.domain.ElementLevel;
import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.domain.Quantity;
import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import es.gobcan.istac.indicators.core.domain.RateDerivation;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataBasicDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceVariableDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.RateDerivationDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.SubjectDto;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;

@Component
public class Do2DtoMapperImpl implements Do2DtoMapper {

    @Override
    public IndicatorsSystemDto indicatorsSystemDoToDto(IndicatorsSystemVersion source) {

        IndicatorsSystemDto target = new IndicatorsSystemDto();

        target.setUuid(source.getIndicatorsSystem().getUuid());
        target.setVersionNumber(source.getVersionNumber());
        target.setCode(source.getIndicatorsSystem().getCode());
        target.setTitle(internationalStringToDto(source.getTitle()));
        target.setAcronym(internationalStringToDto(source.getAcronym()));
        target.setUriGopestat(source.getUriGopestat());
        target.setObjetive(internationalStringToDto(source.getObjetive()));
        target.setDescription(internationalStringToDto(source.getDescription()));
        target.setProcStatus(source.getProcStatus());
        target.setProductionVersion(source.getIndicatorsSystem().getProductionVersion() != null ? source.getIndicatorsSystem().getProductionVersion().getVersionNumber() : null);
        target.setDiffusionVersion(source.getIndicatorsSystem().getDiffusionVersion() != null ? source.getIndicatorsSystem().getDiffusionVersion().getVersionNumber() : null);

        target.setProductionValidationDate(dateDoToDto(source.getProductionValidationDate()));
        target.setProductionValidationUser(source.getProductionValidationUser());
        target.setDiffusionValidationDate(dateDoToDto(source.getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getDiffusionValidationUser());
        target.setPublicationDate(dateDoToDto(source.getPublicationDate()));
        target.setPublicationUser(source.getPublicationUser());
        target.setArchiveDate(dateDoToDto(source.getArchiveDate()));
        target.setArchiveUser(source.getArchiveUser());

        target.setCreatedBy(source.getCreatedBy());
        target.setCreatedDate(dateDoToDto(source.getCreatedDate()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setLastUpdated(dateDoToDto(source.getLastUpdated()));

        return target;
    }

    @Override
    public DimensionDto dimensionDoToDto(Dimension source) {
        DimensionDto target = new DimensionDto();
        target.setUuid(source.getUuid());
        target.setParentUuid(source.getElementLevel().getParentUuid());
        target.setTitle(internationalStringToDto(source.getTitle()));
        target.setOrderInLevel(source.getElementLevel().getOrderInLevel());

        target.setCreatedBy(source.getCreatedBy());
        target.setCreatedDate(dateDoToDto(source.getCreatedDate()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setLastUpdated(dateDoToDto(source.getLastUpdated()));

        return target;
    }

    @Override
    public IndicatorInstanceDto indicatorInstanceDoToDto(IndicatorInstance source) {
        IndicatorInstanceDto target = new IndicatorInstanceDto();
        target.setUuid(source.getUuid());
        target.setIndicatorUuid(source.getIndicator().getUuid());
        target.setTitle(internationalStringToDto(source.getTitle()));
        target.setGeographicalGranularityUuid(source.getGeographicalGranularity() != null ? source.getGeographicalGranularity().getUuid() : null);
        target.setGeographicalValueUuid(source.getGeographicalValue() != null ? source.getGeographicalValue().getUuid() : null);
        target.setTimeGranularity(source.getTimeGranularity());
        target.setTimeValue(source.getTimeValue());
        target.setParentUuid(source.getElementLevel().getParentUuid());
        target.setOrderInLevel(source.getElementLevel().getOrderInLevel());

        target.setCreatedBy(source.getCreatedBy());
        target.setCreatedDate(dateDoToDto(source.getCreatedDate()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setLastUpdated(dateDoToDto(source.getLastUpdated()));

        return target;
    }

    @Override
    public IndicatorDto indicatorDoToDto(IndicatorVersion source) {

        IndicatorDto target = new IndicatorDto();

        target.setUuid(source.getIndicator().getUuid());
        target.setVersionNumber(source.getVersionNumber());
        target.setCode(source.getIndicator().getCode());
        target.setTitle(internationalStringToDto(source.getTitle()));
        target.setAcronym(internationalStringToDto(source.getAcronym()));
        target.setSubjectCode(source.getSubjectCode());
        target.setSubjectTitle(internationalStringToDto(source.getSubjectTitle()));
        target.setQuantity(quantityDoToDto(source.getQuantity()));
        target.setConceptDescription(internationalStringToDto(source.getConceptDescription()));
        target.setComments(internationalStringToDto(source.getComments()));
        target.setCommentsUrl(source.getCommentsUrl());
        target.setNotes(internationalStringToDto(source.getNotes()));
        target.setNotesUrl(source.getNotesUrl());
        target.setProcStatus(source.getProcStatus());
        target.setProductionVersion(source.getIndicator().getProductionVersion() != null ? source.getIndicator().getProductionVersion().getVersionNumber() : null);
        target.setDiffusionVersion(source.getIndicator().getDiffusionVersion() != null ? source.getIndicator().getDiffusionVersion().getVersionNumber() : null);

        target.setProductionValidationDate(dateDoToDto(source.getProductionValidationDate()));
        target.setProductionValidationUser(source.getProductionValidationUser());
        target.setDiffusionValidationDate(dateDoToDto(source.getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getDiffusionValidationUser());
        target.setPublicationDate(dateDoToDto(source.getPublicationDate()));
        target.setPublicationUser(source.getPublicationUser());
        target.setArchiveDate(dateDoToDto(source.getArchiveDate()));
        target.setArchiveUser(source.getArchiveUser());

        target.setCreatedBy(source.getCreatedBy());
        target.setCreatedDate(dateDoToDto(source.getCreatedDate()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setLastUpdated(dateDoToDto(source.getLastUpdated()));

        return target;
    }

    @Override
    public DataSourceDto dataSourceDoToDto(DataSource source) {
        DataSourceDto target = new DataSourceDto();
        target.setUuid(source.getUuid());
        target.setQueryGpe(source.getQueryGpe());
        target.setPx(source.getPx());
        target.setTimeVariable(source.getTimeVariable());
        target.setTimeValue(source.getTimeValue());
        target.setGeographicalVariable(source.getGeographicalVariable());
        target.setGeographicalValueUuid(source.getGeographicalValue() != null ? source.getGeographicalValue().getUuid() : null);
        target.getOtherVariables().addAll(dataSourceVariableDoToDto(source.getOtherVariables()));
        target.setInterperiodRate(rateDerivationDoToDto(source.getInterperiodRate()));
        target.setAnnualRate(rateDerivationDoToDto(source.getAnnualRate()));

        target.setCreatedBy(source.getCreatedBy());
        target.setCreatedDate(dateDoToDto(source.getCreatedDate()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setLastUpdated(dateDoToDto(source.getLastUpdated()));

        return target;
    }

    @Override
    public List<ElementLevelDto> elementsLevelsDoToDto(List<ElementLevel> sources) {
        List<ElementLevelDto> targets = new ArrayList<ElementLevelDto>();
        for (ElementLevel source : sources) {
            ElementLevelDto target = elementLevelDoToDto(source);
            targets.add(target);
        }
        return targets;
    }

    @Override
    public QuantityUnitDto quantityUnitDoToDto(QuantityUnit source) {

        QuantityUnitDto target = new QuantityUnitDto();
        target.setUuid(source.getUuid());
        target.setSymbol(source.getSymbol());
        target.setSymbolPosition(source.getSymbolPosition());
        target.setTitle(internationalStringToDto(source.getTitle()));

        return target;
    }

    @Override
    public SubjectDto subjectDoToDto(Subject source) {

        SubjectDto target = new SubjectDto();
        target.setCode(source.getId());

        InternationalStringDto title = new InternationalStringDto();
        LocalisedStringDto localisedStringDto = new LocalisedStringDto();
        localisedStringDto.setLabel(source.getTitle());
        localisedStringDto.setLocale(IndicatorsConstants.LOCALE_SPANISH); // title only in spanish
        title.addText(localisedStringDto);
        target.setTitle(title);

        return target;
    }
    
    @Override
    public SubjectDto subjectDoToDto(SubjectIndicatorResult source) {

        SubjectDto target = new SubjectDto();
        target.setCode(source.getId());
        target.setTitle(internationalStringToDto(source.getTitle()));

        return target;
    }

    @Override
    public GeographicalValueDto geographicalValueDoToDto(GeographicalValue source) {

        GeographicalValueDto target = new GeographicalValueDto();
        target.setUuid(source.getUuid());
        target.setCode(source.getCode());
        target.setTitle(internationalStringToDto(source.getTitle()));
        target.setGranularityUuid(source.getGranularity().getUuid());
        target.setLatitude(source.getLatitude());
        target.setLongitude(source.getLongitude());

        return target;
    }

    @Override
    public GeographicalGranularityDto geographicalGranularityDoToDto(GeographicalGranularity source) {

        GeographicalGranularityDto target = new GeographicalGranularityDto();
        target.setUuid(source.getUuid());
        target.setCode(source.getCode());
        target.setTitle(internationalStringToDto(source.getTitle()));

        return target;
    }
    
    @Override
    public DataBasicDto dataBasicDoToDto(DataBasic source) {
        DataBasicDto target = new DataBasicDto();
        target.setUuid(source.getUuid());
        target.setTitle(source.getTitle());
        return target;
    }
    
    @Override
    public DataStructureDto dataStructureDoToDto(DataStructure source) {
        DataStructureDto target = new DataStructureDto();
        target.setUuid(source.getUuid());
        target.setTitle(source.getTitle());
        target.setUriPx(source.getUriPx());
        if (source.getCategoryCodes() != null) {
            target.setVariables(new ArrayList<String>(source.getCategoryCodes().keySet()));
            target.setValueCodes(source.getCategoryCodes());
        }
        if (source.getCategoryLabels() != null) {
            target.setValueLabels(source.getCategoryLabels());
        }
        if (source.getTemporals() != null && source.getTemporals().size() > 0) {
            target.setTemporalVariable(source.getTemporals().get(0));
        }
        if (source.getSpatials() != null && source.getSpatials().size() > 0) {
            target.setSpatialVariable(source.getSpatials().get(0));
        }
        return target;
    }
    
    @Override
    public DataDto dataDoToDto(Data source) {
        DataDto target = new DataDto();
        return target;
    }

    private ElementLevelDto elementLevelDoToDto(ElementLevel source) {
        ElementLevelDto target = new ElementLevelDto();
        if (source.getDimension() != null) {
            target.setDimension(dimensionDoToDto(source.getDimension()));
        } else if (source.getIndicatorInstance() != null) {
            target.setIndicatorInstance(indicatorInstanceDoToDto(source.getIndicatorInstance()));
        }
        for (ElementLevel child : source.getChildren()) {
            target.addSubelement(elementLevelDoToDto(child));
        }
        return target;
    }

    private List<DataSourceVariableDto> dataSourceVariableDoToDto(List<DataSourceVariable> sources) {
        List<DataSourceVariableDto> targets = new ArrayList<DataSourceVariableDto>();
        for (DataSourceVariable source : sources) {
            DataSourceVariableDto target = dataSourceVariableDoToDto(source);
            targets.add(target);
        }
        return targets;
    }

    private DataSourceVariableDto dataSourceVariableDoToDto(DataSourceVariable source) {
        DataSourceVariableDto target = new DataSourceVariableDto();
        target.setVariable(source.getVariable());
        target.setCategory(source.getCategory());
        return target;
    }

    private InternationalStringDto internationalStringToDto(InternationalString source) {
        if (source == null) {
            return null;
        }
        InternationalStringDto target = new InternationalStringDto();
        target.getTexts().addAll(localisedStringDoToDto(source.getTexts()));
        return target;
    }

    private Set<LocalisedStringDto> localisedStringDoToDto(Set<LocalisedString> sources) {
        Set<LocalisedStringDto> targets = new HashSet<LocalisedStringDto>();
        for (LocalisedString source : sources) {
            LocalisedStringDto target = new LocalisedStringDto();
            target.setLabel(source.getLabel());
            target.setLocale(source.getLocale());
            targets.add(target);
        }
        return targets;
    }

    private Date dateDoToDto(DateTime source) {
        if (source == null) {
            return null;
        }
        return source.toDate();
    }

    // Note: transforms all metadata regardless of the type
    // InvocationValidation checks metadata unexpected for each type
    private QuantityDto quantityDoToDto(Quantity source) {
        if (source == null) {
            return null;
        }

        QuantityDto target = new QuantityDto();
        target.setType(source.getQuantityType());
        target.setUnitUuid(source.getUnit() != null ? source.getUnit().getUuid() : null);
        target.setUnitMultiplier(source.getUnitMultiplier());
        target.setSignificantDigits(source.getSignificantDigits());
        target.setDecimalPlaces(source.getDecimalPlaces());
        target.setMinimum(source.getMinimum());
        target.setMaximum(source.getMaximum());
        target.setNumeratorIndicatorUuid(source.getNumerator() != null ? source.getNumerator().getUuid() : null);
        target.setDenominatorIndicatorUuid(source.getDenominator() != null ? source.getDenominator().getUuid() : null);
        target.setIsPercentage(source.getIsPercentage());
        target.setPercentageOf(internationalStringToDto(source.getPercentageOf()));
        target.setBaseValue(source.getBaseValue());
        target.setBaseTime(source.getBaseTime());
        target.setBaseLocationUuid(source.getBaseLocation() != null ? source.getBaseLocation().getUuid() : null);
        target.setBaseQuantityIndicatorUuid(source.getBaseQuantity() != null ? source.getBaseQuantity().getUuid() : null);

        return target;
    }
    private RateDerivationDto rateDerivationDoToDto(RateDerivation source) {
        if (source == null) {
            return null;
        }
        RateDerivationDto target = new RateDerivationDto();
        target.setMethodType(source.getMethodType());
        target.setMethod(source.getMethod());
        target.setQuantity(quantityDoToDto(source.getQuantity()));
        target.setRounding(source.getRounding());

        return target;
    }
}

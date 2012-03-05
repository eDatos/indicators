package es.gobcan.istac.indicators.web.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class IndDatabase {

    private static Map<String, IndicatorsSystemDto>          indicatorsSystems;
    private static Map<String, ElementLevelDto>              dimensions;
    private static Map<String, ElementLevelDto>              indicatorInstances;
    private static Map<String, IndicatorsSystemStructureDto> systemsStructures;        //
    private static Map<String, IndicatorDto>                 indicators;

    static {
        if (indicators == null) {
            indicators = new HashMap<String, IndicatorDto>();

            for (int i = 0; i < 6; i++) {
                IndicatorDto indicAux = createBaseIndicator();
                indicators.put(indicAux.getCode(), indicAux);
            }
        }
        if (indicatorsSystems == null) {
            indicatorsSystems = new HashMap<String, IndicatorsSystemDto>();

            // Minimo 2
            for (int i = 0; i < 6; i++) {
                IndicatorsSystemDto indSyst = createBaseIndicatorsSystem();
                if (i > 1) {
                    // Simulate systems from web service, is not persisted so
                    // we don't have uuid, and version either.
                    indSyst.setVersionNumber(null);
                    indSyst.setUuid(null);
                }
                indicatorsSystems.put(indSyst.getCode(), indSyst);
            }
        }
        //
      
        if (systemsStructures == null) {
            systemsStructures = new HashMap<String, IndicatorsSystemStructureDto>();
            dimensions = new HashMap<String, ElementLevelDto>();
            indicatorInstances = new HashMap<String, ElementLevelDto>();
            
            IndicatorDto indicatorTest1 = indicators.values().iterator().next();
            Iterator<IndicatorsSystemDto> it = indicatorsSystems.values().iterator();
            IndicatorsSystemDto system1 = it.next();
            IndicatorsSystemDto system2 = it.next();
            
            IndicatorsSystemStructureDto structure1 = new IndicatorsSystemStructureDto();
            structure1.setUuid(system1.getUuid());
            structure1.setVersionNumber("1.0");
            
            DimensionDto dim1 = createBaseDimension("según sexo", "based on sex");
            dim1.setOrderInLevel(1L);
            ElementLevelDto level1 = new ElementLevelDto();
            level1.setDimension(dim1);
            dimensions.put(dim1.getUuid(), level1);

            DimensionDto dim2 = createBaseDimension("según edad", "based on age");
            dim2.setOrderInLevel(2L);
            ElementLevelDto level2 = new ElementLevelDto();
            level2.setDimension(dim2);
            dimensions.put(dim2.getUuid(), level2);

            DimensionDto dim3 = createBaseDimension("según nacionalidad", "based on nationality");
            dim3.setOrderInLevel(3L);
            ElementLevelDto level3 = new ElementLevelDto();
            level3.setDimension(dim3);
            dimensions.put(dim3.getUuid(), level3);

            
            IndicatorInstanceDto indInst1 = createBaseIndicatorInstance("Hombres", "Men");
            indInst1.setOrderInLevel(1L);
            indInst1.setIndicatorUuid(indicatorTest1.getUuid());
            indInst1.setParentUuid(dim1.getUuid());
            ElementLevelDto level1_1 = new ElementLevelDto();
            level1_1.setIndicatorInstance(indInst1);
            level1.addSubelement(level1_1);
            indicatorInstances.put(indInst1.getUuid(), level1_1);

            IndicatorInstanceDto indInst2 = createBaseIndicatorInstance("Mujeres", "Women");
            indInst2.setOrderInLevel(2L);
            indInst2.setIndicatorUuid(indicatorTest1.getUuid());
            indInst2.setParentUuid(dim1.getUuid());
            ElementLevelDto level1_2 = new ElementLevelDto();
            level1_2.setIndicatorInstance(indInst2);
            level1.addSubelement(level1_2);
            indicatorInstances.put(indInst2.getUuid(), level1_2);
            
            structure1.addElement(level1);
            structure1.addElement(level2);
            structure1.addElement(level3);
            
            systemsStructures.put(structure1.getUuid(), structure1);
            
            //second structure
            IndicatorsSystemStructureDto structure2= new IndicatorsSystemStructureDto();
            structure2.setUuid(system2.getUuid());
            structure2.setVersionNumber("1.0");
            
            indInst1 = createBaseIndicatorInstance("Hombres", "Men");
            indInst1.setOrderInLevel(1L);
            indInst1.setIndicatorUuid(indicatorTest1.getUuid());
            level1 = new ElementLevelDto();
            level1.setIndicatorInstance(indInst1);
            indicatorInstances.put(indInst1.getUuid(), level1);

            indInst2 = createBaseIndicatorInstance("Mujeres", "Women");
            indInst2.setOrderInLevel(2L);
            indInst2.setIndicatorUuid(indicatorTest1.getUuid());
            level2 = new ElementLevelDto();
            level2.setIndicatorInstance(indInst2);
            indicatorInstances.put(indInst2.getUuid(), level2);
            
            structure2.addElement(level1);
            structure2.addElement(level2);
            systemsStructures.put(structure2.getUuid(), structure2);
        }
    }

    // Indicator System

    public static List<IndicatorsSystemDto> getIndicatorsSystems() throws MetamacException {
        return new ArrayList<IndicatorsSystemDto>(indicatorsSystems.values());
    }

    public static IndicatorsSystemDto getIndicatorsSystemByCode(String code) throws MetamacException {
        IndicatorsSystemDto system = indicatorsSystems.get(code);
        if (system == null) {
            throw new MetamacException(ServiceExceptionType.INDICATORS_SYSTEM_NOT_FOUND,code);
        }
        return system;
    }

    public static IndicatorsSystemStructureDto getIndicatorsSystemStructureDto(String uuid) throws MetamacException {
        IndicatorsSystemStructureDto struct = systemsStructures.get(uuid);
        return struct;
    }

    public static void saveIndicatorsSystem(IndicatorsSystemDto system) throws MetamacException {
        if (system == null || StringUtils.isEmpty(system.getCode())) {
            throw new IllegalArgumentException();
        }

        if (system.getUuid() == null) {
            if (indicatorsSystems.get(system.getCode()) != null && !StringUtils.isEmpty(indicatorsSystems.get(system.getCode()).getUuid())) {
                throw new IllegalArgumentException("Ya existe un sistema con este código");
            }
            system.setUuid(getUuid());
            system.setVersionNumber("1.0");
            system.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
            indicatorsSystems.put(system.getCode(), system);
        } else {
            Double newVersion = Double.parseDouble(system.getVersionNumber()) + 1;
            system.setVersionNumber(newVersion.toString());
            indicatorsSystems.put(system.getCode(), system);
        }
    }

    public static void deleteIndicatorsSystem(String code) throws MetamacException {
        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException();
        }

        indicatorsSystems.remove(code);
    }

    // Indicators
    public static List<IndicatorDto> getIndicators() throws MetamacException {
        return new ArrayList<IndicatorDto>(indicators.values());
    }

    public static IndicatorDto getIndicatorByCode(String code) throws MetamacException {
        return indicators.get(code);
    }

    public static IndicatorDto getIndicatorByUuid(String uuid) throws MetamacException {
        for (IndicatorDto ind : indicators.values()) {
            if (ind.getUuid().equals(uuid)) {
                return ind;
            }
        }
        return null;
    }

    public static IndicatorDto saveIndicator(IndicatorDto ind) throws MetamacException {
        if (ind == null || StringUtils.isEmpty(ind.getCode())) {
            throw new IllegalArgumentException();
        }
        if (ind.getUuid() == null) {
            if (indicators.get(ind.getCode()) != null) {
                throw new IllegalArgumentException("Ya existe un indicador con ese código");
            }
            ind.setUuid(getUuid());
            ind.setVersionNumber("1.0");
            ind.setProcStatus(IndicatorProcStatusEnum.DRAFT);
            indicators.put(ind.getCode(), ind);
        } else {
            Double newVersion = Double.parseDouble(ind.getVersionNumber()) + 1;
            ind.setVersionNumber(newVersion.toString());
            indicators.put(ind.getCode(), ind);
        }
        return ind;
    }

    public static void deleteIndicator(String uuid) throws MetamacException {
        if (StringUtils.isEmpty(uuid)) {
            throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND);
        }
        String codeFound  = null;
        for (Entry<String,IndicatorDto> entry: indicators.entrySet()) {
            if (entry.getValue().getUuid().equals(uuid)) {
                codeFound = entry.getValue().getCode();
            }
        }
        if (codeFound != null) {
            indicators.remove(codeFound);
        }
        
    }

    /* Dimensions */
    public static DimensionDto createDimension(String systemUuid, DimensionDto dimension) throws MetamacException {
        // Nos saltamos comprobacion de que la dimension está en el sistema
        if (systemsStructures.get(systemUuid) == null) {
            IndicatorsSystemStructureDto struc = new IndicatorsSystemStructureDto();
            struc.setUuid(systemUuid);
            struc.setVersionNumber("1.0");
            systemsStructures.put(systemUuid,struc);
        }
        dimension.setUuid(getUuid());
        ElementLevelDto level = new ElementLevelDto();
        level.setDimension(dimension);
        dimensions.put(dimension.getUuid(), level);

        // Hacemos hueco
        List<ElementLevelDto> siblings = dimension.getParentUuid() == null ? systemsStructures.get(systemUuid).getElements() : dimensions.get(dimension.getParentUuid()).getSubelements();
        makeGap(siblings, dimension.getOrderInLevel());

        if (dimension.getParentUuid() == null) {
            systemsStructures.get(systemUuid).addElement(level);
        } else {
            dimensions.get(dimension.getParentUuid()).addSubelement(level);
        }
        return dimension;
    }

    public static void saveDimension(DimensionDto dimension) throws MetamacException {
        if (dimensions.get(dimension.getUuid()) == null) {
            new IllegalArgumentException();
        }
        dimensions.get(dimension.getUuid()).setDimension(dimension);
    }

    public static void moveDimension(String uuid, String systemUuid, String newParentUuid, Long newOrderInLevel) throws MetamacException {
        // Nos saltamos comprobacion de que la dimension está en el sistema

        ElementLevelDto dimLevel = dimensions.get(uuid); 
        DimensionDto dim = dimLevel.getDimension();
        String oldParentUuid = dim.getParentUuid();
        dim.setParentUuid(newParentUuid);
        dim.setOrderInLevel(newOrderInLevel);

        // Borramos de donde estaba
        if (oldParentUuid == null) {
            systemsStructures.get(systemUuid).removeElement(dimLevel);
        } else {
            dimensions.get(oldParentUuid).removeSubelement(dimLevel);
        }

        List<ElementLevelDto> siblings = dim.getParentUuid() == null ? systemsStructures.get(systemUuid).getElements() : dimensions.get(dim.getParentUuid()).getSubelements();
        makeGap(siblings, newOrderInLevel);

        // Insertamos en destino
        if (newParentUuid == null) {
            systemsStructures.get(systemUuid).addElement(dimLevel);
        } else {
            dimensions.get(newParentUuid).addSubelement(dimLevel);
        }
    }

    public static void deleteDimension(String uuid) throws MetamacException {
        ElementLevelDto dimLevel = dimensions.get(uuid); 
        
        // Delete content
        List<ElementLevelDto> subElements = new ArrayList<ElementLevelDto>(dimLevel.getSubelements());
        for (ElementLevelDto subElem : subElements) {
            if (subElem.isDimension()) {
                deleteDimension(subElem.getDimension().getUuid());
            } else if (subElem.isIndicatorInstance()) {
                deleteIndicatorInstance(subElem.getIndicatorInstance().getUuid());
            }
        }
        
        if (dimLevel.getParentUuid() == null) {
            //no sabemos a que sistema pertenece , lo borramos de todos aunque sabemos que solo puede ser uno
            for (Entry<String, IndicatorsSystemStructureDto> systemEntry : systemsStructures.entrySet()) {
                systemEntry.getValue().removeElement(dimLevel);
            }
        } else {
            dimensions.get(dimLevel.getParentUuid()).removeSubelement(dimLevel);
        }
        dimensions.remove(uuid);
    }

    /* Indicator instances */
    public static IndicatorInstanceDto createIndicatorInstance(String systemUuid, IndicatorInstanceDto instance) throws MetamacException {

        if (systemsStructures.get(systemUuid) == null) {
            IndicatorsSystemStructureDto struc = new IndicatorsSystemStructureDto();
            struc.setUuid(systemUuid);
            struc.setVersionNumber("1.0");
            systemsStructures.put(systemUuid,struc);
        }
        
        instance.setUuid(getUuid());
        ElementLevelDto level = new ElementLevelDto();
        level.setIndicatorInstance(instance);
        indicatorInstances.put(instance.getUuid(), level);

        // Hacemos hueco
        List<ElementLevelDto> siblings = level.getParentUuid() == null ? systemsStructures.get(systemUuid).getElements() : dimensions.get(level.getParentUuid()).getSubelements();
        makeGap(siblings, level.getOrderInLevel());
        
        if (level.getParentUuid() == null) {
            systemsStructures.get(systemUuid).addElement(level);
        } else {
            dimensions.get(level.getParentUuid()).addSubelement(level);
        }
        return instance;
    }

    public static void saveIndicatorInstance(IndicatorInstanceDto instance) throws MetamacException {
        if (indicatorInstances.get(instance.getUuid()) == null) {
            new IllegalArgumentException();
        }
        indicatorInstances.get(instance.getUuid()).setIndicatorInstance(instance);
    }

    
    public static void moveIndicatorInstance(String uuid, String systemUuid, String newParentUuid, Long newOrderInLevel) throws MetamacException {
        // Nos saltamos comprobacion de que la dimension está en el sistema

        ElementLevelDto level = indicatorInstances.get(uuid); 
        String oldParentUuid = level.getParentUuid();
        
        IndicatorInstanceDto dim = level.getIndicatorInstance();
        dim.setParentUuid(newParentUuid);
        dim.setOrderInLevel(newOrderInLevel);

        // Borramos de donde estaba
        if (oldParentUuid == null) {
            systemsStructures.get(systemUuid).removeElement(level);
        } else {
            dimensions.get(oldParentUuid).removeSubelement(level);
        }

        List<ElementLevelDto> siblings = level.getParentUuid() == null ? systemsStructures.get(systemUuid).getElements() : dimensions.get(level.getParentUuid()).getSubelements();
        makeGap(siblings, newOrderInLevel);

        // Insertamos en destino
        if (newParentUuid == null) {
            systemsStructures.get(systemUuid).addElement(level);
        } else {
            dimensions.get(newParentUuid).addSubelement(level);
        }
    }

    public static void deleteIndicatorInstance(String uuid) throws MetamacException {
        ElementLevelDto level = indicatorInstances.get(uuid); 
        
        if (level.getParentUuid() == null) {
            //no sabemos a que sistema pertenece , lo borramos de todos aunque sabemos que solo puede ser uno
            for (Entry<String, IndicatorsSystemStructureDto> systemEntry : systemsStructures.entrySet()) {
                systemEntry.getValue().removeElement(level);
            }
        } else {
            dimensions.get(level.getParentUuid()).removeSubelement(level);
        }
        indicatorInstances.remove(uuid);
    }

    private static void makeGap(List<ElementLevelDto> children, Long id) {
        for (ElementLevelDto level: children) {
            Long order = level.getOrderInLevel();
            
            if (order >= id) {
                if (level.isDimension()) {
                    level.getDimension().setOrderInLevel(order+1);
                } else if (level.isIndicatorInstance()) {
                    level.getIndicatorInstance().setOrderInLevel(order+1);
                }
                    
            }
        }
    }

    /* UTILS */

    private static IndicatorsSystemDto createBaseIndicatorsSystem() {
        IndicatorsSystemDto systemDto = new IndicatorsSystemDto();
        int code = getInt();
        systemDto.setUuid(getUuid());
        systemDto.setVersionNumber("1.0");
        systemDto.setCode("SYS" + code);
        systemDto.setTitle(createIntString("Sistema " + code, "System " + code));
        systemDto.setProcStatus(IndicatorsSystemProcStatusEnum.DRAFT);
        return systemDto;
    }

    private static IndicatorDto createBaseIndicator() {
        IndicatorDto indicAux = new IndicatorDto();
        int code = getInt();
        indicAux.setUuid(getUuid());
        indicAux.setCode("IND" + code);
        indicAux.setName(createIntString("Indicador " + code, "Indicator " + code));
        indicAux.setVersionNumber("1.0");
        indicAux.setProcStatus(IndicatorProcStatusEnum.DRAFT);
        return indicAux;
    }

    private static DimensionDto createBaseDimension(String textEs, String textEn) {
        DimensionDto dim = new DimensionDto();
        dim.setUuid(getUuid());
        dim.setTitle(createIntString(textEs, textEn));
        dim.setOrderInLevel(1L);
        dim.setParentUuid(null);
        return dim;
    }

    private static IndicatorInstanceDto createBaseIndicatorInstance(String textEs, String textEn) {
        IndicatorInstanceDto instance = new IndicatorInstanceDto();
        instance.setUuid(getUuid());
        instance.setTitle(createIntString(textEs, textEn));
        instance.setOrderInLevel(1L);
        instance.setParentUuid(null);
        instance.setIndicatorUuid(null);
        return instance;
    }

    private static InternationalStringDto createIntString(String textEs, String textEn) {
        InternationalStringDto intStr = new InternationalStringDto();
        LocalisedStringDto localisedString = new LocalisedStringDto();
        localisedString.setUuid(getUuid());
        localisedString.setLocale("es");
        localisedString.setLabel(textEs);
        intStr.addText(localisedString);

        localisedString = new LocalisedStringDto();
        localisedString.setUuid(getUuid());
        localisedString.setLocale("en");
        localisedString.setLabel(textEn);
        intStr.addText(localisedString);
        return intStr;
    }

    private static String getUuid() {
        return UUID.randomUUID().toString();
    }

    private static int getInt() {
        return new Random().nextInt(100000);
    }
}

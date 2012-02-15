package es.gobcan.istac.indicators.web.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.web.shared.db.AnalysisDimension;
import es.gobcan.istac.indicators.web.shared.db.IndicatorInstance;
import es.gobcan.istac.indicators.web.shared.db.IndicatorSystemContent;

public class IndDatabase {

	private static List<IndicatorsSystemDto> indicatorsSystems;
	private static Map<Long,List<IndicatorSystemContent>> indSystemsStructure;
	private static List<IndicatorDto> indicators;
	
	
	static {
		if (indicators == null) {
			indicators = new ArrayList<IndicatorDto>();
			
			for (int i = 0; i < 6; i++) {
				IndicatorDto indicAux = createBaseIndicator();
				indicators.add(indicAux);
			}
		}
		if (indicatorsSystems == null) {
			indicatorsSystems = new ArrayList<IndicatorsSystemDto>();
			
			for (int i = 0; i < 6; i++) {
				IndicatorsSystemDto indSyst = createBaseIndicatorsSystem();
				indicatorsSystems.add(indSyst);
			}
		}			
		//Structures
		if (indSystemsStructure == null) {
			indSystemsStructure = new HashMap<Long, List<IndicatorSystemContent>>();
			//ID0
			List<IndicatorSystemContent> indSysContent = new ArrayList<IndicatorSystemContent>();
			indSystemsStructure.put(0L, indSysContent);
			IndicatorInstance indInst1 = new IndicatorInstance(0L,"Hombres",indicators.get(0));
			IndicatorInstance indInst2 = new IndicatorInstance(1L,"Mujeres",indicators.get(1));
			
			AnalysisDimension dim = new AnalysisDimension(0L,"según sexo");
			dim.addElement(indInst1);
			dim.addElement(indInst2);
			indSysContent.add(dim);
			
			
			indSysContent = new ArrayList<IndicatorSystemContent>();
			indInst1 = new IndicatorInstance(0L,"Hombres",indicators.get(0));
			indInst2 = new IndicatorInstance(1L,"Mujeres",indicators.get(1));
			
			indSysContent.add(indInst1);
			indSysContent.add(indInst2);
			indSystemsStructure.put(1L, indSysContent);
		}
	}
	
	//Indicator System
	
	public static List<IndicatorsSystemDto> getIndicatorsSystems() {
		return indicatorsSystems;
	}
	
	public static IndicatorsSystemDto getIndicatorsSystemByCode(String code) {
		for (IndicatorsSystemDto indSys : indicatorsSystems) {
			if (indSys.getCode().equals(code)) {
				return indSys;
			}
		}
		return null;
	}
	
	public static List<IndicatorSystemContent> getIndicatorsSystemStructure(Long indSysId) {
		return indSystemsStructure.get(indSysId);
	}
	
	public static void saveIndicatorsSystem(IndicatorsSystemDto system) {
		if (system == null || StringUtils.isEmpty(system.getCode())) {
			throw new IllegalArgumentException();
		}
		
		if (system.getUuid() == null) {
			system.setUuid(getUuid());
			system.setVersionNumber("1.0");
			system.setState(IndicatorsSystemStateEnum.DRAFT);
			indicatorsSystems.add(system);
		} else {
		
			for (int i = 0; i < indicatorsSystems.size(); i++) {
				IndicatorsSystemDto systemDB = indicatorsSystems.get(i); 
				if (systemDB.getCode().equals(system.getCode())) {
					Double newVersion = Double.parseDouble(system.getVersionNumber())+1;
	                system.setVersionNumber(newVersion.toString());
					indicatorsSystems.set(i, system);
				}	
			}
		}
	}
	
	//Indicators
	public static List<IndicatorDto> getIndicators() {
		return indicators;
	}
	
	public static IndicatorDto getIndicatorByCode(String code) {
		for (IndicatorDto ind: indicators) {
			if (ind.getCode().equals(code)) {
				return ind;
			}
		}
		return null;
	}
	
	public static IndicatorDto getIndicatorByUuid(String uuid) {
		for (IndicatorDto ind: indicators) {
			if (ind.getUuid().equals(uuid)) {
				return ind;
			}
		}
		return null;
	}
	
	public static void saveIndicator(IndicatorDto ind) {
	    if (ind == null || StringUtils.isEmpty(ind.getCode())) {
	        throw new IllegalArgumentException();
	    }
	    if (ind.getUuid() == null) {
	        ind.setUuid(getUuid());
	        ind.setVersionNumber("1.0");
	        ind.setState(IndicatorStateEnum.DRAFT);
	        indicators.add(ind);
	    } else {
	        for (int i = 0; i < indicators.size(); i++) {
	            IndicatorDto indDB = indicators.get(i);
	            if (indDB.getUuid().equals(ind.getUuid())) {
	            	Double newVersion = Double.parseDouble(ind.getVersionNumber())+1;
	                ind.setVersionNumber(newVersion.toString());
	                indicators.set(i, ind);
	            }
	        }
	    }
	}
	
	public static void deleteIndicator(String code) {
		if (StringUtils.isEmpty(code)) {
			throw new IllegalArgumentException();
		}
		Iterator<IndicatorDto> it = indicators.iterator();
		while (it.hasNext()) {
			IndicatorDto indicator = it.next();
			if (indicator.getCode().equals(code)) {
				it.remove();
			}
		}
	}
	
	public static void deleteIndicatorsSystem(String code) {
		if (StringUtils.isEmpty(code)) {
			throw new IllegalArgumentException();
		}
		Iterator<IndicatorsSystemDto> it = indicatorsSystems.iterator();
		while (it.hasNext()) {
			IndicatorsSystemDto system = it.next();
			if (system.getCode().equals(code)) {
				it.remove();
			}
		}
	}
	
	
	/* UTILS */
	
	private static IndicatorsSystemDto createBaseIndicatorsSystem() {
		IndicatorsSystemDto systemDto = new IndicatorsSystemDto();
		int code = getInt();
		systemDto.setUuid(getUuid());
		systemDto.setVersionNumber("1.0");
		systemDto.setCode("SYS"+code);
		systemDto.setTitle(createIntString("Sistema "+code, "System "+code));
		systemDto.setState(IndicatorsSystemStateEnum.DRAFT);
		return systemDto;
	}
	
	private static IndicatorDto createBaseIndicator() {
		IndicatorDto indicAux = new IndicatorDto();
		int code = getInt();
        indicAux.setUuid(getUuid());
        indicAux.setCode("IND"+code);
		indicAux.setName(createIntString("Indicador "+code, "Indicator "+code));
		indicAux.setVersionNumber("1.0");
		indicAux.setState(IndicatorStateEnum.DRAFT);
		return indicAux;
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

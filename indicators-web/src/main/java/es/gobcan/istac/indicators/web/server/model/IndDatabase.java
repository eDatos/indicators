package es.gobcan.istac.indicators.web.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;

import com.google.gwt.user.client.Random;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
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
			IndicatorDto indicAux = new IndicatorDto();
			int code = getInt();
            indicAux.setUuid(getUuid());
            indicAux.setCode("IND"+code);
			indicAux.setName(createIntString("Indicador "+code, "Indicator "+code));
			indicAux.setVersionNumber(1L);
			indicators.add(indicAux);
			
			indicAux = new IndicatorDto();
			code = getInt();
            indicAux.setUuid(getUuid());
            indicAux.setCode("IND"+code);
            indicAux.setName(createIntString("Indicador "+code, "Indicator "+code));
            indicAux.setVersionNumber(1L);
            indicators.add(indicAux);
            
            indicAux = new IndicatorDto();
            code = getInt();
            indicAux.setUuid(getUuid());
            indicAux.setCode("IND"+code);
            indicAux.setName(createIntString("Indicador "+code, "Indicator "+code));
            indicAux.setVersionNumber(1L);
            indicators.add(indicAux);
		}
		if (indicatorsSystems == null) {
			indicatorsSystems = new ArrayList<IndicatorsSystemDto>();
			IndicatorsSystemDto indSyst = new IndicatorsSystemDto();
	        int code = getInt();
			indSyst.setUuid(getUuid());
			indSyst.setCode("SYS"+code);
			indSyst.setTitle(createIntString("Sistema "+code, "System "+code));
			indicatorsSystems.add(indSyst);
			
			indSyst = new IndicatorsSystemDto();
			code = getInt();
            indSyst.setUuid(getUuid());
            indSyst.setCode("SYS"+code);
            indSyst.setTitle(createIntString("Sistema "+code, "System "+code));
			indicatorsSystems.add(indSyst);
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
	
	public static IndicatorsSystemDto getIndicatorsSystemById(Long id) {
		for (IndicatorsSystemDto indSys : indicatorsSystems) {
			if (indSys.getId().equals(id)) {
				return indSys;
			}
		}
		return null;
	}
	
	public static List<IndicatorSystemContent> getIndicatorsSystemStructure(Long indSysId) {
		return indSystemsStructure.get(indSysId);
	}
	
	public static void createIndicatorsSystem(IndicatorsSystemDto indS) {
		if (indS == null) {
			throw new IllegalArgumentException();
		}
		for (IndicatorsSystemDto indSys: indicatorsSystems) {
			if (indSys.getId().equals(indS.getId())) {
				throw new IllegalArgumentException();
			}
		}
		indicatorsSystems.add(indS);
	}
	
	//Indicators
	public static List<IndicatorDto> getIndicators() {
		return indicators;
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
	    if (ind == null) {
	        throw new IllegalArgumentException();
	    }
	    if (ind.getUuid() == null) {
	        ind.setCode("IND"+getInt());
	        ind.setUuid(getUuid());
	        ind.setVersionNumber(1L);
	        indicators.add(ind);
	    } else {
	        for (int i = 0; i < indicators.size(); i++) {
	            IndicatorDto indDB = indicators.get(i);
	            if (indDB.getUuid().equals(ind.getUuid())) {
	                ind.setVersionNumber(ind.getVersionNumber()+1);
	                indicators.set(i, ind);
	            }
	        }
	    }
	}
	
	
	/* UTILS */
	
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
        return Random.nextInt();
    }
}

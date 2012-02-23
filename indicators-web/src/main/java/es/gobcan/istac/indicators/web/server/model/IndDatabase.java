package es.gobcan.istac.indicators.web.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorStateEnum;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemStateEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class IndDatabase {

	private static Map<String,IndicatorsSystemDto> indicatorsSystems;
	private static Map<String,DimensionDto> dimensions;
	private static Map<String,IndicatorInstanceDto> indicatorInstances;
	private static Map<String,List<String>> systemsDimensions; 			//dimensions right under system root
	private static Map<String,List<String>> systemsIndicatorInstances; //all indicator instances in the system
	private static Map<String,IndicatorDto> indicators;
	
	
	static {
		if (indicators == null) {
			indicators = new HashMap<String,IndicatorDto>();
			
			for (int i = 0; i < 6; i++) {
				IndicatorDto indicAux = createBaseIndicator();
				indicators.put(indicAux.getCode(),indicAux);
			}
		}
		if (indicatorsSystems == null) {
			indicatorsSystems = new HashMap<String,IndicatorsSystemDto>();
			
			//Minimo 2
			for (int i = 0; i < 6; i++) {
				IndicatorsSystemDto indSyst = createBaseIndicatorsSystem();
				indicatorsSystems.put(indSyst.getCode(),indSyst);
			}
		}			
		//
		if (systemsDimensions == null || systemsIndicatorInstances == null) {
			dimensions = new HashMap<String, DimensionDto>();
			indicatorInstances = new HashMap<String, IndicatorInstanceDto>();
			systemsIndicatorInstances = new HashMap<String, List<String>>();
			systemsDimensions = new HashMap<String, List<String>>();
			IndicatorDto indicatorTest1 = indicators.values().iterator().next(); 
			
			
			Iterator<IndicatorsSystemDto> it = indicatorsSystems.values().iterator();
			IndicatorsSystemDto system1 = it.next();
			IndicatorsSystemDto system2 = it.next();
			
			DimensionDto dim1 = createBaseDimension("según sexo", "based on sex");
			dim1.setOrderInLevel(1L);
			dimensions.put(dim1.getUuid(), dim1);
			
			DimensionDto dim2 = createBaseDimension("según edad", "based on age");
			dim2.setOrderInLevel(2L);
			dimensions.put(dim2.getUuid(), dim2);
			
			DimensionDto dim3 = createBaseDimension("según nacionalidad", "based on nationality");
			dim3.setOrderInLevel(3L);
			dimensions.put(dim3.getUuid(), dim3);
			
			IndicatorInstanceDto indInst1 = createBaseIndicatorInstance("Hombres", "Men");
			indInst1.setOrderInLevel(1L);
			indInst1.setIndicatorUuid(indicatorTest1.getUuid());
			indInst1.setParentUuid(dim1.getUuid());
			indicatorInstances.put(indInst1.getUuid(), indInst1);
			
			IndicatorInstanceDto indInst2 = createBaseIndicatorInstance("Mujeres", "Women");
			indInst2.setOrderInLevel(2L);
			indInst2.setIndicatorUuid(indicatorTest1.getUuid());
			indInst2.setParentUuid(dim1.getUuid());
			indicatorInstances.put(indInst2.getUuid(), indInst2);
			
			
			systemsDimensions.put(system1.getUuid(), toList(dim1.getUuid(),dim2.getUuid(), dim3.getUuid()));
			systemsIndicatorInstances.put(system1.getUuid(), toList(indInst1.getUuid(),indInst2.getUuid()));
			
			
			indInst1 = createBaseIndicatorInstance("Hombres", "Men");
			indInst1.setOrderInLevel(1L);
			indInst1.setIndicatorUuid(indicatorTest1.getUuid());
			indicatorInstances.put(indInst1.getUuid(), indInst1);
			
			indInst2 = createBaseIndicatorInstance("Mujeres", "Women");
			indInst2.setOrderInLevel(2L);
			indInst2.setIndicatorUuid(indicatorTest1.getUuid());
			indicatorInstances.put(indInst2.getUuid(), indInst2);
			
			systemsDimensions.put(system2.getUuid(), new ArrayList<String>());
			systemsIndicatorInstances.put(system2.getUuid(), toList(indInst1.getUuid(),indInst2.getUuid()));
		}
	}
	
	//Indicator System
	
	public static List<IndicatorsSystemDto> getIndicatorsSystems() {
		return new ArrayList<IndicatorsSystemDto>(indicatorsSystems.values());
	}
	
	public static IndicatorsSystemDto getIndicatorsSystemByCode(String code) {
		return indicatorsSystems.get(code);
	}
	
	public static List<DimensionDto> getIndicatorsSystemDimensions(String uuid) {
		List<String> dimUuids =  systemsDimensions.get(uuid);
		List<DimensionDto> result = new ArrayList<DimensionDto>();
		if (dimUuids != null) {
			for (String dimUuid : dimUuids) {
				result.add(dimensions.get(dimUuid));
			}
		}
		return result;
	}
	
	public static List<IndicatorInstanceDto> getIndicatorsSystemIndicatorInstances(String uuid) {
		List<String> uuids = systemsIndicatorInstances.get(uuid);
		List<IndicatorInstanceDto> result = new ArrayList<IndicatorInstanceDto>();
		if (uuids != null) {
			for (String instUuid : uuids) {
				result.add(indicatorInstances.get(instUuid));
			}
		}
		return result;
	}
	
	
	public static void saveIndicatorsSystem(IndicatorsSystemDto system) {
		if (system == null || StringUtils.isEmpty(system.getCode())) {
			throw new IllegalArgumentException();
		}
		
		if (system.getUuid() == null) {
			if (indicatorsSystems.get(system.getCode()) != null) {
				throw new IllegalArgumentException("Ya existe un sistema con este código");
			}
			system.setUuid(getUuid());
			system.setVersionNumber("1.0");
			system.setState(IndicatorsSystemStateEnum.DRAFT);
			indicatorsSystems.put(system.getCode(),system);
		} else {
			Double newVersion = Double.parseDouble(system.getVersionNumber())+1;
			system.setVersionNumber(newVersion.toString());
			indicatorsSystems.put(system.getCode(), system);
		}
	}
	
	public static void deleteIndicatorsSystem(String code) {
		if (StringUtils.isEmpty(code)) {
			throw new IllegalArgumentException();
		}
		
		indicatorsSystems.remove(code);
	}

	
	//Indicators
	public static List<IndicatorDto> getIndicators() {
		return new ArrayList<IndicatorDto>(indicators.values());
	}
	
	public static IndicatorDto getIndicatorByCode(String code) {
		return indicators.get(code);
	}
	
	public static IndicatorDto getIndicatorByUuid(String uuid) {
		for (IndicatorDto ind: indicators.values()) {
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
	    	if (indicators.get(ind.getCode()) != null) {
	    		throw new IllegalArgumentException("Ya existe un indicador con ese código");
	    	}
	        ind.setUuid(getUuid());
	        ind.setVersionNumber("1.0");
	        ind.setState(IndicatorStateEnum.DRAFT);
	        indicators.put(ind.getCode(),ind);
	    } else {
	    	Double newVersion = Double.parseDouble(ind.getVersionNumber())+1;
	    	ind.setVersionNumber(newVersion.toString());
	    	indicators.put(ind.getCode(), ind);
	    }
	}
	
	public static void deleteIndicator(String code) throws MetamacException {
		if (StringUtils.isEmpty(code)) {
			throw new MetamacException(ServiceExceptionType.INDICATOR_NOT_FOUND);
		}
		indicators.remove(code);
	}
	
	
	
	/* Dimensions */
	public static DimensionDto createDimension(String systemUuid, DimensionDto dimension) {
		//Nos saltamos comprobacion de que la dimension está en el sistema
		
		dimension.setUuid(getUuid());
		dimensions.put(dimension.getUuid(), dimension);
		
		//Hacemos hueco
		List<Object> siblings = getChildrenList(systemUuid, dimension.getParentUuid());
		makeGap(siblings, dimension.getOrderInLevel());
		
		if (dimension.getParentUuid() == null) {
			systemsDimensions.get(systemUuid).add(dimension.getUuid());
		} else {
			dimensions.get(dimension.getParentUuid()).addSubdimension(dimension);
		}
		return dimension;
	}
	
	public static void saveDimension(DimensionDto dimension) {
		if (dimensions.get(dimension.getUuid()) == null) {
			new IllegalArgumentException();
		}
		dimensions.put(dimension.getUuid(), dimension);
		
		/* Es un nuevo objeto hay que actualizar los links a dimensiones */
		updateAllSubDimensions();
	}
	
	private static void updateAllSubDimensions() {
		for (DimensionDto dim : dimensions.values()) {
			List<DimensionDto> subDims = dim.getSubdimensions(); 
			for (int i = 0; i < subDims.size(); i++) {
				DimensionDto subDim = subDims.get(i);
				subDims.set(i, dimensions.get(subDim.getUuid()));
			}
		}
	}
	
	public static void moveDimension(String uuid, String systemUuid, String newParentUuid, Long newOrderInLevel) {
		//Nos saltamos comprobacion de que la dimension está en el sistema
		
		DimensionDto dim = dimensions.get(uuid);
		String oldParentUuid = dim.getParentUuid();
		dim.setParentUuid(newParentUuid);
		dim.setOrderInLevel(newOrderInLevel);
		
		//Borramos de donde estaba
		if (oldParentUuid == null) {
			systemsDimensions.get(systemUuid).remove(dim.getUuid());
		} else {
			dimensions.get(oldParentUuid).getSubdimensions().remove(dim);
		}
		
		List<Object> siblings = getChildrenList(systemUuid, newParentUuid);
		makeGap(siblings,newOrderInLevel);
		
		//Insertamos en destino
		if (newParentUuid == null) {
			systemsDimensions.get(systemUuid).add(dim.getUuid());
		} else {
			dimensions.get(newParentUuid).addSubdimension(dim);
		}
	}
	
	public static void deleteDimension(String uuid) {
		DimensionDto dim = dimensions.get(uuid);
		//Delete content
		List<DimensionDto> subDimensions = dim.getSubdimensions();
		for (DimensionDto subDim: subDimensions) {
			deleteDimension(subDim.getUuid());
		}
		if (dim.getParentUuid() == null) {
			for (Entry<String,List<String>> systemEntry: systemsDimensions.entrySet()) {
				systemEntry.getValue().remove(uuid);
			}
		} else {
			dimensions.get(dim.getParentUuid()).getSubdimensions().remove(dim);
		}
		//Remove indicator Instances children
		Set<Entry<String,IndicatorInstanceDto>> indInstancesEntries = new HashSet<Map.Entry<String,IndicatorInstanceDto>>(indicatorInstances.entrySet());
		for (Entry<String,IndicatorInstanceDto> entry : indInstancesEntries) {
			if (uuid.equals(entry.getValue().getParentUuid())) {
				deleteIndicatorInstance(entry.getKey());
			}
		}
		dimensions.remove(uuid);
	}
	
	/* Indicator instances */
	public static IndicatorInstanceDto createIndicatorInstance(String systemUuid, IndicatorInstanceDto instance) {
		
		instance.setUuid(getUuid());
		indicatorInstances.put(instance.getUuid(), instance);
		
		//Hacemos hueco
		List<Object> siblings = getChildrenList(systemUuid, instance.getParentUuid());
		makeGap(siblings, instance.getOrderInLevel());
		
		systemsIndicatorInstances.get(systemUuid).add(instance.getUuid());
		return instance;
	}
	
	public static void saveIndicatorInstance(IndicatorInstanceDto instance) {
		if (indicatorInstances.get(instance.getUuid()) == null) {
			new IllegalArgumentException();
		}
		indicatorInstances.put(instance.getUuid(), instance);
	}
	
	public static void moveIndicatorInstance(String uuid, String systemUuid, String newParentUuid, Long newOrderInLevel) {
		//Nos saltamos comprobacion de que la dimension está en el sistema
		
		IndicatorInstanceDto instance = indicatorInstances.get(uuid);
		instance.setParentUuid(newParentUuid);
		instance.setOrderInLevel(newOrderInLevel);

		//delete
		systemsIndicatorInstances.get(systemUuid).remove(instance.getUuid());
		
		List<Object> siblings = getChildrenList(systemUuid, newParentUuid);
		makeGap(siblings,newOrderInLevel);
		
		systemsIndicatorInstances.get(systemUuid).add(instance.getUuid());
	}
	
	public static void deleteIndicatorInstance(String uuid) {
		indicatorInstances.remove(uuid);
		for (Entry<String,List<String>> systemEntry: systemsIndicatorInstances.entrySet()) {
			systemEntry.getValue().remove(uuid);
		}
	}
	
	private static void makeGap(List<Object> children, Long id) {
		for (Object obj : children) {
			Long order = null;
			if (obj instanceof DimensionDto) {
				order = ((DimensionDto)obj).getOrderInLevel();
				if (order >= id) {
					((DimensionDto)obj).setOrderInLevel(order+1);
				}
			} else {
				order = ((IndicatorInstanceDto)obj).getOrderInLevel();
				if (order >= id) {
					((IndicatorInstanceDto)obj).setOrderInLevel(order+1);
				}
			}
		}
	}
	
	private static List<Object> getChildrenList(String systemUuid, String parentUuid) {
		List<DimensionDto> dimChildren = parentUuid == null ? getIndicatorsSystemDimensions(systemUuid) : dimensions.get(parentUuid).getSubdimensions();
		List<IndicatorInstanceDto> indInstChildren = new ArrayList<IndicatorInstanceDto>();
		for (IndicatorInstanceDto inst : getIndicatorsSystemIndicatorInstances(systemUuid)) {
			if (parentUuid == null && inst.getParentUuid() == null) {
				indInstChildren.add(inst);
			} else if (parentUuid != null && parentUuid.equals(inst.getParentUuid())) {
				indInstChildren.add(inst);
			}
		}
		List<Object> children = new ArrayList<Object>(dimChildren);
		children.addAll(indInstChildren);
		return children;
	}
	
	
	/* UTILS */
	
	private static <T extends Object> List<T> toList(T... objs) {
		List<T> output = new ArrayList<T>();
		for (T obj: objs) {
			output.add(obj);
		}
		return output;
	}
	
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

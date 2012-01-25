package es.gobcan.istac.indicators.web.shared.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndDatabase {

	private static List<IndicatorSystem> indicatorSystems;
	private static Map<Long,List<IndicatorSystemContent>> indSystemsStructure;
	private static List<Indicator> indicators;
	
	static {
		if (indicators == null) {
			indicators = new ArrayList<Indicator>();
			indicators.add(new Indicator(0L,"IND 0"));
			indicators.add(new Indicator(1L,"IND 1"));
			indicators.add(new Indicator(2L,"IND 2"));
		}
		if (indicatorSystems == null) {
			indicatorSystems = new ArrayList<IndicatorSystem>();
			indicatorSystems.add(new IndicatorSystem(0L,"SYS 0"));
			indicatorSystems.add(new IndicatorSystem(1L,"SYS 1"));
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
	
	public static List<IndicatorSystem> getIndicatorSystems() {
		return indicatorSystems;
	}
	
	public static IndicatorSystem getIndicatorSystemById(Long id) {
		for (IndicatorSystem indSys : indicatorSystems) {
			if (indSys.getId().equals(id)) {
				return indSys;
			}
		}
		return null;
	}
	
	public static List<IndicatorSystemContent> getIndicatorSystemStructure(Long indSysId) {
		return indSystemsStructure.get(indSysId);
	}
	
	public static void createIndicatorSystem(IndicatorSystem indS) {
		if (indS == null) {
			throw new IllegalArgumentException();
		}
		for (IndicatorSystem indSys: indicatorSystems) {
			if (indSys.getId().equals(indS.getId())) {
				throw new IllegalArgumentException();
			}
		}
		indicatorSystems.add(indS);
	}
	
	//Indicators
	public static List<Indicator> getIndicators() {
		return indicators;
	}
	
	public static Indicator getIndicatorById(Long id) {
		for (Indicator ind: indicators) {
			if (ind.getId().equals(id)) {
				return ind;
			}
		}
		return null;
	}
	
	public static void createIndicator(Indicator ind) {
		if (ind == null) {
			throw new IllegalArgumentException();
		}
		for (Indicator indDB: indicators) {
			if (indDB.getId().equals(ind.getId())) {
				throw new IllegalArgumentException();
			}
		}
		indicators.add(ind);
	}
	
}

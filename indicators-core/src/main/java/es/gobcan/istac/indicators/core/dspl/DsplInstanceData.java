package es.gobcan.istac.indicators.core.dspl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.siemac.metamac.core.common.util.Pair;

public class DsplInstanceData {

    private Map<Pair<String, String>, String> data = new HashMap<Pair<String, String>, String>();

    public DsplInstanceData() {
    }

    public void put(String geo, String time, String value) {
        data.put(new Pair<String, String>(geo, time), value);
    }

    public String get(String geo, String time) {
        return data.get(new Pair<String, String>(geo, time));
    }

    public Set<String> getGeoCodes() {
        Set<String> codes = new HashSet<String>();
        for (Pair<String, String> key : data.keySet()) {
            codes.add(key.getFirst());
        }
        return codes;
    }

    public Set<String> getTimeCodes() {
        Set<String> codes = new HashSet<String>();
        for (Pair<String, String> key : data.keySet()) {
            codes.add(key.getSecond());
        }
        return codes;
    }

}

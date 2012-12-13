package es.gobcan.istac.indicators.core.dspl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;

public class DsplLocalisedValue {

    private String              value;
    private Map<String, String> localisedValues;

    public DsplLocalisedValue() {
        this.value = null;
        localisedValues = new HashMap<String, String>();
    }
    public DsplLocalisedValue(String value) {
        this.value = value;
        localisedValues = new HashMap<String, String>();
    }

    public DsplLocalisedValue(InternationalString value) {
        this.value = null;
        localisedValues = new HashMap<String, String>();

        for (LocalisedString locText : value.getTexts()) {
            localisedValues.put(locText.getLocale(), locText.getLabel());
        }
    }

    public Set<String> getLocales() {
        return localisedValues.keySet();
    }

    public boolean isEmpty() {
        return getLocales().size() == 0 && value == null;
    }

    public boolean hasNotLocalisedValue() {
        return value != null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText(String locale) {
        return localisedValues.get(locale);
    }

    public void setText(String locale, String text) {
        localisedValues.put(locale, text);
    }

    @Override
    public String toString() {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("[");
        for (String locale : localisedValues.keySet()) {
            strBuild.append(locale).append(": ").append(localisedValues.get(locale)).append(" ");
        }
        strBuild.append("]");

        return strBuild.toString();
    }
}

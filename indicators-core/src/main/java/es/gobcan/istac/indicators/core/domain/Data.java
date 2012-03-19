package es.gobcan.istac.indicators.core.domain;

import java.util.List;

/**
 * Data, contains values
 */
public class Data {
    private List<String> variables;
    
    // valdim1, valdim2, valdim3, value    ->   n times
    private List<List<String>> values;
}

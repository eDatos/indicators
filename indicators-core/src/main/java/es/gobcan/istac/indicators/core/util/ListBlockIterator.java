package es.gobcan.istac.indicators.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListBlockIterator<T, U> {

    private List<T> list;
    private int limit;

    public ListBlockIterator(List<T> list, int limit) {
        this.list = list;
        this.limit = limit;
    }

    public List<U> iterate(ListBlockIteratorFn<T, U> fn) {
        List<U> result = new ArrayList<U>();
        int queryNumbers = list.size() / limit;
        for(int i = 0; i <= queryNumbers; i++) {
            int begin = i * limit;
            int end = begin + limit;
            if (end > list.size()) {
                end = list.size();
            }

            List<T> sublist = list.subList(begin, end);
            if (sublist.size() > 0) {
                result.addAll(fn.apply(sublist));
            }
        }
        return result;
    }

}

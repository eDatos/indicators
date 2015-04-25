package es.gobcan.istac.indicators.core.util;

import java.util.List;

public interface ListBlockIteratorFn<T, U> {

    List<U> apply(List<T> sublist);

}

package es.gobcan.istac.indicators.web.client.utils;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class PlaceRequestUtils {

    public static boolean isNameTokenInPlaceHierarchy(PlaceManager placeManager, String nameToken) {
        for (PlaceRequest placeReq : placeManager.getCurrentPlaceHierarchy()) {
            if (nameToken.equals(placeReq.getNameToken())) {
                return true;
            }
        }
        return false;
    }

    public static List<PlaceRequest> getHierarchyUntilNameToken(PlaceManager placeManager, String nameToken) {
        List<PlaceRequest> filteredHierarchy = new ArrayList<PlaceRequest>();
        List<PlaceRequest> hierarchy = placeManager.getCurrentPlaceHierarchy();
        boolean found = false;
        for (int i = 0; i < hierarchy.size() && !found; i++) {
            PlaceRequest placeReq = hierarchy.get(i);
            if (placeReq.matchesNameToken(nameToken)) {
                found = true;
            }
            filteredHierarchy.add(placeReq);
        }

        return filteredHierarchy;
    }

}

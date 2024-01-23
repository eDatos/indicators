package es.gobcan.istac.indicators.web.client.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.client.utils.CommonPlaceRequestUtils;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

import es.gobcan.istac.indicators.core.dto.RelatedResourceDto;
import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;

public class PlaceRequestUtils extends CommonPlaceRequestUtils {

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

    public static String getGeoValueParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.adminGeoValuesPage, es.gobcan.istac.indicators.core.navigation.shared.PlaceRequestParams.adminGeoValueParam);
    }

    public static String getTabParamFromUrl(PlaceManager placeManager) {
        return getParamFromUrl(placeManager, NameTokens.adminGeoValuesPage, es.gobcan.istac.indicators.core.navigation.shared.PlaceRequestParams.adminGeoValueParam);
    }

    // ---------------------------------------------------------------------------
    // ADMIN
    // ---------------------------------------------------------------------------

    // Annotation type

    public static List<PlaceRequest> buildAbsoluteAdminPlaceRequest() {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.adminPage));
        // placeRequestHierarchy.add(new PlaceRequest(NameTokens.adminGeoValuesPage));
        return placeRequestHierarchy;
    }

    public static PlaceRequest buildRelativeGeoValuePlaceRequest(String geoValueCode) {
        return new PlaceRequest(NameTokens.adminGeoValuesPage).with(es.gobcan.istac.indicators.core.navigation.shared.PlaceRequestParams.adminGeoValueParam, geoValueCode);
        // PlaceRequest placeRequest = new PlaceRequest(NameTokens.adminGeoValuesPage);
    }

    public static List<PlaceRequest> buildAbsoluteGeoValueAdminPlaceRequest(String geoValueCode) {
        List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
        placeRequestHierarchy.add(new PlaceRequest(NameTokens.adminPage).with(es.gobcan.istac.indicators.core.navigation.shared.PlaceRequestParams.adminTabParam, NameTokens.adminGeoValuesPage)
                .with(es.gobcan.istac.indicators.core.navigation.shared.PlaceRequestParams.adminGeoValueParam, geoValueCode));
        return placeRequestHierarchy;
    }

    public static List<PlaceRequest> buildAbsoluteResourcePlaceRequest(RelatedResourceDto relatedResourceDto) {
        if (relatedResourceDto != null) {
            String geoValueCode = relatedResourceDto.getCode();
            if (geoValueCode != null) {
                return buildAbsoluteGeoValueAdminPlaceRequest(geoValueCode);
            }
        }
        return null;
    }
}

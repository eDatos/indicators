package es.gobcan.istac.indicators.web.shared;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@GenDispatch(isSecure = false)
public class VersioningIndicatorsSystem {

    @In(1)
    IndicatorsSystemDtoWeb indicatorsSystemToVersioning;

    @In(2)
    VersionTypeEnum        versionType;

    @Out(1)
    IndicatorsSystemDtoWeb indicatorsSystemDtoWeb;

}

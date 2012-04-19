package es.gobcan.istac.indicators.rest.test.mocks;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;

public class IndicatorsSystemVersionMock {

    private static Map<String, IndicatorsSystemVersion> INSTANCES                   = new HashMap<String, IndicatorsSystemVersion>();
    private static String                               INDICATORS_SYSTEM_VERSION_1 = "IndicatorsSystemVersion_1";
    private static String                               INDICATORS_SYSTEM_VERSION_2 = "IndicatorsSystemVersion_2";

    public static IndicatorsSystemVersion mockIndicatorsSystemVersion1() {
        IndicatorsSystemVersion indicatorsSystemVersion = INSTANCES.get(INDICATORS_SYSTEM_VERSION_1);
        if (indicatorsSystemVersion != null) {
            return indicatorsSystemVersion;
        }

        // private IndicatorsSystemProcStatusEnum procStatus;
        // private IndicatorsSystem indicatorsSystem;
        // private List<ElementLevel> childrenAllLevels = new ArrayList<ElementLevel>();
        // private List<ElementLevel> childrenFirstLevel = new ArrayList<ElementLevel>();

        indicatorsSystemVersion = new IndicatorsSystemVersion();
        INSTANCES.put(INDICATORS_SYSTEM_VERSION_1, indicatorsSystemVersion);

        indicatorsSystemVersion.setVersionNumber("1.0");
        indicatorsSystemVersion.setIsLastVersion(Boolean.TRUE);
        indicatorsSystemVersion.setProductionValidationDate(new DateTime());
        indicatorsSystemVersion.setProductionValidationUser("user1");
        indicatorsSystemVersion.setDiffusionValidationDate(new DateTime());
        indicatorsSystemVersion.setDiffusionValidationUser("user1");
        indicatorsSystemVersion.setPublicationDate(new DateTime());
        indicatorsSystemVersion.setPublicationUser("user1");
        indicatorsSystemVersion.setArchiveDate(new DateTime());
        indicatorsSystemVersion.setArchiveUser("user1");
        indicatorsSystemVersion.getUuid(); // TO GENERATE
        indicatorsSystemVersion.setCreatedDate(new DateTime());
        indicatorsSystemVersion.setCreatedBy("user1");
        indicatorsSystemVersion.setLastUpdated(new DateTime());
        indicatorsSystemVersion.setLastUpdatedBy("user1");
        indicatorsSystemVersion.setProcStatus(IndicatorsSystemProcStatusEnum.PUBLISHED);

        indicatorsSystemVersion.setIndicatorsSystem(IndicatorsSystemMock.mockIndicatorsSystem1());
        indicatorsSystemVersion.getChildrenFirstLevel().add(ElementLevelMock.mockElementLevel_indicatorSystem_1_level_1());

        return indicatorsSystemVersion;
    }

    public static IndicatorsSystemVersion mockIndicatorsSystemVersion2() {
        IndicatorsSystemVersion indicatorsSystemVersion = INSTANCES.get(INDICATORS_SYSTEM_VERSION_2);
        if (indicatorsSystemVersion != null) {
            return indicatorsSystemVersion;
        }

        indicatorsSystemVersion = new IndicatorsSystemVersion();
        INSTANCES.put(INDICATORS_SYSTEM_VERSION_2, indicatorsSystemVersion);

        indicatorsSystemVersion.setVersionNumber("2.0");
        indicatorsSystemVersion.setIsLastVersion(Boolean.TRUE);
        indicatorsSystemVersion.setProductionValidationDate(new DateTime());
        indicatorsSystemVersion.setProductionValidationUser("user2");
        indicatorsSystemVersion.setDiffusionValidationDate(new DateTime());
        indicatorsSystemVersion.setDiffusionValidationUser("user2");
        indicatorsSystemVersion.setPublicationDate(new DateTime());
        indicatorsSystemVersion.setPublicationUser("user2");
        indicatorsSystemVersion.setArchiveDate(new DateTime());
        indicatorsSystemVersion.setArchiveUser("user2");
        indicatorsSystemVersion.getUuid(); // TO GENERATE
        indicatorsSystemVersion.setCreatedDate(new DateTime());
        indicatorsSystemVersion.setCreatedBy("user2");
        indicatorsSystemVersion.setLastUpdated(new DateTime());
        indicatorsSystemVersion.setLastUpdatedBy("user2");
        indicatorsSystemVersion.setProcStatus(IndicatorsSystemProcStatusEnum.PUBLISHED);
        indicatorsSystemVersion.setIndicatorsSystem(IndicatorsSystemMock.mockIndicatorsSystem2());

        return indicatorsSystemVersion;
    }
}

package es.gobcan.istac.indicators.core.repositoryimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import es.gobcan.istac.indicators.core.constants.IndicatorsConfigurationConstants;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.domain.SubjectRepository;

/**
 * Repository implementation for Subject
 */

public class SubjectRepositoryImpl implements SubjectRepository {

    @Autowired
    private ConfigurationService configurationService;

    private final Logger         LOG = LoggerFactory.getLogger(SubjectRepositoryImpl.class);

    @Autowired
    @Qualifier("dataSourceSubjects")
    private DataSource           dataSource;

    private String               codeColumn;
    private String               titleColumn;
    private String               tableName;

    public SubjectRepositoryImpl() {
    }

    @Override
    public Subject retrieveSubject(String code) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        List<Subject> result = template.query("select " + getCodeColumnName() + "," + getTitleColumnName() + " from " + getTableName() + " where " + getCodeColumnName() + " = ?", new Object[]{code},
                new SubjectRowMapper());

        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<Subject> findSubjects() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select.query("select " + getCodeColumnName() + "," + getTitleColumnName() + " from " + getTableName() + " order by " + getCodeColumnName(), new SubjectRowMapper());
    }

    public String getCodeColumnName() {
        if (codeColumn == null) {
            codeColumn = getRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_COLUMN_CODE);
        }
        return codeColumn;
    }

    public String getTitleColumnName() {
        if (titleColumn == null) {
            titleColumn = getRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_COLUMN_TITLE);
        }
        return titleColumn;
    }

    public String getTableName() {
        if (tableName == null) {
            tableName = getRequiredProperty(IndicatorsConfigurationConstants.DB_SUBJECTS_TABLE);
        }
        return tableName;
    }

    private String getRequiredProperty(String propertyName) {
        String value = configurationService.getProperty(propertyName);
        if (!StringUtils.isEmpty(value)) {
            return value;
        } else {
            LOG.error("The required DATA property " + propertyName + " has not been set or is empty");
            throw new IllegalStateException("The required DATA property " + propertyName + " has not been set or is empty");
        }
    }

    private class SubjectRowMapper implements RowMapper<Subject> {

        @Override
        public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
            Subject subject = new Subject();
            subject.setId(rs.getString(getCodeColumnName()));
            subject.setTitle(rs.getString(getTitleColumnName()));
            return subject;
        }
    }

}

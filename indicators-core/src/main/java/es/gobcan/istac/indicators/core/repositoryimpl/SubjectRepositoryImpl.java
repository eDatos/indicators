package es.gobcan.istac.indicators.core.repositoryimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.Subject;
import es.gobcan.istac.indicators.core.domain.SubjectRepository;

/**
 * Repository implementation for Subject
 */

public class SubjectRepositoryImpl implements SubjectRepository {

    @Autowired
    private IndicatorsConfigurationService configurationService;

    private static final Logger            LOG = LoggerFactory.getLogger(SubjectRepositoryImpl.class);

    @Autowired
    @Qualifier("dataSourceSubjects")
    private DataSource                     dataSource;

    public SubjectRepositoryImpl() {
    }

    @Override
    public Subject retrieveSubject(String code) throws MetamacException {
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
    public List<Subject> findSubjects() throws MetamacException {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select.query("select " + getCodeColumnName() + "," + getTitleColumnName() + " from " + getTableName() + " order by " + getCodeColumnName(), new SubjectRowMapper());
    }

    public String getCodeColumnName() throws MetamacException {
        return configurationService.retrieveDbSubjectsColumnCode();
    }

    public String getTitleColumnName() throws MetamacException {
        return configurationService.retrieveDbSubjectsColumnTitle();
    }

    public String getTableName() throws MetamacException {
        return configurationService.retrieveDbSubjectsTable();
    }

    private class SubjectRowMapper implements RowMapper<Subject> {

        @Override
        public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
            Subject subject = new Subject();
            try {
                subject.setId(rs.getString(getCodeColumnName()));
                subject.setTitle(rs.getString(getTitleColumnName()));
            } catch (MetamacException e) {
                LOG.error("Properties not found", e);
            }
            return subject;
        }
    }

}

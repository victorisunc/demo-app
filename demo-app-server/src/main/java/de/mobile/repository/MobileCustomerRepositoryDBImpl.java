package de.mobile.repository;

import de.mobile.entity.MobileCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Clock;
import java.util.List;

@Repository
public class MobileCustomerRepositoryDBImpl extends NamedParameterJdbcDaoSupport implements MobileCustomerRepository{

     private static final String TABLE = "mobile_customer";

     private static final String ID = "id";
     private static final String FIRST_NAME = "first_name";
     private static final String LAST_NAME = "last_name";
     private static final String COMPANY_NAME = "company_name";
     private static final String EMAIL = "email";
     private static final String CREATE_TIME = "create_time";
     private static final String UPDATE_TIME = "update_time";

     private static final String[] INSERT_COLS = {
             FIRST_NAME,
             LAST_NAME,
             COMPANY_NAME,
             EMAIL,
             CREATE_TIME,
             UPDATE_TIME
     };

     @Autowired
     private Clock clock;

     public MobileCustomerRepositoryDBImpl(DataSource dataSource) {
          setDataSource(dataSource);
     }

     @Override
     public Long create(MobileCustomer customer) {
          final KeyHolder keyHolder = new GeneratedKeyHolder();
          final String sql = SqlHelper.insert(TABLE, INSERT_COLS);
          getNamedParameterJdbcTemplate().update(sql, createSource(customer), keyHolder, new String[] { ID });
          return keyHolder.getKey().longValue();
     }

     @Override
     public MobileCustomer get(Long id) {
          final String sql = SqlHelper.select(TABLE, "*") + " where id = ?";
          return DataAccessUtils.singleResult(getJdbcTemplate().query(sql, this::mapRow, id));
     }

     @Override
     public boolean delete(Long id) {
          final String sql = SqlHelper.delete(TABLE) + " where id = ?";
          return getJdbcTemplate().update(sql, id) > 0;
     }

     @Override
     public List<MobileCustomer> list() {
          final String sql = SqlHelper.select(TABLE, "*") + " ORDER BY id ASC ";
          return getJdbcTemplate().query(sql, this::mapRow);
     }

     private SqlParameterSource createSource(MobileCustomer customer) {
          final long now = clock.millis();
          final MapSqlParameterSource source = new MapSqlParameterSource();
          source.addValue(ID, customer.getId());
          source.addValue(FIRST_NAME, customer.getFirstName());
          source.addValue(LAST_NAME, customer.getLastName());
          source.addValue(COMPANY_NAME, customer.getCompanyName());
          source.addValue(EMAIL, customer.getEmail());
          source.addValue(CREATE_TIME, new Timestamp(now));
          source.addValue(UPDATE_TIME, new Timestamp(now));
          return source;
     }

     private MobileCustomer mapRow(ResultSet rs, int rowNum) throws SQLException {
          final MobileCustomer customer = new MobileCustomer();
          customer.setId(rs.getLong(ID));
          customer.setFirstName(rs.getString(FIRST_NAME));
          customer.setLastName(rs.getString(LAST_NAME));
          customer.setEmail(rs.getString(EMAIL));
          customer.setCreateTime(rs.getTimestamp(CREATE_TIME).getTime());
          customer.setUpdateTime(rs.getTimestamp(UPDATE_TIME).getTime());
          return customer;
     }

}

package de.mobile.repository;

import de.mobile.entity.Category;
import de.mobile.entity.MobileAd;
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
public class MobileAdRepositoryDBImpl extends NamedParameterJdbcDaoSupport implements MobileAdRepository{

    private static final String TABLE = "mobile_ad";

    private static final String ID = "id";
    private static final String MAKE = "make";
    private static final String MODEL = "model";
    private static final String DESCRIPTION = "description";
    private static final String CATEGORY = "category";
    private static final String PRICE = "price";
    private static final String MOBILE_CUSTOMER_ID = "mobile_customer_id";
    private static final String CREATE_TIME = "create_time";
    private static final String UPDATE_TIME = "update_time";

    private static final String[] INSERT_COLS = {
            MAKE,
            MODEL,
            DESCRIPTION,
            CATEGORY,
            PRICE,
            MOBILE_CUSTOMER_ID,
            CREATE_TIME,
            UPDATE_TIME
    };

    @Autowired
    private Clock clock;

    public MobileAdRepositoryDBImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    @Override
    public Long create(MobileAd ad) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = SqlHelper.insert(TABLE, INSERT_COLS);
        getNamedParameterJdbcTemplate().update(sql, createSource(ad), keyHolder, new String[] { ID });
        return keyHolder.getKey().longValue();
    }

    @Override
    public MobileAd get(Long adId) {
        final String sql = SqlHelper.select(TABLE, "*") + " where id = :id";
        return DataAccessUtils.singleResult(getJdbcTemplate().query(sql, this::mapRow, adId));
    }

    @Override
    public List<MobileAd> list() {
        final String sql = SqlHelper.select(TABLE, "*") + " ORDER BY id ASC ";
        return getJdbcTemplate().query(sql, this::mapRow);
    }

    private SqlParameterSource createSource(MobileAd ad) {
        final long now = clock.millis();
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(ID, ad.getId());
        source.addValue(MAKE, ad.getMake());
        source.addValue(MODEL, ad.getModel());
        source.addValue(DESCRIPTION, ad.getDescription());
        source.addValue(CATEGORY, ad.getCategory().name());
        source.addValue(PRICE, ad.getPrice());
        source.addValue(MOBILE_CUSTOMER_ID, ad.getMobileCustomerId());
        source.addValue(CREATE_TIME, new Timestamp(now));
        source.addValue(UPDATE_TIME, new Timestamp(now));
        return source;
    }

    private MobileAd mapRow(ResultSet rs, int rowNum) throws SQLException {
        final MobileAd ad = new MobileAd();
        ad.setId(rs.getLong(ID));
        ad.setMake(rs.getString(MAKE));
        ad.setModel(rs.getString(MODEL));
        ad.setDescription(rs.getString(DESCRIPTION));
        ad.setCategory(Category.valueOf(rs.getString(CATEGORY)));
        ad.setPrice(rs.getBigDecimal(PRICE));
        ad.setMobileCustomerId(rs.getLong(MOBILE_CUSTOMER_ID));
        ad.setCreateTime(rs.getTimestamp(CREATE_TIME).getTime());
        ad.setUpdateTime(rs.getTimestamp(UPDATE_TIME).getTime());
        return ad;
    }

}

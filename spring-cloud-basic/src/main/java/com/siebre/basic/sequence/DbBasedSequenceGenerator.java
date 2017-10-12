package com.siebre.basic.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jhonelee on 2017/7/26.
 */
@Configurable
public class DbBasedSequenceGenerator implements SequenceGenerator {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String defaultSequenceName = "default";

	@Override
	public String next() {
		String sql = "select seq_nextval(?)";

		String result = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				return resultSet.next() ? resultSet.getString(1) : null;
			}
		}, this.defaultSequenceName);

		return result;
	}
}

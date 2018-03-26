package com.enlightent.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jianglei
 * @since 2018/1/30
 */

@Service
public class HiveService {

    private DataSource hiveHikariDataSource;

    public List<Map> executeQuery(String sql) {
        if (StringUtils.isBlank(sql)) {
            throw new IllegalArgumentException("The sql must not be blank");
        }
        
        List<Map> resultList = new ArrayList<>();
        Connection connection = null;
        try {
        	connection = hiveHikariDataSource.getConnection();
            PreparedStatement stat = connection.prepareStatement(sql);
            ResultSet resultSet = stat.executeQuery();
            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();
                Map<String, Object> map = new LinkedHashMap<>(count);
                for(int i = 1; i <= count; i++) {
                    String label = metaData.getColumnLabel(i);
                    Object res = resultSet.getObject(i);
                    map.put(label, res);
                }
                resultList.add(map);
            }
		}  catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
        return resultList;
    }


    @Autowired
    @Qualifier("hiveHikariDataSource")
    public void setHiveHikariDataSource(Object hiveHikariDataSource) {
        this.hiveHikariDataSource = (DataSource) hiveHikariDataSource;
    }
}

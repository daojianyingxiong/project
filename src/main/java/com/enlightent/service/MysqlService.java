package com.enlightent.service;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;

/**
* @author 作者 wr:
* @version 创建时间：2018年2月8日 下午6:07:03
* 类说明
*/
@Service
public class MysqlService {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@PersistenceContext(unitName = "devPersistenceUnit")
    EntityManager entityManagerDev;
	
	public List<Map> getRestult(String source, String sql){
		Query query = null;
		if (source.equals("mysql-test")) {
			query = entityManagerDev.createNativeQuery(sql);
		} else {
			query = entityManager.createNativeQuery(sql);
		}
		MyAliasToEntityMapResultTransformer aliasToEntityMap = MyAliasToEntityMapResultTransformer.INSTANCE;
		query.unwrap(SQLQuery.class).setResultTransformer(aliasToEntityMap);
		return query.getResultList();
	}
}

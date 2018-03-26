package com.enlightent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.enlightent.util.ConfUtil;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

/**
 * @author 作者 wr:
 * @version 创建时间：2018年2月8日 下午6:03:09 类说明
 */
@Service
public class MongoService {

	static MongoClient crawlerClient = null;
	static MongoClient mongoClient = null;
	static MongoClient testClient = null;
	static {
		List<MongoCredential> crawler = new ArrayList<>();
		crawler.add(MongoCredential.createCredential("yunhemongo", "crawler", "QtNp1nXdh".toCharArray()));
		List<MongoCredential> list = new ArrayList<>();
		
		list.add(MongoCredential.createCredential("enlightent", "portrait", "yun!fuck601".toCharArray()));
		list.add(MongoCredential.createCredential("enlightent", "enlightent_daily", "yun!fuck601".toCharArray()));
		list.add(MongoCredential.createCredential("enlightent", "enlightent_minute", "yun!fuck601".toCharArray()));
		list.add(MongoCredential.createCredential("enlightent", "novel", "yun!fuck601".toCharArray()));

		crawlerClient = new MongoClient(getServerAddressList(ConfUtil.MONGO_ADDRESS_CRAWLER), crawler);
		mongoClient = new MongoClient(getServerAddressList(ConfUtil.MONGO_ADDRESS), list);
		testClient = new MongoClient(getServerAddressList(ConfUtil.MONGO_TEST_ADDRESS), list);
		

	}
	
	private static List<ServerAddress> getServerAddressList(String address) {
		String[] split = address.split(",");
		List<ServerAddress> list = new ArrayList<>(split.length);
		for (String string : split) {
			String[] split2 = string.split(":");
			list.add(new ServerAddress(split2[0], Integer.parseInt(split2[1])));
		}
		return list;
	}

	public List<Map> parseMongo(String sql, String source) {
		Map fromJson = (Map) JSON.parse(sql);
		String database = (String) fromJson.get("database");
		String mongoSql = (String) fromJson.get("sql");

		String table = (String) fromJson.get("table");
		DBCollection collection = getDBCollection(source, database, table);
		if (mongoSql.startsWith("[")) {
			BasicDBList basicDBList = (BasicDBList) JSON.parse(mongoSql);
			List<DBObject> list = new ArrayList<>();
			for (Object object : basicDBList) {
				list.add((DBObject) object);
			}
			AggregationOutput output = collection.aggregate(list);
			List<DBObject> results = (List) output.results();
			List<Map> result = new ArrayList<>(results.size());
			for (DBObject dbObject : results) {
				result.add(dbObject.toMap());
			}
			return result;
		} else {
			mongoSql = "[" + mongoSql + "]";
			BasicDBList basicDBList = (BasicDBList) JSON.parse(mongoSql);
			DBCursor cursor = null;
			if (basicDBList.size() == 1) {
				DBObject dbObject = (DBObject) basicDBList.get(0);
				cursor = collection.find(dbObject);
			} else {
				DBObject dbObject = (DBObject) basicDBList.get(0);
				DBObject db = (DBObject) basicDBList.get(1);
				cursor = collection.find(dbObject, db);
			}
			List<Map> list = null;
			if (cursor != null) {
				list = new ArrayList<>();
				while (cursor.hasNext()) {
					DBObject obj = cursor.next();
					list.add(obj.toMap());
				}
			}
			return list;
		}
	}

	public DBCollection getDBCollection(String source, String database, String table) {
		DB db = null;
		if (database.equals("crawler")) {
			db = crawlerClient.getDB(database);
		} else {
			if (source.equals("mongo-test")) {
				db = testClient.getDB(database);
			} else {
				db = mongoClient.getDB(database);
			}
		}
		DBCollection dbc = db.getCollection(table);
		return dbc;
	}

}

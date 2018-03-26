package com.enlightent.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClientURI;

@Configuration
public class MongoConfiguration {
	
	@Value("${mongo.address}")
    private String mongoAddress;
	
	@Value("${mongo.address.crawler}")
	private String mongoAddressCrawler;
	
	private static final String MONGO_PARAMS = "?connectTimeoutMS=1000&socketTimeoutMS=1000&minPoolSize=1&waitQueueMultiple=4&wtimeoutMS=1000";
	
    @Bean
    @Primary
    @Qualifier("portrayalMongoDbFactory")
    public MongoDbFactory portrayalMongoDbFactory() throws Exception{
    	String portrayalUri = "mongodb://enlightent:yun!fuck601@" + mongoAddress + "/portrait" + MONGO_PARAMS;
        MongoClientURI mongoClientURI = new MongoClientURI(portrayalUri);
        return new SimpleMongoDbFactory(mongoClientURI);
    }
//
//    @Bean
//    @Qualifier("dailyMongoDbFactory")
//    public MongoDbFactory dailyMongoDbFactory() throws Exception{
//    	String dailyUri = "mongodb://enlightent:yun!fuck601@" + mongoAddress + "/enlightent_daily" + MONGO_PARAMS;
//        MongoClientURI mongoClientURI = new MongoClientURI(dailyUri);
//        return new SimpleMongoDbFactory(mongoClientURI);
//    }
//
//    @Bean
//    @Qualifier("minuteMongoDbFactory")
//    public MongoDbFactory minuteMongoDbFactory() throws Exception{
//    	String minuteUri = "mongodb://enlightent:yun!fuck601@" + mongoAddress + "/enlightent_minute" + MONGO_PARAMS;
//        MongoClientURI mongoClientURI = new MongoClientURI(minuteUri);
//        return new SimpleMongoDbFactory(mongoClientURI);
//    }
//
//    @Bean
//    @Qualifier("novelMongoDbFactory")
//    public MongoDbFactory novelMongoDbFactory() throws Exception{
//    	String novelUri = "mongodb://enlightent:yun!fuck601@" + mongoAddress + "/novel" + MONGO_PARAMS;
//        MongoClientURI mongoClientURI = new MongoClientURI(novelUri);
//        return new SimpleMongoDbFactory(mongoClientURI);
//    }
//    
//    @Bean
//    @Qualifier("crawlerFactory")
//    public MongoDbFactory crawlerFactory() throws Exception{
//    	String novelUri = "mongodb://yunhemongo:QtNp1nXdh@" + mongoAddressCrawler + "/crawler" + MONGO_PARAMS;
//    	MongoClientURI mongoClientURI = new MongoClientURI(novelUri);
//    	return new SimpleMongoDbFactory(mongoClientURI);
//    }
//
//
//    @Bean
//    @Qualifier("crawlerTemplate")
//    public MongoTemplate crawlerTemplate()  throws Exception{
//    	return new MongoTemplate(crawlerFactory());
//    }
//    
    @Bean
    @Primary
    @Qualifier("portrayalTemplate")
    public MongoTemplate portrayalTemplate()  throws Exception{
        return new MongoTemplate(portrayalMongoDbFactory());
    }
//
//    @Bean
//    @Qualifier("dailyTemplate")
//    public MongoTemplate dailyTemplate()  throws Exception{
//        return new MongoTemplate(dailyMongoDbFactory());
//    }
//
//    @Bean
//    @Qualifier("minuteTemplate")
//    public MongoTemplate minuteTemplate()  throws Exception{
//        return new MongoTemplate(minuteMongoDbFactory());
//    }
//
//    @Bean
//    @Qualifier("novelTemplate")
//    public MongoTemplate novelTemplate()  throws Exception{
//        return new MongoTemplate(novelMongoDbFactory());
//    }
}

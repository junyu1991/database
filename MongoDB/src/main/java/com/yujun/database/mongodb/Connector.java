package com.yujun.database.mongodb;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import java.util.Collections;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/10/14
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */
@Configuration
public class Connector extends AbstractMongoConfiguration {
    @Value("${mongo.host}")
    private String host;
    @Value("${mongo.port}")
    private int port;
    @Value("${mongo.username}")
    private String userName;
    @Value("${mongo.password}")
    private String password;
    /** 保存用户名密码的数据库 **/
    @Value("${mongo.userdb}")
    private String userDb;
    @Value("${file.mongo.database}")
    private String database;
    @Value("${file.mongo.geodatabase}")
    private String geoDatabase;
    @Value("${file.mongo.textdatabase}")
    private String textDatabase;

    /**
     * 从配置文件中获取mongodb host以及port初始化，返回client
     * @author: yujun
     * @date: 2019/10/14
     * @description: TODO
     * @param
     * @return: {@link MongoClient}
     * @exception:
    */
    @Bean(name = "mongoClient")
    public MongoClient mongoClient() {
        MongoCredential mongoCredential = MongoCredential.createCredential(userName, userDb, password.toCharArray());
//        MongoClientOptions.Builder builder = MongoClientOptions.builder();
//        return new MongoClient(Collections.singletonList(new ServerAddress(host, port)), mongoCredential, null);
        return new MongoClient(host, port);
    }

    @Bean(name = "mongoClientFactoryBean")
    public MongoClientFactoryBean mongoClientFactoryBean() {
        MongoClientFactoryBean mongoClientFactoryBean = new MongoClientFactoryBean();
        mongoClientFactoryBean.setHost(host);
        mongoClientFactoryBean.setPort(port);
        mongoClientFactoryBean.setSingleton(true);
        return mongoClientFactoryBean;
    }

    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient(), this.getDatabaseName());
        mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient(), this.getDatabaseName()));
        MappingMongoConverter mappingMongoConverter = super.mappingMongoConverter();
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient(), this.getDatabaseName()), mappingMongoConverter);
        return mongoTemplate;
    }
    @Bean(name = "geoMongoTemplate")
    public MongoTemplate geoMongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = null;
        MappingMongoConverter mappingMongoConverter = super.mappingMongoConverter();
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient(), geoDatabase), mappingMongoConverter);
        return mongoTemplate;
    }

    @Bean(name = "textMongoTemplate")
    public MongoTemplate textMongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = null;
        MappingMongoConverter mappingMongoConverter = super.mappingMongoConverter();
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient(), textDatabase), mappingMongoConverter);
        return mongoTemplate;
    }

    protected String getDatabaseName() {
        return this.database;
    }
}

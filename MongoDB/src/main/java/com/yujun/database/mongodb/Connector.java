package com.yujun.database.mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Value("${mongo.hosts}")
    private String[] hosts;

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

    @Value("${mongo.connect.connectionsPerHost}")
    private int connectionsPerHost;

    @Value("${mongo.connect.threadsAllowedToBlockForConnectionMultiplier}")
    private int threadsAllowedToBlockForConnectionMultiplier;

    @Value("${mongo.connect.connectTimeout}")
    private int connectTimeout;
    @Value("${mongo.connect.maxWaitTime}")
    private int maxWaitTime;
    @Value("${mongo.connect.maxConnectionIdleTime}")
    private int maxConnectionIdleTime;
    @Value("${mongo.connect.socketTimeout}")
    private int socketTimeout;


    /**
     * 只是用host以及port初始化MongoClient
     * @author: yujun
     * @date: 2019/10/24
     * @param
     * @return: {@link com.mongodb.MongoClient}
     * @exception:
    */
    @Bean("simpleMongoClient")
    public MongoClient simpleMongoClient() {
        return new MongoClient(host, port);
    }

    /**
     * 使用集群MongoDB server初始化MongoClient，Spring会扫描所有的MongoDB Server节点，获取集群信息
     * @author: yujun
     * @date: 2019/10/24
     * @param
     * @return: {@link com.mongodb.MongoClient}
     * @exception:
    */
    @Bean("mongoClientWithReplicat")
    public MongoClient mongoClientWithReplicat() {
        MongoClient mongoClient = new MongoClient(getAllServerAddress());
        return mongoClient;
    }

    /**
     * 将配置文件中的MongoDB Server连接方式(host:port)转换成ServerAddress列表
     * 配置文件中的配置示例： 192.168.0.102:21017, 192.168.0.107:21017
     * @author: yujun
     * @date: 2019/10/24
     * @param
     * @return: {@link java.util.List<com.mongodb.ServerAddress>}
     * @exception:
    */
    private List<ServerAddress> getAllServerAddress() {
        List<String> strings = Arrays.asList(hosts);
        List<ServerAddress> serverAddresses = new ArrayList<>();
        for(String host : strings) {
            String[] split = host.split(":");
            if(split.length == 2) {
                serverAddresses.add(new ServerAddress(split[0], Integer.parseInt(split[1])));
            }
        }
        return serverAddresses;
    }

    /**
     * 使用MongoClientOptions以及MongoCredential(用于配置用户名以及密码相关项目)初始化MongoClient
     * 若MongoDB Server是集群模式，则初始化MongoClient时传入<code>List<ServerAddress></code>即可
     * @author: yujun
     * @date: 2019/10/14
     * @description: TODO
     * @param
     * @return: {@link MongoClient}
     * @exception:
    */
    @Bean(name = "mongoClient")
    public MongoClient mongoClient() {
        MongoClientOptions mongoClientOptions = mongoClientOptions();
        ServerAddress serverAddress = new ServerAddress(host, port);
        MongoCredential mongoCredential = MongoCredential.createCredential(userName, userDb, password.toCharArray());
        //return new MongoClient(getAllServerAddress(), mongoCredential, mongoClientOptions); //使用List<ServerAddress>初始化即可使用Mongo集群
        return new MongoClient(serverAddress, mongoCredential, mongoClientOptions);
    }

    /**
     * 将配置文件中的Mongo client连接项转换成MongoClientOptions
     * @author: yujun
     * @date: 2019/10/24
     * @param
     * @return: {@link com.mongodb.MongoClientOptions}
     * @exception:
    */
    private MongoClientOptions mongoClientOptions() {
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder()
                .connectionsPerHost(connectionsPerHost)
                .threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier)
                .connectTimeout(connectTimeout)
                .maxWaitTime(maxWaitTime)
                .maxConnectionIdleTime(maxConnectionIdleTime)
                .socketTimeout(socketTimeout)
                .writeConcern(WriteConcern.ACKNOWLEDGED)//设置WriteConcern
                .build();
        return mongoClientOptions;
    }

    /**
     * 初始化MongoClientFactoryBean，MongoClientFactoryBean也可作为MongoClient使用
     * @author: yujun
     * @date: 2019/10/24
     * @param
     * @return: {@link org.springframework.data.mongodb.core.MongoClientFactoryBean}
     * @exception:
    */
    @Bean(name = "mongoClientFactoryBean")
    public MongoClientFactoryBean mongoClientFactoryBean() {
        MongoCredential mongoCredential = MongoCredential.createCredential(userName, userDb, password.toCharArray());
        MongoClientFactoryBean mongoClientFactoryBean = new MongoClientFactoryBean();
        mongoClientFactoryBean.setHost(host);
        mongoClientFactoryBean.setPort(port);
        mongoClientFactoryBean.setSingleton(true);
        mongoClientFactoryBean.setCredentials(new MongoCredential[]{mongoCredential});
        MongoClientOptions mongoClientOptions = mongoClientOptions();
        mongoClientFactoryBean.setMongoClientOptions(mongoClientOptions);
        return mongoClientFactoryBean;
    }

    /**
     * 初始化MongoDbFactory
     * @author: yujun
     * @date: 2019/10/24
     * @param
     * @return: {@link org.springframework.data.mongodb.MongoDbFactory}
     * @exception:
    */
    @Bean(name = "mongoDbFactory")
    public MongoDbFactory mongoDbFactory() {
        SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClient(), database);
        MongoDatabase db = simpleMongoDbFactory.getDb();
        return simpleMongoDbFactory;
    }

    /**
     * 使用mongoClient以及数据库名初始化MongoTemplate
     * @author: yujun
     * @date: 2019/10/24
     * @param
     * @return: {@link org.springframework.data.mongodb.core.MongoTemplate}
     * @exception:
    */
    public MongoTemplate simpleMongoTemplate() {
        return new MongoTemplate(mongoClient(), database);
    }

    /**
     * 使用MongoDbFactory初始化MongoTemplate
     * @author: yujun
     * @date: 2019/10/24
     * @param
     * @return: {@link org.springframework.data.mongodb.core.MongoTemplate}
     * @exception:
    */
    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
    /*    MappingMongoConverter mappingMongoConverter = super.mappingMongoConverter();
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mongoTemplate = new MongoTemplate(mongoDbFactory(), mappingMongoConverter);*/
        return mongoTemplate;
    }

    /**
     * 使用MongoDbFactory以及MappingMongoConverter初始化MongoTemplate
     * @author: yujun
     * @date: 2019/10/24
     * @param
     * @return: {@link org.springframework.data.mongodb.core.MongoTemplate}
     * @exception:
    */
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

    @Bean("transactionTemplate")
    public MongoTemplate transactionTemplate() throws Exception {
        MongoTemplate mongoTemplate = null;
        MappingMongoConverter mappingMongoConverter = super.mappingMongoConverter();
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(mongoClient(), textDatabase), mappingMongoConverter);
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);
        return mongoTemplate;
    }

    /**
     * 用于事务控制
     * 1.可用于初始化TransactionTemplate
     * 2.可用于注册MongoTransactionManager至application context中，使用spring的托管事务功能
     * @author: yujun
     * @date: 2019/10/24
     * @param
     * @return: {@link org.springframework.data.mongodb.MongoTransactionManager}
     * @exception:
    */
    @Bean(name = "mongoTransactionManager")
    public MongoTransactionManager mongoTransactionManager() {
        return new MongoTransactionManager(mongoDbFactory());
    }

    protected String getDatabaseName() {
        return this.database;
    }
}

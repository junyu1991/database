package com.yujun.database.mongodb;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.yujun.database.model.VideoInfo;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- admin 2019/10/14
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */
public class MongoUsageTest {

    @Autowired
    @Qualifier("mongoClientFactoryBean")
    private Mongo mongoClientFactoryBean;
    @Autowired
    @Qualifier("mongoClient")
    private MongoClient mongoClient;

    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    public void insertMovie(List<VideoInfo> videos) {
        MongoDatabase movie = mongoClient.getDatabase("movie");
        movie.createCollection("movie");
        MongoOperations mongoOperations = new MongoTemplate(new SimpleMongoDbFactory(mongoClient, "movie"));
        mongoOperations.insert(videos);
        mongoTemplate.insert(videos, "videos");
    }

    public List<VideoInfo> findVideosByVideoName(String videoName) {
        BasicQuery query = new BasicQuery("{filename:+" + videoName +"}");
        query.setSortObject(new Document());
        Query filename = query(where("filename").all(videoName));
        List<VideoInfo> videos = mongoTemplate.find(query, VideoInfo.class, "videos");
        return videos;
    }
}

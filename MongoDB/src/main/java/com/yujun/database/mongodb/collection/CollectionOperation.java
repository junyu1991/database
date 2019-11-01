package com.yujun.database.mongodb.collection;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Set;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/11/1
 * </li>
 * </p>
 *
 * <b>类说明：MongoDB collection操作示例</b>
 * <p>
 *
 * </p>
 */
@ComponentScan(value = {"com.yujun.database.mongodb"})
public class CollectionOperation {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 获取MongoTemplate操作的database下的所有collection name
     * @author: yujun
     * @date: 2019/11/1
     * @param
     * @return: {@link java.util.Set<java.lang.String>}
     * @exception:
    */
    public Set<String> getAllCollectionName() {
        return mongoTemplate.getCollectionNames();
    }

    /**
     * 获取MongoTemplate操作的database下的指定Collection
     * @author: yujun
     * @date: 2019/11/1
     * @param collectionName
     * @return: {@link com.mongodb.client.MongoCollection<org.bson.Document>}
     * @exception:
    */
    public MongoCollection<Document> getCollection(String collectionName) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
        return collection;
    }

    /**
     * 判断MongoTemplate操作的database中是否存在指定collection
     * @author: yujun
     * @date: 2019/11/1
     * @param collectionName
     * @return: {@link boolean}
     * @exception:
    */
    public boolean existsCollection(String collectionName) {
        return mongoTemplate.collectionExists(collectionName);
    }

    /**
     * 创建collection
     * @author: yujun
     * @date: 2019/11/1
     * @param collectionName
     * @return:
     * @exception:
    */
    public void createCollection(String collectionName) {
        mongoTemplate.createCollection(collectionName);
    }

    /**
     * 删除指定collection
     * @author: yujun
     * @date: 2019/11/1
     * @param collectionName
     * @return:
     * @exception:
    */
    public void dropCollection(String collectionName) {
        mongoTemplate.dropCollection(collectionName);
    }

}

package com.yujun.database.mongodb.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.*;

import java.util.List;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/11/1
 * </li>
 * </p>
 *
 * <b>类说明：演示MongoDB索引操作</b>
 * <p>
 *
 * </p>
 */
@ComponentScan(value = {"com.yujun.database.mongodb"})
public class IndexOperation {

    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    /**
     * 创建索引示例，此例演示了创建一般常用索引，MongoDB也支持创建GeoSpatialIndex以及TextIndexDefinition
     * @author: yujun
     * @date: 2019/11/1
     * @param collection
     * @param indexName
     * @return:
     * @exception:
    */
    public void createIndex(String collection, String indexName){
        mongoTemplate.indexOps(collection).ensureIndex(new Index().on(indexName, Sort.Direction.ASC));
    }

    /**
     * 获取指定collection中的索引信息
     * @author: yujun
     * @date: 2019/11/1
     * @param collection
     * @return: {@link java.util.List<org.springframework.data.mongodb.core.index.IndexInfo>}
     * @exception:
    */
    public List<IndexInfo> getIndexInfo(String collection) {
        List<IndexInfo> indexInfo = mongoTemplate.indexOps(collection).getIndexInfo();
        return indexInfo;
    }

    /**
     * 删除指定collection中的指定index
     * @author: yujun
     * @date: 2019/11/1
     * @param collection
     * @param indexName
     * @return:
     * @exception:
    */
    public void dropIndex(String collection, String indexName) {
        mongoTemplate.indexOps(collection).dropIndex(indexName);
    }

    /**
     * 删除指定collection中的所有索引信息
     * @author: yujun
     * @date: 2019/11/1
     * @param collection
     * @return:
     * @exception:
    */
    public void dropAllIndex(String collection) {
        mongoTemplate.indexOps(collection).dropAllIndexes();
    }
    /*
    public void resetIndexCache(String collection) {
        mongoTemplate.indexOps(collection)
    }*/

}

package com.yujun.database.mongodb.file.dao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.annotation.Annotation;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- admin 2019/10/16
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */
@Getter
@ComponentScan(basePackages = {"com.yujun.database.mongodb"})
public class BasicDao {

    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    public<T> T findById(Object ID, Class<T> clazz) {
        return mongoTemplate.findById(ID, clazz);
    }

}

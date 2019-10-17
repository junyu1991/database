package com.yujun.filemanager;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.yujun.database.model.FileInfo;
import com.yujun.thread.ScanFileThread;
import com.yujun.util.FileUtil;
import com.yujun.util.FindFileVisitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- admin 2019/10/15
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */
@ComponentScan(basePackages = {"com.yujun"})
@PropertySource(value = "classpath:application.yml")
@Slf4j
@Configuration
public class FileManage {

    @Autowired
    @Qualifier("mongoClient")
    private MongoClient mongoClient;

    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Value("${file.scanpath}")
    private String scanPath[];

    @Value("${file.mongo.database}")
    private String database;

    @Value("${file.mongo.collection}")
    private String collectionName;

    @Autowired
    @Qualifier("javaMongoFindFileVisitor")
    private FindFileVisitor findFileVisitor;

    public void startScan() {
        for(String path : this.scanPath) {
//            FileUtil.walkFile(path, findFileVisitor);
            new ScanFileThread(path, findFileVisitor, "scan["+path+"]-thread").start();
        }
    }

    @Bean(name="fileManager")
    public FileManage fileManage() {
        log.info("Start scanning");
        return this;
    }
}

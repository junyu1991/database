package com.yujun.database.mongodb.session;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.yujun.database.model.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/10/22
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 * 主要演示如何使用MongoDB client session进行操作，以及基于session的事务
 * </p>
 */
@Slf4j
@ComponentScan(value = {"com.yujun.database.mongodb"})
public class SessionUsage {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private MongoTransactionManager mongoTransactionManager;
    
    /**
     * 使用mongo session进行简单操作
     * @author: yujun
     * @date: 2019/10/22
     * @param fileInfo
     * @return: 
     * @exception: 
    */
    public void basicUsage(FileInfo fileInfo) {
        ClientSessionOptions sessionOptions = ClientSessionOptions.builder()
                .causallyConsistent(true)
                .build();
        ClientSession session = mongoClient.startSession(sessionOptions);

        mongoTemplate.withSession(()->session)
                .execute(action -> {
                    Query query = new Query();
                    query.addCriteria(Criteria.where("filename").is(fileInfo.getFilename()));
                    FileInfo fileInfo1 = action.findOne(query, FileInfo.class);
                    action.insert(fileInfo);
                    return fileInfo1;
                });
        session.close();
    }

    /**
     * 使用session实现MongoDB的事务控制
     * @author: yujun
     * @date: 2019/10/22
     * @param fileInfos
     * @return:
     * @exception:
    */
    public void transactionWithSession(List<FileInfo> fileInfos) {
        ClientSessionOptions sessionOptions = ClientSessionOptions.builder()
                .causallyConsistent(true)
                .build();
        ClientSession session = mongoClient.startSession(sessionOptions);
        mongoTemplate.withSession(()->session)
                .execute(action -> {
                    session.startTransaction();
                    try {
                        action.insert(fileInfos, FileInfo.class);
                        session.commitTransaction();
                    } catch (RuntimeException e) {
                        log.error("Insert fileinfos exception.", e);
                        session.abortTransaction();
                    }
                    return null;
                });
        session.close();
    }

    /**
     * 使用TransactionTemplate进行事务控制
     * @author: yujun
     * @date: 2019/10/23
     * @param fileInfos
     * @return:
     * @exception:
    */
    public void workWithTransactionTemplate(List<FileInfo> fileInfos) {
        //在运行过程中使用setSessionSynchronization()方法修改MongoTemplate状态会导致线程安全问题
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ALWAYS);

        //创建TransactionTemplate对象，需要使用PlatformTransactionManager，目前暂时不清楚PlatformTransactionManager的初始化
        TransactionTemplate transactionTemplate = new TransactionTemplate(mongoTransactionManager);

        //使用transactionTemplate执行操作时，事务控制是自动被注册好的
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                mongoTemplate.insert(fileInfos);
            }
        });
    }


}

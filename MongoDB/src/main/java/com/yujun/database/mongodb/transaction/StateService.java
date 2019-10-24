package com.yujun.database.mongodb.transaction;

import com.yujun.database.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/10/23
 * </li>
 * </p>
 *
 * <b>类说明：事务控制演示</b>
 * <p>
 *
 * </p>
 */
@Component
public class StateService {

    @Autowired
    @Qualifier("transactionTemplate")
    private MongoTemplate transactionTemplate;

    @Transactional
    public void insertFiles(List<FileInfo> files) {
        transactionTemplate.insert(files);
    }
}

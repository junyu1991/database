package com.yujun.database.mongodb;

import com.mongodb.WriteConcern;
import org.springframework.data.mongodb.core.MongoAction;
import org.springframework.data.mongodb.core.WriteConcernResolver;

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
public class TestWriteConcernResolver implements WriteConcernResolver {
    public WriteConcern resolve(MongoAction action) {
        action.getEntityType().getSimpleName().contains("");
        action.getMongoActionOperation().name();
        return WriteConcern.ACKNOWLEDGED;
    }
}

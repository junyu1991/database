package com.yujun.database.mongodb.transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    void someFunction() {

    }
}

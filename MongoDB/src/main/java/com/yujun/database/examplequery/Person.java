package com.yujun.database.examplequery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/10/21
 * </li>
 * </p>
 *
 * <b>类说明：用于演示Example查询</b>
 * <p>
 *
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    private String id;
    private String firstname;
    private String lastname;
    private Address address;
}

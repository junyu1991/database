package com.yujun.database.mongodb.querybyexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/10/21
 * </li>
 * </p>
 *
 * <b>类说明：Example操作示例</b>
 * <p>
 *  ExampleMatcher主要有三部分构成：
 *  1. probe，实例类，用于保存属性字段，本例中是Person
 *  2. ExampleMatcher，主要用于组成查询条件，可用于多个Example
 *  3. Example，包含probe以及ExampleMatcher，将这两部分组合起来
 * </p>
 */
public class ExampleUsage {

    @Autowired
    private PersonQuerybyExampleExecutor personQuerybyExampleExecutor;

    /**
     * 演示如何创建Example
     * @author: yujun
     * @date: 2019/10/21
     * @param firstName
     * @param lastName
     * @return: {@link Example<  Person >}
     * @exception:
    */
    public Example<Person> makeUpPersonExample(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstname(firstName);
        person.setLastname(lastName);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("lastname")
                .withIncludeNullValues();

        Example<Person> example = Example.of(person, matcher);
        return example;
    }

    /**
     * 构建ExampleMatcher
     * @author: yujun
     * @date: 2019/10/21
     * @param
     * @return: {@link ExampleMatcher}
     * @exception:
    */
    public ExampleMatcher configExampleMatcher() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("firstname", endsWith())
                .withMatcher("lastname", startsWith().ignoreCase());
        return matcher;
    }

    /**
     * 使用lambda构建ExampleMatcher
     * @author: admin
     * @date: 2019/10/21
     * @param
     * @return: {@link ExampleMatcher}
     * @exception:
     */
    public ExampleMatcher configExampleMatcherWithLambda() {
        //使用matching()表示必须要满足所有的条件，使用matchingAny()则是满足条件之一即可
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("firstname", match -> match.endsWith())
                .withMatcher("firstname", match -> match.startsWith());
        return matcher;
    }

    /**
     * 使用Example进行查询
     * @author: admin
     * @date: 2019/10/21
     * @param probe
     * @return: {@link List<  Person >}
     * @exception:
    */
    public List<Person> findPeople(Person probe) {
        return (List<Person>) personQuerybyExampleExecutor.findAll(Example.of(probe));
    }

}

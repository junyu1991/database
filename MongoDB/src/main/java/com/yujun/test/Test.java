package com.yujun.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class Test {

    public static void main(String[] args) {
        String pattern = "\\.(java|class|jsp|war)$";
        Pattern compile = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = compile.matcher("test.jaVa");
        if(matcher.find()) {
            System.out.println("match");
        }
    }
}

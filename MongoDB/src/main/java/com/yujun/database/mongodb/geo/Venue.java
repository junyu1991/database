package com.yujun.database.mongodb.geo;/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- admin 2019/10/18
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

/**
 @author admin
 @version 1.0.0
 @date 2019/10/18 9:54
 @description TODO
 **/
@Getter
@Setter
@Document(collection = "geo-collection")
public class Venue {
    @Id
    private String id;
    private String name;
    private double[] location;

    public Venue(String name, double[] location) {
        this.name = name;
        this.location = location;
    }

    public Venue(String name, double x, double y) {
        this.name = name;
        this.location = new double[]{x,y};
    }

    public String toString() {
        return "Venue [id=" + id +", name=" + name +",location="
                + Arrays.toString(location) + "]";
    }
}

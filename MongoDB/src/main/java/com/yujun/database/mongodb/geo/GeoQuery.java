package com.yujun.database.mongodb.geo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
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
@ComponentScan("com.yujun.database.mongodb")
public class GeoQuery {

    @Autowired
    @Qualifier("geoMongoTemplate")
    private MongoTemplate mongoTemplate;

    /**
     * 查询地理位置在指定圆球内的场馆数据。
     * @author: yujun
     * @date: 2019/10/18
     * @param x x坐标
     * @param y y坐标
     * @param r 半径R
     * @return:
     * @exception:
    */
    public List<Venue> queryVenueInCircle(Double x, Double y, Double r) {
        Circle circle = new Circle(x, y, r);
        Query query = new Query();
        query.addCriteria(Criteria.where("location").withinSphere(circle));
        //查询在指定圆内的场馆数据
        //query.addCriteria(Criteria.where("location").within(circle));
        return this.mongoTemplate.find(query,Venue.class);
    }
    /**
     * 查询地理位置在指定框内的场馆数据
     * @author: admin
     * @date: 2019/10/18
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @return: {@link List< Venue>}
     * @exception:
    */
    public List<Venue> queryVenueInBox(Double x, Double y, Double x1, Double y1) {
        Box box = new Box(new Point(x, y), new Point(x1, y1));
        Query query = Query.query(Criteria.where("location").within(box));
        return this.mongoTemplate.find(query, Venue.class);
    }
    /**
     * 根据点的位置查询场馆数据
     * @author: admin
     * @date: 2019/10/18
     * @param x
     * @param y
     * @param distance
     * @return: {@link List< Venue>}
     * @exception:
    */
    public List<Venue> queryVenueNearByPoint(Double x, Double y, Double distance) {
        Point point = new Point(x, y);
        Query query = new Query();
        //根据场馆距离点的最小距离查询
        query.addCriteria(Criteria.where("location").near(point).minDistance(distance));
        //根据场馆距离点的最大距离查询
        query.addCriteria(Criteria.where("location").near(point).maxDistance(distance));
        //与near方法相同，nearSphere是查询的球体内的数据
        query.addCriteria(Criteria.where("location").nearSphere(point).minDistance(distance));
        return this.mongoTemplate.find(query, Venue.class);
    }

}

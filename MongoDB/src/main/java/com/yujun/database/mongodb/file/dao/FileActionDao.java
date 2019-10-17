package com.yujun.database.mongodb.file.dao;

import com.sun.istack.internal.NotNull;
import com.yujun.database.model.FileInfo;
import com.yujun.database.model.VideoInfo;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/10/16
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */
public class FileActionDao extends BasicDao {

    /**
     * 根据文件名查询文件
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO
     * @param fileName
     * @return: {@link List< FileInfo>}
     * @exception:
    */
    public List<FileInfo> getFileInfoByFileName(String fileName) {
        List<FileInfo> filename = this.getMongoTemplate().find(Query.query(Criteria.where("filename").is(fileName)), FileInfo.class);
        return filename;
    }

    /**
     *
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO
     * @param fileSize
     * @param symbol
     * @return: {@link List< FileInfo>}
     * @exception:
    */
    public List<FileInfo> getFileInfoByFileSize(Long fileSize, char symbol) {
        List<FileInfo> result = null;
        switch (symbol) {
            case '>':
                result = this.getMongoTemplate().find(Query.query(Criteria.where("fileSize").gt(fileSize)), FileInfo.class);
                break;
            case '<':
                result = this.getMongoTemplate().find(Query.query(Criteria.where("fileSize").lt(fileSize)), FileInfo.class);
                break;
            case '=':
                result = this.getMongoTemplate().find(Query.query(Criteria.where("fileSize").is(fileSize)), FileInfo.class);
                break;
            default:
                break;
        }
        return result;
    }

    /** 
     * 根据文件名更新文件信息
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO 
     * @param fileInfo 用于存储更新需要的文件信息
     * @param updateParam 需要更新的字段
     * @return: 
     * @exception: 
    */
    @Deprecated
    public void updateAllFileSizeByFileName(FileInfo fileInfo, @NotNull String ...updateParam) {
        Query query = Query.query(Criteria.where("filename").is(fileInfo.getFilename()));
        Update update = new Update();
        Method[] methods = fileInfo.getClass().getMethods();
        for(String param : updateParam) {
            String methodName = "get" + param;
            for(Method m : methods) {
                if(m.getName().equalsIgnoreCase(methodName)) {
                    Object invoke = null;
                    try {
                        invoke = m.invoke(fileInfo, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    update.set(param, invoke);
                }
            }
        }
        this.getMongoTemplate().updateMulti(query, update, "file");
    }

    /** 
     * 批量更新符合条件的FileInfo
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO 
     * @param updateParam
     * @param filter
     * @return: 
     * @exception: 
    */
    public void updateAllFileInfo(Map<String, Object> updateParam, Map<String, Object> filter) {
        Query query = this.makeQuery(filter);
        Update update = this.makeUpdate(updateParam);
        this.getMongoTemplate().updateMulti(query, update, "file");
    }

    /**
     * 根据指定条件生成查询条件
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO
     * @param filter
     * @return: {@link Query}
     * @exception:
    */
    private Query makeQuery(Map<String, Object> filter) {
        Query query = new Query();
        for(String key : filter.keySet()) {
            Criteria criteria = new Criteria(key);
            criteria.is(filter.get(key));
            query.addCriteria(criteria);
        }
        return query;
    }

    /**
     * 根据指定条件生成更新
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO
     * @param updateParam
     * @return: {@link Update}
     * @exception:
    */
    private Update makeUpdate(Map<String, Object> updateParam) {
        Update update = new Update();
        for(String key : updateParam.keySet()) {
            update.set(key, updateParam.get(key));
        }
        return update;
    }

    /** 
     * 若数据库中不存在符合筛选条件的对象则进行插入操作，若存在则进行更新操作
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO 
     * @param updateParam
     * @param filter
     * @return: 
     * @exception: 
    */
    public void upsertFileInfo(Map<String, Object> updateParam, Map<String, Object> filter) {
        Update update = this.makeUpdate(updateParam);
        Query query = this.makeQuery(filter);
        this.getMongoTemplate().upsert(query, update, FileInfo.class,"file");
    }

    /**
     * 查找并更新FileInfo
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO
     * @param updateParam
     * @param filter
     * @return: {@link FileInfo}
     * @exception:
    */
    public FileInfo findAndModifyFileInfo(Map<String, Object> updateParam, Map<String, Object> filter) {
        Update update = this.makeUpdate(updateParam);
        Query query = this.makeQuery(filter);
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options();
        //使用findAndModifyOptions.returnNew(true)使返回的结果为更新后的结果
        FileInfo file = this.getMongoTemplate().findAndModify(query, update, findAndModifyOptions.returnNew(true), FileInfo.class, "file");
        return file;
    }

    /**
     * 使用VideoInfo替换符合条件的FileInfo
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO
     * @param videoInfo
     * @param filter
     * @return: {@link VideoInfo}
     * @exception:
    */
    public VideoInfo findAndReplace(VideoInfo videoInfo, Map<String, Object> filter) {
        Query query = this.makeQuery(filter);
        Optional<VideoInfo> andReplace = this.getMongoTemplate().update(FileInfo.class)
                .matching(query).replaceWith(videoInfo)
                .withOptions(FindAndReplaceOptions.options().upsert())
                .withOptions(FindAndReplaceOptions.options().returnNew())
                .as(VideoInfo.class)
                .findAndReplace();
        return andReplace.get();
    }

    /** 
     * 删除指定fileInfo，主要根据FileInfo中的_id（主键）查询删除
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO 
     * @param fileInfo
     * @return: 
     * @exception: 
    */
    public void removeFileInfo(FileInfo fileInfo) {
        this.getMongoTemplate().remove(fileInfo);
    }

    /**
     * 从collectionName集合中批量删除符合筛选条件的所有数据
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO
     * @param filter
     * @param collectionName
     * @return:
     * @exception:
    */
    public void removeFileInfo(Map<String, Object> filter, String collectionName) {
        Query query = this.makeQuery(filter);
        this.getMongoTemplate().remove(query, collectionName);
    }

    /**
     * 从collectionName集合中逐条删除符合筛选条件的所有数据
     * @author: yujun
     * @date: 2019/10/16
     * @description: TODO
     * @param filter
     * @param collectionName
     * @return:
     * @exception:
    */
    public void removeFileInfoOneByOne(Map<String, Object> filter, String collectionName) {
        Query query = this.makeQuery(filter);
        this.getMongoTemplate().findAllAndRemove(query, collectionName);
    }
}

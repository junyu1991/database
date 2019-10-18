package com.yujun.database.mongodb.text;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;

import java.util.List;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/10/18
 * </li>
 * </p>
 *
 * <b>类说明： mongodb text search示例代码</b>
 * <p>
 *
 * </p>
 */
public class TextSearch {

    @Autowired
    @Qualifier(value = "textMongoTemplate")
    private MongoTemplate textMongoTemplate;

    /**
     * 使用TextQuery进行全文搜索，使用TextQuery之前需要为搜索的字段添加text索引
     * db.collection.createIndex({textColumn:'text'},{weights:{textColumn:3}})
     * @author: yujun
     * @date: 2019/10/18
     * @param queryWords
     * @return: {@link java.util.List<org.springframework.data.mongodb.core.mapping.Document>}
     * @exception:
    */
    public List<Document> queryMatchManyText(String ...queryWords) {
        TextCriteria criteria = new TextCriteria().matchingAny(queryWords);
        TextQuery query = TextQuery.queryText(criteria).sortByScore();
        List<Document> documents = this.textMongoTemplate.find(query, Document.class, "text_collection");
        return documents;
    }

    /**
     * 使用TextQuery进行全文搜索
     * @author: admin
     * @date: 2019/10/18
     * @param queryWord
     * @return: {@link List< Document>}
     * @exception:
    */
    public List<Document> queryText(String queryWord) {
        TextCriteria criteria = new TextCriteria().matching(queryWord);
        TextQuery query = TextQuery.queryText(criteria).sortByScore();
        List<Document> documents = this.textMongoTemplate.find(query, Document.class, "text_collection");
        return documents;
    }

    /**
     * 全文搜索，排除包含有指定字符串的结果
     * @author: admin
     * @date: 2019/10/18
     * @param queryWord
     * @param excludeString
     * @return: {@link List< Document>}
     * @exception:
    */
    public List<Document> queryTextAndExcludeString(String queryWord, String excludeString) {
        //使用notMatching可排除指定字符串
        TextCriteria criteria = new TextCriteria().matching(queryWord).notMatching(excludeString);
        //也可使用 - 来进行排除,caseSensitive()用于设置是否大小写敏感
        criteria = new TextCriteria().matching(queryWord).matching("-" + excludeString).caseSensitive(true);
        TextQuery query = TextQuery.queryText(criteria).sortByScore();
        List<Document> documents = this.textMongoTemplate.find(query, Document.class, "text_collection");
        return documents;
    }

}

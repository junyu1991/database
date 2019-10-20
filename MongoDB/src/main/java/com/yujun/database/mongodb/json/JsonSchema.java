package com.yujun.database.mongodb.json;

import com.yujun.database.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.data.mongodb.core.validation.Validator;

import static org.springframework.data.mongodb.core.query.Criteria.matchingDocumentStructure;
import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.object;
import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.string;

/**
 * @author hunter
 * @version 1.0.0
 * @date 10/19/19 2:14 PM
 * @description TODO
 **/
public class JsonSchema {

    @Autowired
    @Qualifier("jsonMongoTemplate")
    private MongoTemplate jsonMongoTemplate;

    /**
     * 创建json协议示例
     * {
     *   "type": "object",
     *   "required": [ "firstname", "lastname" ],
     *   "properties": {
     *     "firstname": {
     *       "type": "string",
     *       "enum": [ "luke", "han" ]
     *     },
     *     "address": {
     *       "type": "object",
     *       "properties": {
     *         "postCode": { "type": "string", "minLength": 4, "maxLength": 5 }
     *       }
     *     }
     *   }
     * }
     * @author:
     * @date: 10/19/19
     * @description: TODO
     * @param
     * @return: {@link MongoJsonSchema}
     * @exception:
    */
    public MongoJsonSchema createJsonSchema() {
        return MongoJsonSchema.builder()
                .required("firstname","lastname")
                .properties(string("firstname").possibleValues("luke","han"),
                        object("address")
                                .properties(string("postCode").minLength(4).maxLength(5)))
                .build();
    }

    /**
     * 使用Json schema创建collection
     * @author:
     * @date: 10/19/19
     * @description: TODO
     * @param collectionName
     * @param mongoJsonSchema
     * @return:
     * @exception:
    */
    public void createCollectionWithJsonSchema(String collectionName, MongoJsonSchema mongoJsonSchema) {
        this.jsonMongoTemplate.createCollection(collectionName, CollectionOptions.empty().schema(mongoJsonSchema));
    }

    /**
     * 在查询时使用MongoJsonSchema
     * @author:
     * @date: 10/19/19
     * @description: TODO
     * @param collectionName
     * @param mongoJsonSchema
     * @return:
     * @exception:
    */
    public void queryWithJsonSchema(String collectionName, MongoJsonSchema mongoJsonSchema) {
        Query query = Query.query(matchingDocumentStructure(mongoJsonSchema));
        //添加其他查询条件
        this.jsonMongoTemplate.find(query, FileInfo.class, collectionName);
    }

    /*public void encryptFiledsUsingMongoJsonSchema(String filedName, String algorithm, String keyId) {
        MongoJsonSchema schema = MongoJsonSchema.builder().properties(
                encrypted(string(filedName))
        ).build();
    }*/
}

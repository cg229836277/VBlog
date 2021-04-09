package org.sang.mongodb.repository;

import lombok.extern.slf4j.Slf4j;
import org.sang.mongodb.dataobject.ArticleDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class BaseRepository<T> {

    protected final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    protected MongoTemplate mongoTemplate;

    private Class<?> getParseClass(String collectionName) {
        Class<?> childClass = null;
        switch (collectionName) {
            case ArticleDO.TYPE_ARTICLE:
                childClass = ArticleDO.class;
                break;
        }
        log.info("childClass type is {}", childClass);
        return childClass;
    }

    public T insert(T dataDao) {
        return mongoTemplate.insert(dataDao);
    }

    public T update(T dataDao, String collectionName) {
        return (T) mongoTemplate.save(dataDao, collectionName);
    }

    //db.blog.find({"type":"blog"})
    public List<T> getByType(String type, String collectionName) {
        Class clazz = getParseClass(collectionName);
        return mongoTemplate.find(new Query(Criteria.where("type").is(type)), clazz, collectionName);
    }

    //db.phone_usage.find({ "start_time" : { "$gte" : "1612454400000"}, "end_time" : { "$lte" : "1612540799999"},"user_name":"chuckchan"}).sort({"date":-1})
}

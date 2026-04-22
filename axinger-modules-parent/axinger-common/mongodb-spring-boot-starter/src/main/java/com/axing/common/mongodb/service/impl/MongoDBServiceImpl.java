package com.axing.common.mongodb.service.impl;

import com.axing.common.mongodb.service.MongoService;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.List;

/**
 * mongodb数据泛型dao类
 */
@Slf4j
public class MongoDBServiceImpl implements MongoService {

    private MongoTemplate mongoTemplate;

    public MongoDBServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 根据主键id返回对象
     *
     * @param id 唯一标识
     * @return T 对象
     */
    @Override
    public <T> T findById(Class<T> entityClass, String id) {
        try {
            return this.mongoTemplate.findById(id, entityClass);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * 根据类获取全部的对象列表
     *
     * @param entityClass 返回类型
     * @return List<T> 返回对象列表
     */
    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        return this.mongoTemplate.findAll(entityClass);
    }

    /**
     * 删除一个对象
     *
     * @param obj 要删除的Mongo对象
     */
    @Override
    public void remove(Object obj) {
        this.mongoTemplate.remove(obj);
    }

    /**
     * 删除对象
     *
     * @param key         是属性
     * @param value       是属性对应得值
     * @param entityClass 查询的对象
     */
    @Override
    public <T> T findAndRemove(String key, Object value, Class<T> entityClass) {
        Query query = new Query(Criteria.where(key).is(value));
        return this.mongoTemplate.findAndRemove(query, entityClass);
    }

    /**
     * 添加对象
     *
     * @param obj 要添加的Mongo对象
     */
    @Override
    public <T> T add(T obj) {
        return this.mongoTemplate.insert(obj);
    }

    /**
     * 批量添加对象
     *
     * @param objectsToSave 要添加的Mongo对象
     */
    @Override
    public <T> Collection<T> batchAdd(Collection<T> objectsToSave) {
        return this.mongoTemplate.insertAll(objectsToSave);
    }

    /**
     * 根据对象中的某个属性进行查询
     *
     * @param key         是属性
     * @param value       是属性对应得值
     * @param entityClass 查询的对象
     */
    @Override
    public <T> List<T> findByQuery(String key, Object value, Class<T> entityClass) {
        Query query = new Query(Criteria.where(key).is(value));
        return this.mongoTemplate.find(query, entityClass);
    }

    /**
     * 根据对象中的某个属性进行查询
     *
     * @param key         是属性
     * @param value       是属性对应得值
     * @param entityClass 查询的对象
     */
    @Override
    public <T> T findOneByQuery(String key, Object value, Class<T> entityClass) {
        Query query = new Query(Criteria.where(key).is(value));
        return this.mongoTemplate.findOne(query, entityClass);
    }

    /**
     * 修改对象
     *
     * @param obj 要修改的Mongo对象
     */
    @Override
    public void saveOrUpdate(Object obj) {
        this.mongoTemplate.save(obj);
    }

    @Override
    public <T> T save(T objectToSave, String collectionName) {
        return this.mongoTemplate.save(objectToSave, collectionName);
    }

    /**
     * @param entityClass 查询对象
     * @param query       查询条件
     * @return
     */
    public <T> Long count(Class<T> entityClass, Query query) {

        return this.mongoTemplate.count(query, entityClass);
    }


    /**
     * 根据对象中的某个属性进行删除
     *
     * @param key         是属性
     * @param value       是属性对应得值
     * @param entityClass 查询的对象
     */
    @Override
    public <T> List<T> removeByQuery(String key, Object value, Class<T> entityClass) {
        Query query = new Query(Criteria.where(key).is(value));
        return this.mongoTemplate.findAllAndRemove(query, entityClass);
    }

    @Override
    public <T> List<T> removeByQuery(Query query, Class<T> entityClass) {
        return this.mongoTemplate.findAllAndRemove(query, entityClass);
    }

    /**
     * 根据多条件进行查询
     *
     * @param query       是查询条件
     * @param entityClass 查询的对象
     */
    @Override
    public <T> List<T> findList(Query query, Class<T> entityClass) {
        return (List<T>) this.mongoTemplate.find(query, entityClass);
    }

    @Override
    public <T> T findOne(Query query, Class<T> entityClass) {
        return (T) this.mongoTemplate.findOne(query, entityClass);
    }

    /**
     * 改动符合条件的第一条记录
     *
     * @param query
     * @param update
     * @param entityClass
     * @return
     */
    @Override
    public UpdateResult updateFirst(Query query, Update update, Class<?> entityClass) {
        return mongoTemplate.updateFirst(query, update, entityClass);
    }
}

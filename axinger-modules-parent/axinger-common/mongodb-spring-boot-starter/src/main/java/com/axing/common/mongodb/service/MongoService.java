package com.axing.common.mongodb.service;

import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.List;

public interface MongoService {

    <T> T findById(Class<T> entityClass, String id);

    <T> List<T> findAll(Class<T> entityClass);

    void remove(Object obj);

    /**
     * @param obj
     * @param <T>
     * @return
     */
    <T> T add(T obj);

    <T> Collection<T> batchAdd(Collection<T> objectsToSave);


    <T> List<T> findByQuery(String key, Object value, Class<T> entityClass);

    <T> T findOneByQuery(String key, Object value, Class<T> entityClass);

    void saveOrUpdate(Object obj);

    /**
     * 保持数据到指定表
     *
     * @param objectToSave
     * @param collectionName
     * @param <T>
     * @return
     */
    <T> T save(T objectToSave, String collectionName);

    <T> T findAndRemove(String key, Object value, Class<T> entityClass);

    <T> List<T> removeByQuery(String key, Object value, Class<T> entityClass);

    <T> List<T> removeByQuery(Query query, Class<T> entityClass);

    /**
     * 根据多条件进行查询
     *
     * @param query       是查询条件
     * @param entityClass 查询的对象
     */
    <T> List<T> findList(Query query, Class<T> entityClass);

    <T> T findOne(Query query, Class<T> entityClass);

    /**
     * 改动符合条件的第一条记录
     *
     * @param query
     * @param update
     * @param entityClass
     * @return
     */
    UpdateResult updateFirst(Query query, Update update, Class<?> entityClass);

}

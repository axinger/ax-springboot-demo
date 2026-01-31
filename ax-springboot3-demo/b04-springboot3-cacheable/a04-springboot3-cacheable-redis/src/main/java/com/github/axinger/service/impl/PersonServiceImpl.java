package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.domain.PersonEntity;
import com.github.axinger.mapper.PersonMapper;
import com.github.axinger.service.PersonService;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author cepai
 * @description 针对表【person】的数据库操作Service实现
 * @createDate 2023-12-19 14:55:09
 */
@Service
@CacheConfig(cacheNames = "school:person", keyGenerator = "keyGenerator")
//统一配置该 UserMapper 使用的缓存名为 users ,方法中使用了cacheNames,则类的方法上使用cacheNames将不会生效
public class PersonServiceImpl extends ServiceImpl<PersonMapper, PersonEntity>
        implements PersonService {

    @Override
    // @Cacheable(value 与 @CacheConfig(cacheNames  只能一个有效
    //二者选其一,可以使用value上的信息，来替换类上cacheNames的信息
//    @Cacheable(value = "test2",key = "#id")
    @Cacheable(key = "#id", unless = "#result==null")
    //如果有数据，则直接返回缓存数据；若没有数据，调用方法并将方法返回值放到缓存中
    public PersonEntity findPersonEntity(Integer id) {
        System.err.println("执行findPersonEntity===============");
        return this.lambdaQuery()
                .eq(PersonEntity::getId, id)
                .one();
    }


    @Override
    //将方法的返回值放到缓存中
    @CachePut(key = "#person.id")
    public PersonEntity savePersonEntity(PersonEntity person) {
        return null;
    }

    @Override
    //将方法的返回值放到缓存中
//    @CachePut(key = "#person.id")
//    @Caching(
//            cacheable = {
//                    @Cacheable(value = "emp", key = "#person.id")
//            },
//            put = {                  // 更新缓存可以通过id，email或者lastName进行key值查找。
//                    @CachePut(key = "#result.id"),
//                    @CachePut(key = "#result.name"),
//            }
//    )
    @CachePut(key = "#result.id")
//    @CachePut(key = "#result.id")
    public PersonEntity savePersonNullId(PersonEntity person) {
        person.setId(null);
        save(person);
        return person;
    }


    /**
     * 对获取批量对象的方法直接加上 @Cacheable 或 @CacheResult，
     * 则会使用【对象集合参数】整体生成一个缓存 key，将返回的 Map 或 List 整体作为一个缓存值。
     */
    @Override
    //将方法的返回值放到缓存中
//    @CachePut(key = "#result.stream().map(val->val.getId()).toList()")
//    @Cacheable(value = "list")
    @CachePut(value = "list", key = "#root.caches")
    public List<PersonEntity> savePersonList(List<PersonEntity> personList) {
        this.saveBatch(personList);
        return personList;
    }

    @Override
    @CachePut(key = "#person.id")
    public PersonEntity updatePersonEntity(PersonEntity person) {
        Assert.notNull(person.getId(), "id不能为空");
        this.updateById(person);
        return person;
    }

    @Override
    //    @CacheEvict(value = "user", key = "#userId") // CacheEvict 删除
    @CacheEvict(key = "#id")
    //将一条或多条数据从缓存中删除
    public void deletePersonEntity(Integer id) {
        this.removeById(id);
    }

    @Override
    @Caching(
            cacheable = {
                    @Cacheable(value = "emp", key = "#id")
            },
            put = {                  // 更新缓存可以通过id，email或者lastName进行key值查找。
                    @CachePut(value = "emp", key = "#result.id"),
                    @CachePut(value = "emp", key = "#result.name"),
            }
    )
    public PersonEntity getLastName(Integer id) {
        return null;
    }
}





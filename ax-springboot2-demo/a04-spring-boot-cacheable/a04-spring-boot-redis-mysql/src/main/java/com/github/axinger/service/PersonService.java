package com.github.axinger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.domain.PersonEntity;

import java.util.List;

/**
 * @author cepai
 * @description 针对表【person】的数据库操作Service
 * @createDate 2023-12-19 14:55:09
 */
public interface PersonService extends IService<PersonEntity> {

    PersonEntity findPersonEntity(Integer id);

    PersonEntity savePersonEntity(PersonEntity person);

    PersonEntity savePersonNullId(PersonEntity person);

    List<PersonEntity> savePersonList(List<PersonEntity> personList);

    PersonEntity updatePersonEntity(PersonEntity person);

    void deletePersonEntity(Integer id);

    PersonEntity getLastName(Integer id);
}

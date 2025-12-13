package com.xing.repository;


import com.xing.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {


    Person getByName(String name);

    List<Person> findListByName(String name);

    List<Person> findByNameLike(String name);
}

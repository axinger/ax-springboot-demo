package com.xing.repository;

import com.xing.entity.Dog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName DogRepository.java
 * @description TODO
 * @createTime 2022年05月06日 22:38:00
 */
@Repository
public interface DogRepository extends MongoRepository<Dog, String> {
}

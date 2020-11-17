package com.example.punkapi;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserBeerRepository extends CrudRepository<UserBeer, Integer> {

    UserBeer findByUserIdAndBeerId(String userId, Integer beerId);

    List<UserBeer> findByUserId(String userId);

}

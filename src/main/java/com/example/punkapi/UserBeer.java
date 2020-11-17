package com.example.punkapi;

import javax.persistence.*;

@Entity
@Table(name = "MAP_USER_BEER")
public class UserBeer {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name="user_id")
    private String userId;

    @Column(name="beer_id")
    private Integer beerId;

    public UserBeer() {
    }

    public UserBeer(String userId, Integer beerId) {
        this.userId = userId;
        this.beerId = beerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getBeerId() {
        return beerId;
    }

    public void setBeerId(Integer beerId) {
        this.beerId = beerId;
    }


}

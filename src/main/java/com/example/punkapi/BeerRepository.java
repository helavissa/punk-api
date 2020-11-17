package com.example.punkapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BeerRepository extends PagingAndSortingRepository<Beer, Integer> {

    @Query("Select b from Beer b where lower(b.name) like lower(concat('%', :searchStr,'%')) " +
            "or lower(b.description) like lower(concat('%', :searchStr,'%')) ")
    Page<Beer> findByNameOrDescription(@Param("searchStr") String searchStr,
                                       Pageable pageable);

    @Query("Select b from Beer b " +
            "where (lower(b.name) like lower(concat('%', :searchStr,'%')) " +
            "or lower(b.description) like lower(concat('%', :searchStr,'%'))) and " +
            "b.id in (:favourites)")
    Page<Beer> findByNameOrDescriptionFavourites(@Param("searchStr") String searchStr,
                                       @Param("favourites") List<Integer> favourites,
                                       Pageable pageable);

    @Query("Select b from Beer b where b.id in (:favourites)")
    Page<Beer> findFavourites(@Param("favourites") List<Integer> favourites,
                              Pageable pageable);

}


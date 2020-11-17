package com.example.punkapi;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class MainController {

    private final BeerRepository beerRepository;
    private final UserBeerRepository userBeerRepository;

    public MainController(BeerRepository beerRepository, UserBeerRepository userBeerRepository) {
        this.beerRepository = beerRepository;
        this.userBeerRepository = userBeerRepository;
    }

    @CrossOrigin
    @RequestMapping(value = "/beers")
    @ResponseBody
    public Page<Beer> loadBeers(Pageable pageable,
                                 @RequestParam(required = false) String userId,
                                 @RequestParam(required = false) String searchStr) {
        if(userId == null ){
            if(searchStr == null) {
                // this is when user doesn't request favourites, no search performed
                return beerRepository.findAll(pageable);
            }else{
                // this is when user doesn't request favourites, a search performed
                return beerRepository.findByNameOrDescription(searchStr, pageable);
            }
        }else{
            List<Integer> favourites = userBeerRepository.findByUserId(userId)
                    .stream().map(userBeer -> userBeer.getBeerId()).collect(Collectors.toList());
            if(searchStr == null){
                // this is when user does request favourites, no search performed
                return beerRepository.findFavourites(favourites, pageable);
            }else{
                // this is when user does request favourites and search is performed
                return beerRepository.findByNameOrDescriptionFavourites(searchStr, favourites, pageable);
            }
        }


        /*if(searchStr == null) {
            return beerRepository.findAll(pageable);
        }else{
            return beerRepository.findByNameOrDescription(searchStr, searchStr, pageable);
        }*/

    }

    @CrossOrigin
    @RequestMapping(value = "/user/{userid}/get-favourites")
    @ResponseBody
    public List<Integer> getFavouries(@PathVariable String userid) {
        return userBeerRepository.findByUserId(userid)
                .stream().map(userBeer -> userBeer.getBeerId()).collect(Collectors.toList());
    }

    @CrossOrigin
    @RequestMapping(value = "/user/{userid}/toggle-beer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> toggleUserBeer(@PathVariable String userid,
                                                 @RequestParam Integer beerId){
        UserBeer userBeer = userBeerRepository.findByUserIdAndBeerId(userid, beerId);
        if(userBeer == null){
            UserBeer userBeerEntry = new UserBeer(userid, beerId);
            try {
                userBeerRepository.save(userBeerEntry);
            }catch (DataIntegrityViolationException ex){
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("org.hibernate.exception.ConstraintViolationException: A foreign key constraint fails");
            }
            return new ResponseEntity<>(Integer.toString(userBeerEntry.getId()), HttpStatus.OK);
        }else{
            userBeerRepository.deleteById(userBeer.getId());
            return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/user/{userid}/toggle-beer2", method = RequestMethod.POST)
    @ResponseBody
    public String toggleUserBeer2(@PathVariable String userid,
                                                 @RequestParam Integer beerId){
        UserBeer userBeer = userBeerRepository.findByUserIdAndBeerId(userid, beerId);
        if(userBeer == null){
            UserBeer userBeerEntry = new UserBeer(userid, beerId);
            userBeerRepository.save(userBeerEntry);
            return Integer.toString(userBeerEntry.getId());
        }else{
            userBeerRepository.deleteById(userBeer.getId());
            return null;
        }
    }

}

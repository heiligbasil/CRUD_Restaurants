package com.lambdaschool.crudyrestaurants.controllers;

import com.lambdaschool.crudyrestaurants.models.Restaurant;
import com.lambdaschool.crudyrestaurants.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    // http://localhost:2019/restaurants/restaurants
    @GetMapping(value = "/restaurants", produces = {"application/json"})
    public ResponseEntity<?> listAllRestaurants() {
        List<Restaurant> myRestaurants = restaurantService.findAll();
        return new ResponseEntity<>(myRestaurants, HttpStatus.OK);
    }

    // http://localhost:2019/restaurants/restaurant/3
    @GetMapping(value = "/restaurant/{restid}", produces = {"application/json"})
    public ResponseEntity<?> findRestaurantById(@PathVariable long restid) {
        Restaurant myRestaurant = restaurantService.findRestaurantById(restid);
        return new ResponseEntity<>(myRestaurant, HttpStatus.OK);
    }

    // http://localhost:2019/restaurants/restaurant/name/Supreme%20Eats
    @GetMapping(value = "/restaurant/name/{restname}", produces = {"application/json"})
    public ResponseEntity<?> findRestaurantByName(@PathVariable String restname) {
        Restaurant myRestaurant = restaurantService.findRestaurantByName(restname);
        return new ResponseEntity<>(myRestaurant, HttpStatus.OK);
    }


    // http://localhost:2019/restaurants/restaurant/state/st
    @GetMapping(value = "/restaurant/state/{state}", produces = {"application/json"})
    public ResponseEntity<?> findRestaurantByState(@PathVariable String state) {
        List<Restaurant> myRestaurants = restaurantService.findByState(state);
        return new ResponseEntity<>(myRestaurants, HttpStatus.OK);
    }


    // http://localhost:2019/restaurants/restaurant/likename/eat
    @GetMapping(value = "/restaurant/likename/{restname}", produces = {"application/json"})
    public ResponseEntity<?> findRestaurantByNameLike(@PathVariable String restname) {
        List<Restaurant> myRestaurants = restaurantService.findByNameLike(restname);
        return new ResponseEntity<>(myRestaurants, HttpStatus.OK);
    }

    // http://localhost:2019/restaurants/restaurant
   /*
           {
              "name": "Good East",
              "address": "123 Main Avenue",
              "city": "Uptown",
              "state": "ST",
              "telephone": "555-777-7777",
              "menus": [
                  {
                      "dish": "Soda",
                      "price": 3.50
                  },
                  {
                  "dish": "Latte",
                   "price": 5.00
              }
              ],
              "payments": [
              {
                  "paymentid":8
              },
              {
                  "paymentid":9
              }
              ]
          }
   */
    @PostMapping(value = "/restaurant", consumes = {"application/json"})
    public ResponseEntity<?> addNewRestaurant(@Valid @RequestBody Restaurant newRestaurant) {
        newRestaurant = restaurantService.save(newRestaurant);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newRestaurantURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{restaurantid}").buildAndExpand(newRestaurant.getRestaurantid()).toUri();

        responseHeaders.setLocation(newRestaurantURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    // http://localhost:2019/restaurants/restaurant/18
    /*
            {
                "telephone" : "555-555-1234",
                "seatcapacity" : 152
            }
     */
    @PutMapping(value = "/restaurant/{restaurantid}", consumes = {"application/json"})
    public ResponseEntity<?> updateRestaurant(@RequestBody Restaurant updateRestaurant, @PathVariable long restaurantid) {
        restaurantService.update(updateRestaurant, restaurantid);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // http://localhost:2019/restaurants/restaurant/18
    @DeleteMapping("/restaurant/{restaurantid}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable long restaurantid) {
        restaurantService.delete(restaurantid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
package com.lambdaschool.crudyrestaurants.services;

import com.lambdaschool.crudyrestaurants.models.Menu;
import com.lambdaschool.crudyrestaurants.models.Payment;
import com.lambdaschool.crudyrestaurants.models.Restaurant;
import com.lambdaschool.crudyrestaurants.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "restaurantService")
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestaurantRepository restrepos;

    @Override
    public List<Restaurant> findAll() {
        List<Restaurant> list = new ArrayList<>();
        restrepos.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Restaurant findRestaurantById(long id) {
        return restrepos.findById(id).orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));
    }

    @Override
    public Restaurant findRestaurantByName(String name) {
        Restaurant restaurant = restrepos.findByName(name);

        if (restaurant == null) {
            throw new EntityNotFoundException(name);
        }
        return restaurant;
    }

    @Override
    public List<Restaurant> findByState(String state) {
        return restrepos.findByStateIgnoringCase(state);
    }

    @Override
    public List<Restaurant> findByNameLike(String thename) {
        return restrepos.findByNameContainingIgnoringCase(thename);
    }

    @Transactional
    @Override
    public Restaurant save(Restaurant restaurant) {
        Restaurant newRestaurant = new Restaurant();

        newRestaurant.setName(restaurant.getName());
        newRestaurant.setAddress(restaurant.getAddress());
        newRestaurant.setCity(restaurant.getCity());
        newRestaurant.setState(restaurant.getState());
        newRestaurant.setTelephone(restaurant.getTelephone());
        newRestaurant.setSeatcapacity(restaurant.getSeatcapacity());

        // payment type must already exit
        for (Payment p : restaurant.getPayments()) {
            Payment newPay = paymentService.findPaymentById(p.getPaymentid());

            newRestaurant.addPayment(newPay);
        }

        for (Menu m : restaurant.getMenus()) {
            Menu newMenu = new Menu(m.getDish(), m.getPrice(), newRestaurant);

            newRestaurant.getMenus().add(newMenu);
        }

        return restrepos.save(newRestaurant);
    }

    @Transactional
    @Override
    public Restaurant update(Restaurant restaurant, long id) {
        Restaurant currentRestaurant = findRestaurantById(id);

        if (restaurant.getName() != null) {
            currentRestaurant.setName(restaurant.getName());
        }

        if (restaurant.getAddress() != null) {
            currentRestaurant.setAddress(restaurant.getAddress());
        }

        if (restaurant.getCity() != null) {
            currentRestaurant.setCity(restaurant.getCity());
        }

        if (restaurant.getState() != null) {
            currentRestaurant.setState(restaurant.getState());
        }

        if (restaurant.getTelephone() != null) {
            currentRestaurant.setTelephone(restaurant.getTelephone());
        }

        if (restaurant.hasvalueforseatcapacity) {
            currentRestaurant.setSeatcapacity(restaurant.getSeatcapacity());
        }

        // add new Payments - deleting would happen with another endpoint
        if (restaurant.getPayments().size() > 0) {
            for (Payment p : restaurant.getPayments()) {
                Payment newPay = paymentService.findPaymentById(p.getPaymentid());

                currentRestaurant.addPayment(newPay);
            }
        }

        if (restaurant.getMenus().size() > 0) {
            for (Menu m : restaurant.getMenus()) {
                Menu newMenu = new Menu(m.getDish(), m.getPrice(), currentRestaurant);

                currentRestaurant.getMenus().add(newMenu);
            }
        }

        return restrepos.save(currentRestaurant);
    }

    @Transactional
    @Override
    public void delete(long id) {
        if (findRestaurantById(id) != null) {
            restrepos.deleteById(id);
        }
        ;
    }
}
package com.github.afanas10101111.dfl.web.restaurant;

import com.github.afanas10101111.dfl.dto.DishTo;
import com.github.afanas10101111.dfl.dto.RestaurantTo;
import com.github.afanas10101111.dfl.model.Dish;
import com.github.afanas10101111.dfl.model.Restaurant;
import com.github.afanas10101111.dfl.service.RestaurantService;
import com.github.afanas10101111.dfl.service.VoteService;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.afanas10101111.dfl.util.ControllerUtil.getUriOfNewResource;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = RestaurantController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    public static final String URL = "/v1/restaurants";
    public static final String DISHES_SUFFIX = "/dishes";
    public static final String WITH_DISHES_SUFFIX = "/with-dishes";
    public static final String UP_TO_DATE_SUFFIX = "/up-to-date";

    private final RestaurantService rService;
    private final VoteService vService;
    private final ModelMapper mapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createWithLocation(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("createWithLocation (name = {})", restaurantTo.getName());
        Restaurant newFromTo = getFromTo(restaurantTo);
        ValidationUtil.checkNew(newFromTo);
        Restaurant created = rService.create(newFromTo);
        return ResponseEntity.created(getUriOfNewResource(URL, created)).body(getTo(created, false));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("update with id = {}, set {}", id, restaurantTo);
        Restaurant updatedFromTo = getFromTo(restaurantTo);
        ValidationUtil.checkIdConsistent(id, updatedFromTo);
        rService.update(updatedFromTo);
    }

    @PutMapping(value = "/{id}" + DISHES_SUFFIX, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDishes(@PathVariable long id, @Valid @RequestBody List<DishTo> dishTos) {
        log.info("updateMeals (quantity = {}) for restaurant with id = {}", dishTos.size(), id);
        rService.updateDishes(id, dishTos.stream()
                .map(m -> mapper.map(m, Dish.class))
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("delete with id = {}", id);
        rService.delete(id);
    }

    @GetMapping()
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        return getTos(rService.getAll());
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable long id) {
        log.info("get with id = {}", id);
        return getTo(rService.get(id), true);
    }

    @GetMapping("/{id}" + WITH_DISHES_SUFFIX)
    public RestaurantTo getWithDishes(@PathVariable long id) {
        log.info("getWithMeals with id = {}", id);
        return getToWithDishes(rService.getWithDishes(id), true);
    }

    @GetMapping(UP_TO_DATE_SUFFIX)
    public List<RestaurantTo> getAllUpToDate() {
        log.info("getAllUpToDate");
        return getTos(rService.getAllUpToDate());
    }

    @GetMapping(WITH_DISHES_SUFFIX)
    public List<RestaurantTo> getAllWithDishesUpToDate() {
        log.info("getAllWithMealsByDate");
        return getTosWithDishes(rService.getAllWithDishesUpToDate());
    }

    private Restaurant getFromTo(RestaurantTo to) {
        return mapper.map(to, Restaurant.class);
    }

    private RestaurantTo getToWithDishes(Restaurant restaurant, boolean withVoices) {
        RestaurantTo to = mapper.map(restaurant, RestaurantTo.class);
        to.setVoices(withVoices ? vService.getVoicesCount(restaurant.id()) : null);
        return to;
    }

    private List<RestaurantTo> getTosWithDishes(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(r -> getToWithDishes(r, false))
                .collect(Collectors.toList());
    }

    private RestaurantTo getTo(Restaurant restaurant, boolean withVoices) {
        restaurant.setDishes(null);
        RestaurantTo to = getToWithDishes(restaurant, withVoices);
        to.setDishes(null);
        return to;
    }

    private List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(r -> getTo(r, false))
                .collect(Collectors.toList());
    }
}

package com.github.afanas10101111.dfl.web.restaurant;

import com.github.afanas10101111.dfl.dto.MealTo;
import com.github.afanas10101111.dfl.dto.RestaurantTo;
import com.github.afanas10101111.dfl.model.Meal;
import com.github.afanas10101111.dfl.model.Restaurant;
import com.github.afanas10101111.dfl.service.RestaurantService;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public static final String MEALS_SUFFIX = "/meals";
    public static final String WITH_MEALS_SUFFIX = "/with-meals";
    public static final String UP_TO_DATE_SUFFIX = "/up-to-date";

    private final RestaurantService service;
    private final ModelMapper mapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createWithLocation(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("createWithLocation (name = {})", restaurantTo.getName());
        Restaurant newFromTo = getFromTo(restaurantTo);
        ValidationUtil.checkNew(newFromTo);
        Restaurant created = service.create(newFromTo);
        return ResponseEntity.created(getUriOfNewResource(created)).body(getTo(created));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("update with id = {}, set {}", id, restaurantTo);
        Restaurant updatedFromTo = getFromTo(restaurantTo);
        ValidationUtil.checkIdConsistent(id, updatedFromTo);
        service.update(updatedFromTo);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}" + MEALS_SUFFIX, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMeals(@PathVariable long id, @Valid @RequestBody MealTo.ValidList mealTos) {
        log.info("updateMeals (quantity = {}) for restaurant with id = {}", mealTos.size(), id);
        service.updateMeals(id, mealTos.stream()
                .map(m -> mapper.map(m, Meal.class))
                .collect(Collectors.toList()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("delete with id = {}", id);
        service.delete(id);
    }

    @GetMapping()
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        return getTos(service.getAll());
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable long id) {
        log.info("get with id = {}", id);
        return getTo(service.get(id));
    }

    @GetMapping(WITH_MEALS_SUFFIX + "/{id}")
    public RestaurantTo getWithMeals(@PathVariable long id) {
        log.info("getWithMeals with id = {}", id);
        return getToWithMeals(service.getWithMeals(id));
    }

    @GetMapping(UP_TO_DATE_SUFFIX)
    public List<RestaurantTo> getAllUpToDate() {
        log.info("getAllUpToDate");
        return getTos(service.getAllUpToDate());
    }

    @GetMapping(WITH_MEALS_SUFFIX)
    public List<RestaurantTo> getAllWithMealsUpToDate() {
        log.info("getAllWithMealsByDate");
        return getTosWithMeals(service.getAllWithMealsUpToDate());
    }

    private Restaurant getFromTo(RestaurantTo to) {
        return mapper.map(to, Restaurant.class);
    }

    private RestaurantTo getToWithMeals(Restaurant restaurant) {
        return mapper.map(restaurant, RestaurantTo.class);
    }

    private List<RestaurantTo> getTosWithMeals(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(this::getToWithMeals)
                .collect(Collectors.toList());
    }

    private RestaurantTo getTo(Restaurant restaurant) {
        restaurant.setMeals(null);
        RestaurantTo to = getToWithMeals(restaurant);
        to.setMeals(null);
        return to;
    }

    private List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(this::getTo)
                .collect(Collectors.toList());
    }
}

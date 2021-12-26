package com.github.afanas10101111.dfl.web.restaurant;

import com.github.afanas10101111.dfl.dto.MealTo;
import com.github.afanas10101111.dfl.dto.RestaurantTo;
import com.github.afanas10101111.dfl.model.Meal;
import com.github.afanas10101111.dfl.model.Restaurant;
import com.github.afanas10101111.dfl.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
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
@RestController
@RequestMapping(value = ForAdminController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ForAdminController extends BaseRestaurantController {
    public static final String URL = "/admin/restaurants";
    public static final String MEALS_SUFFIX = "/meals";
    public static final String WITH_MEALS_SUFFIX = "/with-meals";
    public static final String UP_TO_DATE_SUFFIX = "/up-to-date";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createWithLocation(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("createWithLocation (name = {})", restaurantTo.getName());
        Restaurant newFromTo = getFromTo(restaurantTo);
        ValidationUtil.checkNew(newFromTo);
        Restaurant created = service.create(newFromTo);
        return ResponseEntity.created(getUriOfNewResource(created)).body(getTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("update with id = {}, set {}", id, restaurantTo);
        Restaurant updatedFromTo = getFromTo(restaurantTo);
        ValidationUtil.checkIdConsistent(id, updatedFromTo);
        service.update(updatedFromTo);
    }

    @PutMapping(value = "/{id}" + MEALS_SUFFIX, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMeals(@PathVariable long id, @Valid @RequestBody List<MealTo> mealTos) {
        log.info("updateMeals (quantity = {}) for restaurant with id = {}", mealTos.size(), id);
        List<Meal> meals = mealTos.stream()
                .map(m -> mapper.map(m, Meal.class))
                .collect(Collectors.toList());
        service.updateMeals(id, meals);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("delete with id = {}", id);
        service.delete(id);
    }

    @Override
    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable long id) {
        return super.get(id);
    }

    @Override
    @GetMapping(WITH_MEALS_SUFFIX + "/{id}")
    public RestaurantTo getWithMeals(@PathVariable long id) {
        return super.getWithMeals(id);
    }

    @GetMapping()
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        return getTos(service.getAll());
    }

    @Override
    @GetMapping(UP_TO_DATE_SUFFIX)
    public List<RestaurantTo> getAllUpToDate() {
        return super.getAllUpToDate();
    }

    @Override
    @GetMapping(WITH_MEALS_SUFFIX)
    public List<RestaurantTo> getAllWithMealsUpToDate() {
        return super.getAllWithMealsUpToDate();
    }

    private Restaurant getFromTo(RestaurantTo to) {
        return mapper.map(to, Restaurant.class);
    }
}

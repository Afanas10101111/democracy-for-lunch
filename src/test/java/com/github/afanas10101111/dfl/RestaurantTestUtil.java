package com.github.afanas10101111.dfl;

import com.github.afanas10101111.dfl.dto.DishTo;
import com.github.afanas10101111.dfl.dto.RestaurantTo;
import com.github.afanas10101111.dfl.model.Dish;
import com.github.afanas10101111.dfl.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantTestUtil {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.createWithFieldsToIgnore("dishes");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.createWithFieldsToIgnore("dishes");
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_WITH_DISHES_MATCHER = MatcherFactory.createWithFieldsToIgnore("dishes.date", "dishes.restaurant");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_WITH_DISHES_MATCHER = MatcherFactory.createWithFieldsToIgnore("");
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.createWithFieldsToIgnore("date", "restaurant");

    public static final LocalDate NOW = LocalDate.now();

    public static final long MC_DONALDS_ID = 100002;
    public static final long BURGER_KING_ID = 100003;
    public static final long KFC_ID = 100004;
    public static final long SUB_WAY_ID = 100005;
    public static final long NA_ID = 0;

    public static final long HAMBURGER_ID = 100006;
    public static final long CHEESEBURGER_ID = 100007;
    public static final long BIG_MAK_ID = 100008;
    public static final long KING_BURGER_ID = 100009;
    public static final long KING_BURGER_ROYAL_ID = 100010;
    public static final long SUNDERS_WINGS_ID = 100011;
    public static final long BIG_BASKET_ID = 100012;
    public static final long MEGA_SANDWICH_ID = 100013;
    public static final long AA_NEW_PIE_ID = 100014;
    public static final long AB_NEW_PIE_ID = 100015;

    public static final Dish hamburger = new Dish("hamburger", 5000);
    public static final Dish cheeseburger = new Dish("cheeseburger", 6000);
    public static final Dish bigMak = new Dish("big mak", 16000);
    public static final Dish kingBurger = new Dish("king burger", 8000);
    public static final Dish kingBurgerRoyal = new Dish("king burger royal", 18000);
    public static final Dish sundersWings = new Dish("sunders wings", 9000);
    public static final Dish bigBasket = new Dish("big basket", 19000);
    public static final Dish megaSandwich = new Dish("mega sandwich", 10000);
    public static final Dish aaNewPie = new Dish("aaNewPie", 8888);
    public static final Dish abNewPie = new Dish("abNewPie", 9999);

    public static final Restaurant mcDonalds = new Restaurant("McDonalds", "Moscow", List.of(bigMak, cheeseburger, hamburger));
    public static final Restaurant burgerKing = new Restaurant("BurgerKing", "Moscow", List.of(kingBurger, kingBurgerRoyal));
    public static final Restaurant kfc = new Restaurant("KFC", "Moscow", List.of(bigBasket, sundersWings));
    public static final Restaurant subWay = new Restaurant("SubWay", "Москва", List.of(megaSandwich));

    public static final List<Restaurant> all = List.of(burgerKing, kfc, mcDonalds, subWay);
    public static final List<RestaurantTo> allTos;
    public static final List<Restaurant> allWithActualMenu = List.of(burgerKing, kfc, mcDonalds);
    public static final List<RestaurantTo> allTosWithActualMenu;

    static {
        hamburger.setId(HAMBURGER_ID);
        cheeseburger.setId(CHEESEBURGER_ID);
        bigMak.setId(BIG_MAK_ID);
        kingBurger.setId(KING_BURGER_ID);
        kingBurgerRoyal.setId(KING_BURGER_ROYAL_ID);
        sundersWings.setId(SUNDERS_WINGS_ID);
        bigBasket.setId(BIG_BASKET_ID);
        megaSandwich.setId(MEGA_SANDWICH_ID);
        aaNewPie.setId(AA_NEW_PIE_ID);
        abNewPie.setId(AB_NEW_PIE_ID);

        mcDonalds.setId(MC_DONALDS_ID);
        burgerKing.setId(BURGER_KING_ID);
        kfc.setId(KFC_ID);
        subWay.setId(SUB_WAY_ID);

        allTos = all.stream()
                .map(RestaurantTestUtil::getTo)
                .collect(Collectors.toList());
        allTosWithActualMenu = allWithActualMenu.stream()
                .map(RestaurantTestUtil::getToWithDishes)
                .collect(Collectors.toList());
    }

    public static Restaurant getNew() {
        return new Restaurant("New", "New York", List.of(new Dish(hamburger.getName(), hamburger.getPrice())));
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant("MAC", "Washington DC", List.of(hamburger, cheeseburger));
        updated.setId(MC_DONALDS_ID);
        return updated;
    }

    public static List<Dish> getNewDishes() {
        return List.of(new Dish(aaNewPie.getName(), aaNewPie.getPrice()));
    }

    public static RestaurantTo getToWithDishes(Restaurant restaurant) {
        return new RestaurantTo(
                restaurant.id(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getDishes().stream()
                        .map(RestaurantTestUtil::getDishTo)
                        .collect(Collectors.toSet())
        );
    }

    public static RestaurantTo getTo(Restaurant restaurant) {
        return new RestaurantTo(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                null
        );
    }

    public static DishTo getDishTo(Dish dish) {
        return new DishTo(dish.getName(), dish.getPrice());
    }
}

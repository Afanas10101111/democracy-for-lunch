package com.github.afanas10101111.dfl;

import com.github.afanas10101111.dfl.dto.MealTo;
import com.github.afanas10101111.dfl.dto.RestaurantTo;
import com.github.afanas10101111.dfl.model.Meal;
import com.github.afanas10101111.dfl.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantTestUtil {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.createWithFieldsToIgnore("meals");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.createWithFieldsToIgnore("meals");
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_WITH_MEALS_MATCHER = MatcherFactory.createWithFieldsToIgnore("meals.date", "meals.restaurant");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_WITH_MEALS_MATCHER = MatcherFactory.createWithFieldsToIgnore("");
    public static final MatcherFactory.Matcher<Meal> MEALS_MATCHER = MatcherFactory.createWithFieldsToIgnore("date", "restaurant");

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
    public static final long NEW_PIE_ID = 100014;

    public static final Meal hamburger = new Meal("hamburger", 50.00);
    public static final Meal cheeseburger = new Meal("cheeseburger", 60.00);
    public static final Meal bigMak = new Meal("big mak", 160.00);
    public static final Meal kingBurger = new Meal("king burger", 80.00);
    public static final Meal kingBurgerRoyal = new Meal("king burger royal", 180.00);
    public static final Meal sundersWings = new Meal("sunders wings", 90.00);
    public static final Meal bigBasket = new Meal("big basket", 190.00);
    public static final Meal megaSandwich = new Meal("mega sandwich", 100.00);
    public static final Meal aNewPie = new Meal("aNewPie", 88.88);

    public static final Restaurant mcDonalds = new Restaurant("McDonalds", "Moscow", List.of(bigMak, cheeseburger, hamburger));
    public static final Restaurant burgerKing = new Restaurant("BurgerKing", "Moscow", List.of(kingBurger, kingBurgerRoyal));
    public static final Restaurant kfc = new Restaurant("KFC", "Moscow", List.of(bigBasket, sundersWings));
    public static final Restaurant subWay = new Restaurant("SubWay", "Москва", List.of(megaSandwich));

    public static final List<Restaurant> all = List.of(burgerKing, kfc, mcDonalds, subWay);
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
        aNewPie.setId(NEW_PIE_ID);

        mcDonalds.setId(MC_DONALDS_ID);
        burgerKing.setId(BURGER_KING_ID);
        kfc.setId(KFC_ID);
        subWay.setId(SUB_WAY_ID);

        allTosWithActualMenu = allWithActualMenu.stream()
                .map(RestaurantTestUtil::getTo)
                .collect(Collectors.toList());
    }

    public static Restaurant getNew() {
        return new Restaurant("New", "Neq York", List.of(new Meal(hamburger.getName(), hamburger.getPrice())));
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant("MAC", "Washington DC", List.of(hamburger, cheeseburger));
        updated.setId(MC_DONALDS_ID);
        return updated;
    }

    public static List<Meal> getNewMeals() {
        return List.of(new Meal(aNewPie.getName(), aNewPie.getPrice()));
    }

    public static RestaurantTo getTo(Restaurant restaurant) {
        return new RestaurantTo(
                restaurant.id(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getVoices(),
                restaurant.getMeals().stream()
                        .map(RestaurantTestUtil::getMealTo)
                        .collect(Collectors.toSet())
        );
    }

    public static MealTo getMealTo(Meal meal) {
        return new MealTo(meal.getName(), meal.getPrice());
    }
}

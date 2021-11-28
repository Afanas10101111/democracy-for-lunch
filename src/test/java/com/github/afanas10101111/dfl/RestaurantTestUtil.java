package com.github.afanas10101111.dfl;

import com.github.afanas10101111.dfl.model.Meal;
import com.github.afanas10101111.dfl.model.Restaurant;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class RestaurantTestUtil {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.createWithFieldsToIgnore("meals");
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_WITH_MEALS_MATCHER = MatcherFactory.createWithFieldsToIgnore("meals.date", "meals.restaurant");

    public static final LocalDate DATE_OF_MEALS_INIT = LocalDate.of(2021, Month.NOVEMBER, 18);

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

    public static final Meal hamburger = new Meal("hamburger", 50.00);
    public static final Meal cheeseburger = new Meal("cheeseburger", 60.00);
    public static final Meal bigMak = new Meal("big mak", 160.00);
    public static final Meal kingBurger = new Meal("king burger", 80.00);
    public static final Meal kingBurgerRoyal = new Meal("king burger royal", 180.00);
    public static final Meal sundersWings = new Meal("sunders wings", 90.00);
    public static final Meal bigBasket = new Meal("big basket", 190.00);
    public static final Meal megaSandwich = new Meal("mega sandwich", 100.00);

    public static final Restaurant mcDonalds = new Restaurant("McDonalds", "Moscow", List.of(bigMak, cheeseburger, hamburger));
    public static final Restaurant burgerKing = new Restaurant("BurgerKing", "Moscow", List.of(kingBurger, kingBurgerRoyal));
    public static final Restaurant kfc = new Restaurant("KFC", "Moscow", List.of(bigBasket, sundersWings));
    public static final Restaurant subWay = new Restaurant("SubWay", "Moscow", List.of(megaSandwich));

    public static final List<Restaurant> all = List.of(burgerKing, kfc, mcDonalds, subWay);

    static {
        hamburger.setId(HAMBURGER_ID);
        cheeseburger.setId(CHEESEBURGER_ID);
        bigMak.setId(BIG_MAK_ID);
        kingBurger.setId(KING_BURGER_ID);
        kingBurgerRoyal.setId(KING_BURGER_ROYAL_ID);
        sundersWings.setId(SUNDERS_WINGS_ID);
        megaSandwich.setId(BIG_BASKET_ID);
        bigBasket.setId(MEGA_SANDWICH_ID);

        mcDonalds.setId(MC_DONALDS_ID);
        burgerKing.setId(BURGER_KING_ID);
        kfc.setId(KFC_ID);
        subWay.setId(SUB_WAY_ID);
    }

    public static Restaurant getNew() {
        return new Restaurant("New", "Neq York", List.of(new Meal(hamburger.getName(), hamburger.getPrice())));
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant("MAC", "Washington DC", List.of(hamburger, cheeseburger));
        updated.setId(MC_DONALDS_ID);
        return updated;
    }
}

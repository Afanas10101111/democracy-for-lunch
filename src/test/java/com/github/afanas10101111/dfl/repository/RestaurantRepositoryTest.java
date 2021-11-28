package com.github.afanas10101111.dfl.repository;

import com.github.afanas10101111.dfl.config.DataJpaConfig;
import com.github.afanas10101111.dfl.model.Restaurant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static com.github.afanas10101111.dfl.RestaurantTestUtil.DATE_OF_MEALS_INIT;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.MC_DONALDS_ID;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.RESTAURANT_WITH_MEALS_MATCHER;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.all;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getNew;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.getUpdated;
import static com.github.afanas10101111.dfl.RestaurantTestUtil.mcDonalds;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DataJpaConfig.class)
@Sql(scripts = "classpath:db/populate.sql", config = @SqlConfig(encoding = "UTF-8"))
public class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepositoryImpl repository;

    @Test
    public void save() {
        Restaurant saved = repository.save(getNew());
        long savedId = saved.id();
        Restaurant expectedSaved = getNew();
        expectedSaved.setId(savedId);
        expectedSaved.setMeals(saved.getMeals());
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(saved, expectedSaved);
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(repository.getWithMealsByDate(savedId, LocalDate.now()), expectedSaved);

        Restaurant updatedFromDb = repository.save(getUpdated());
        long updatedId = updatedFromDb.id();
        Restaurant expectedUpdated = getUpdated();
        RESTAURANT_MATCHER.assertMatch(updatedFromDb, expectedUpdated);
        RESTAURANT_MATCHER.assertMatch(repository.getWithMealsByDate(updatedId, LocalDate.now()), expectedUpdated);
    }

    @Test
    public void saveConstraintViolation() {
        Restaurant aNew = getNew();
        aNew.setName(mcDonalds.getName());
        aNew.setAddress(mcDonalds.getAddress());
        assertThrows(DataIntegrityViolationException.class, () -> repository.save(aNew));
    }

    @Test
    public void delete() {
        assertTrue(repository.delete(MC_DONALDS_ID));
        assertNull(repository.get(MC_DONALDS_ID));
        assertFalse(repository.delete(MC_DONALDS_ID));
    }

    @Test
    public void get() {
        RESTAURANT_MATCHER.assertMatch(repository.get(MC_DONALDS_ID), mcDonalds);
    }

    @Test
    public void getWithMealsByDate() {
        RESTAURANT_WITH_MEALS_MATCHER.assertMatch(repository.getWithMealsByDate(MC_DONALDS_ID, DATE_OF_MEALS_INIT), mcDonalds);
    }

    @Test
    public void getWithOutOfDateMeals() {
        assertNull(repository.getWithMealsByDate(MC_DONALDS_ID, LocalDate.now()));
    }

    @Test
    public void getAll() {
        RESTAURANT_MATCHER.assertMatch(repository.getAll(), all);
    }
}
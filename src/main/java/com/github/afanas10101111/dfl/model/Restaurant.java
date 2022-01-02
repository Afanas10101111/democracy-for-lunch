package com.github.afanas10101111.dfl.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, exclude = "meals")
@Entity
@Table(
        name = "restaurants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "address"}, name = "uc_name_address")
)
public class Restaurant extends NamedEntity {

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 5, max = 100)
    private String address;

    @Column(name = "voices", nullable = false)
    @Range(max = 2000000000)
    private int voices;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("name")
    private Set<Meal> meals;

    public Restaurant(String name, String address, Collection<Meal> meals) {
        super(null, name);
        this.address = address;
        setMeals(meals);
    }

    public void addVoice() {
        voices++;
    }

    public void removeVoice() {
        voices--;
    }

    public void setMeals(Collection<Meal> meals) {
        this.meals = CollectionUtils.isEmpty(meals) ? Collections.emptySet() : Set.copyOf(meals);
        this.meals.forEach(m -> m.setRestaurant(this));
    }

    public void addMeals(Collection<Meal> meals) {
        meals.forEach(m -> m.setRestaurant(this));
        this.meals.addAll(meals);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

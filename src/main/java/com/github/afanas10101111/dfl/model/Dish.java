package com.github.afanas10101111.dfl.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, exclude = "restaurant")
@Entity
@Table(
        name = "dishes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"serving_date", "restaurant_id", "name"}, name = "uc_date_restaurant_name")
)
public class Dish extends NamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "price", nullable = false)
    @Range(min = 1, max = 10000000)
    private int price;

    @Column(name = "serving_date", nullable = false)
    @NotNull
    private LocalDate servingDate = LocalDate.now();

    public Dish(String name, int price) {
        super(name);
        this.price = price;
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

package com.github.afanas10101111.dfl.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(
        name = "voices",
        uniqueConstraints = @UniqueConstraint(columnNames = {"voting_date", "user_id"}, name = "uc_date_user")
)
public class Voice extends BaseEntity {

    @Column(name = "voting_date", nullable = false)
    private LocalDate votingDate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Voice(LocalDate votingDate, User user, Restaurant restaurant) {
        this.votingDate = votingDate;
        this.user = user;
        this.restaurant = restaurant;
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

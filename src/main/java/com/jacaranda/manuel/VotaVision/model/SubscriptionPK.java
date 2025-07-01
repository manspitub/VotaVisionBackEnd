package com.jacaranda.manuel.VotaVision.model;

import java.io.Serializable;
import java.util.Objects;

public class SubscriptionPK implements Serializable {

    private Long user;
    private Long category;

    public SubscriptionPK() {}

    public SubscriptionPK(Long user, Long category) {
        this.user = user;
        this.category = category;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionPK)) return false;
        SubscriptionPK that = (SubscriptionPK) o;
        return Objects.equals(user, that.user) &&
               Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, category);
    }
}

package com.jacaranda.manuel.VotaVision.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "Suscripcion")
@IdClass(SubscriptionPK.class)
public class Subscription {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id")
    private Category category;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_suscripcion", nullable = false)
    private Date subscriptionDate = new Date();

    // Constructores
    public Subscription() {}

    public Subscription(User user, Category category, Date subscriptionDate) {
        this.user = user;
        this.category = category;
        this.subscriptionDate = subscriptionDate;
    }

    // Getters y Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription)) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(user, that.user) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, category);
    }
}

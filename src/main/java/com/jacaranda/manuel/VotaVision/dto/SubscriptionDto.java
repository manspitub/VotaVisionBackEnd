package com.jacaranda.manuel.VotaVision.dto;

import java.util.Date;

public class SubscriptionDto {
    private Long categoryId;
    private String categoryName;
    private Date subscribedAt;

    // Getters y setters
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public Date getSubscribedAt() { return subscribedAt; }
    public void setSubscribedAt(Date subscribedAt) { this.subscribedAt = subscribedAt; }
}

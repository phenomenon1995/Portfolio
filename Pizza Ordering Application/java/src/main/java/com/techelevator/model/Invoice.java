package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Invoice {
    @JsonProperty("invoice_id")
    private int invoiceId;
    private BigDecimal total;
    @JsonProperty("is_delivery")
    private boolean isDelivery;
    @JsonProperty("user_id")
    private int userId;
    private String status;
    private Timestamp timestamp;

    public Invoice() {}
    public Invoice(int invoiceId, int userId, BigDecimal total, boolean isDelivery,
                   String status, Timestamp timestamp) {
        this.invoiceId = invoiceId;
        this.total = total;
        this.isDelivery = isDelivery;
        this.userId = userId;
        this.status = status;
        this.timestamp = timestamp;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public void setDelivery(boolean delivery) {
        isDelivery = delivery;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

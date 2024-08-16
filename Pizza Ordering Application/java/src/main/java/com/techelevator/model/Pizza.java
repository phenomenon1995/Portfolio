package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Pizza {
    @JsonProperty("pizza_id")
    private int pizzaId;
    @JsonProperty("invoice_id")
    private int invoiceId;
    @JsonProperty("pizza_name")
    private String pizzaName;
    private BigDecimal total;
    @JsonProperty("additional_instructions")
    private String additionalInstructions;
    private List<Product> components = new ArrayList<>();

    public Pizza() {}
    public Pizza(int pizzaId, int invoiceId,String pizzaName,BigDecimal total,
                 String additionalInstructions) {
        this.pizzaId = pizzaId;
        this.invoiceId = invoiceId;
        this.pizzaName = pizzaName;
        this.total = total;
        this.additionalInstructions = additionalInstructions;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    public List<Product> getComponents() {
        return components;
    }

    public void setComponents(List<Product> components) {
        this.components = components;
    }
    public void addComponent(Product component){
        this.components.add(component);
    }


    public void removeComponent(Product component){
        for (Product c : this.components) {
            if (c.getProductId() == component.getProductId()){this.components.remove(c);
            }
        }

    }
}

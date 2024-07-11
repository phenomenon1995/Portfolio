package com.techelevator;

import java.math.BigDecimal;

public abstract class VendItem {
    private BigDecimal price;
    private String name;

    public VendItem(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract String getMessage();


    @Override
    public String toString(){
        return String.format("%1$s $%2$s", this.name, price);
    }

    public boolean equals(VendItem o){
        if (o == this){return true;}
        if(!(o instanceof VendItem)){return false;}
        VendItem object =  o;
        if(this.name == object.name && this.price.compareTo(object.price) == 0 && this.getMessage() == object.getMessage()){
            return true;
        }
        return false;
    }

    public static class Candy extends VendItem{
        private String message;

        public Candy(String name, BigDecimal price) {
            super(name, price);
            this.message =  "Munch Munch, Yum!";
        }
        public String getMessage(){
            return this.message;
        }
        @Override
        public String toString(){
            return String.format("%s %s", super.getName(), super.getPrice());
        }
    }

    public static class Chip extends VendItem{
        private String message;

        public Chip(String name, BigDecimal price){
            super(name, price);
            this.message = "Crunch Crunch, Yum!";
        }

        public String getMessage(){
            return this.message;
        }


    }

    public static class Drink extends VendItem{

        private String message;

        public Drink(String name, BigDecimal price){
            super(name, price);
            this.message = "Glug Glug, Yum!";

        }

        public String getMessage(){
            return this.message;
        }

    }

    public static class Gum extends VendItem{

        private String message;

        public Gum(String name, BigDecimal price){
            super(name, price);
            this.message = "Chew Chew,Yum!";

        }

        public String getMessage(){
            return this.message;
        }
    }
}

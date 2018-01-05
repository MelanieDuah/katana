package com.katana.entities;

public class CostItem extends BaseEntity {

    private static final long serialVersionUID = 8437795923968524879L;

    private String name;

    public CostItem() {
        super();
    }

    public CostItem(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

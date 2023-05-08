package dev.Inventory.BusinessLayer;

import java.util.List;

public class Record {
    private String catalog_number;
    private String serial_number;
    private String name;
    private String branch;
    private String manufacturer;
    private double supplier_price;
    private double supplier_discount;
    private double store_price; // Product.soldPrice != -1 ? Product.soldPrice : ProductType.originalStorePrice
    private Category category;
    private List<Category> subCategories;
    private String location;
    private int warehouse_amount;
    private int store_amount;



    //catalog_number - ProductTypeID
    //serial_number - ProductID
    public Record(String catalog_number, String serial_number, String name, String branch, String manufacturer, double supplier_price, double supplier_discount, double store_price, Category category, List<Category> subCategories, String location) {
        this.catalog_number = catalog_number;
        this.serial_number = serial_number;
        this.name = name;
        this.branch = branch;
        this.manufacturer = manufacturer;
        this.supplier_price = supplier_price;
        this.supplier_discount = supplier_discount;
        this.store_price = store_price;
        this.category = category;
        this.subCategories = subCategories;
        this.location = location;
    }

    public Record(String catalog_number,  String name, String branch, String manufacturer, double store_price, int warehouse_amount, int store_amount) {
        this.catalog_number = catalog_number;
        this.name = name;
        this.branch = branch;
        this.manufacturer = manufacturer;
        this.store_price = store_price;
        this.warehouse_amount = warehouse_amount;
        this.store_amount = store_amount;
    }

    @Override
    public String toString() {
        return "Record{" +
                "catalog_number=" + catalog_number +
                ", serial_number=" + serial_number +
                ", name='" + name + '\'' +
                ", branch='" + branch + '\'' +
                ", manufacturer=" + manufacturer +
                ", supplier_price=" + supplier_price +
                ", supplier_discount=" + supplier_discount +
                ", store_price=" + store_price +
                ", category=" + category.toString() +
                ", subCategories=" + subCategories.toString() +
                ", location='" + location + '\'' +
                '}';
    }

    public String toStringProduct() {
        return "Record{" +
                "catalog_number=" + catalog_number +
                ", name='" + name + '\'' +
                ", branch='" + branch + '\'' +
                ", manufacturer=" + manufacturer +
                ", store_price=" + store_price +
                ", warehouse_amount='" + warehouse_amount +
                ", store_amount='" + store_amount +
                ", total_amount='" + (warehouse_amount+store_amount) + '\'' +
                '}';
    }
}

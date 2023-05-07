package dev.Inventory.BusinessLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    private  String catalog_number;
    private  String name;
    private String manufacturer;
    private int storeAmount;
    private int warehouseAmount;
    private double originalSupplierPrice;
    private double originalStorePrice;
    private Category category;
    private List<Category> subCategory;
    //default value -1
    private int notificationMin;
    private Map<Integer, ProductItem> productItems;
    private String branch;

    public Product(String catalog_number, String name, String manufacturer, int storeAmount, int warehouseAmount, double originalSupplierPrice, double originalStorePrice, String branch){
        this.catalog_number = catalog_number;
        this.name = name;
        this.manufacturer = manufacturer;
        this.storeAmount = storeAmount;
        this.warehouseAmount = warehouseAmount;
        this.originalSupplierPrice = originalSupplierPrice;
        this.originalStorePrice = originalStorePrice;
        this.category = null;
        this.subCategory = new ArrayList<Category>();
        this.notificationMin = -1;
        // Map<productID, product>
        this.productItems = new HashMap<Integer, ProductItem>();
        this.branch = branch;
    }

    public ProductItem getProduct(int productID){
        if(productItems.containsKey(productID))
            return productItems.get(productID);
        else
            throw new RuntimeException(String.format("Product does not exist with the ID : %s",productID));
    }
    public void reportAsDefective(List<Integer> ItemsSerialNumber){
        for(Integer defectiveSerialNumber: ItemsSerialNumber){
            productItems.get(defectiveSerialNumber).reportAsDefective();
        }
    }
    public void reportAsDefective(int serialNumber){
        productItems.get(serialNumber).reportAsDefective();
    }
    // TODO: should add soldPrice and check with the discount table
    public void reportAsSold(List<Integer> serialNumber, double soldPrice){
        for(Integer soldProductID: serialNumber){
            productItems.get(soldProductID).reportAsSold(soldPrice);
        }
    }
    public void setNotificationMin(int newVal){
        this.notificationMin = newVal;
    }
    public void setCategory(Category category){
        this.category = category;
        category.addProduct(this);
    }
    public void addSubCategory(Category category){
        subCategory.add(category);
        category.addProduct(this);
    }

    public void addProductItem(int serialNumber, int supplierID, double supplierPrice, String location, Double supplierDiscount, LocalDateTime expirationDate){
        productItems.put(serialNumber,new ProductItem(serialNumber,supplierID,supplierPrice,location,supplierDiscount, expirationDate));
    }

    public List<ProductItem> getDefectiveProductItems(){
        List<ProductItem> defectiveList = new ArrayList<ProductItem>();
        for(ProductItem product: productItems.values()){
            if(product.isDefective())
                defectiveList.add(product);
        }
        return defectiveList;
    }
    //TODO : verify we want to return all products (maybe not defective and sold)
    public Map<String,List<ProductItem>> getAllProductItems(Map<String,List<ProductItem>> allProducts){
        List<ProductItem> currentProducts = new ArrayList<ProductItem>();
        for(ProductItem product: productItems.values()){
            currentProducts.add(product);
        }
        allProducts.put(catalog_number, currentProducts);
        return allProducts;
    }

    public Map<Integer, ProductItem> getProductItems(){return this.productItems;}

    public Boolean isProductLack(){
        if(storeAmount+warehouseAmount < notificationMin)
            return true;
        return false;
    }
    public String getCatalogNumber(){return catalog_number;}

    public void setName(String newName){this.name = newName;}
    public void setManufacturer(String newManufacturer){this.manufacturer =newManufacturer;}
    public void setStoreAmount(int newStoreAmount){this.storeAmount = newStoreAmount;}
    public void setWarehouseAmount(int newWarehouseAmount){this.warehouseAmount = newWarehouseAmount;}
    public void setOriginalStorePrice(double newPrice ){this.originalStorePrice = newPrice;}
    public void setOriginalSupplierPrice(double newPrice){this.originalSupplierPrice = newPrice;}
    public double getOriginalStorePrice(){return this.originalStorePrice;}
    public String getName(){return this.name;}
    public String getManufacturer(){return this.manufacturer;}
    public double getOriginalSupplierPrice(){return this.originalSupplierPrice;}
    public Category getCategory(){return this.category;}
    public List<Category> getSubCategory(){return this.subCategory;}
}

package BusinessLayer;

import BusinessLayer.DeliveryAgreements.DeliveryAgreement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class Agreement {
    private String paymentMethod;
    /***
     * products on the agreement - maps between product's id to the product and its amount
     */
    private Map<Integer, Product> products;
    private BillOfQuantities billOfQuantities;
    private DeliveryAgreement deliveryAgreement;

    public Agreement(String paymentMethod, List<Product> productsList, DeliveryAgreement deliveryAgreement){
        this.paymentMethod = paymentMethod;
        products = new HashMap<>();
//        for (Product p : productsList)
//            products.put(p.getId(), p);
        this.deliveryAgreement = deliveryAgreement;
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public Product getProduct(int productId){
        return products.get(productId);
    }
    public DeliveryAgreement getDeliveryAgreement() {
        return deliveryAgreement;
    }

    public void setDeliveryAgreement(DeliveryAgreement deliveryAgreement){
        this.deliveryAgreement = deliveryAgreement;
    }

    public void addProduct(Product product, int amount){
        products.put(product.getId(), product);
    }

    public void removeProduct(Integer productId){
        products.remove(productId);
    }

    public BillOfQuantities getBillOfQuantities(){
        return billOfQuantities;
    }

    public void setBillOfQuantities(BillOfQuantities billOfQuantities) {
        this.billOfQuantities = billOfQuantities;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


}

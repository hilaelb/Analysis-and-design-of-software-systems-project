package Backend.BusinessLayer.SuppliersModule;


import Backend.BusinessLayer.SuppliersModule.DeliveryAgreements.DeliveryAgreement;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Supplier {
    private String name;
    public String bnNumber;
    private BankAccount bankAccount;
    private List<String> fields;
    private String paymentMethod;
    /***
     * maps between the name of the contact person and his contact info - email, phone number
     */
    private Map<String,Pair<String, String>> contactsInfo;
    /***
     * the agreement with the supplier
     */
    private Agreement agreement;
    /***
     * all the orders that have been ordered from the supplier
     */
    private List<Order> orderHistory;
    public Supplier(String name, String bnNumber, BankAccount bankAccount, String paymentMethod,
                    List<String> fields, Map<String,Pair<String, String>> contactsInfo,
                    List<Product> productList, DeliveryAgreement deliveryAgreement){
        this.name = name;
        this.bnNumber = bnNumber;
        this.bankAccount = bankAccount;
        this.fields = fields;
        this.contactsInfo = contactsInfo;
        this.paymentMethod = paymentMethod;
        agreement = new Agreement(productList, deliveryAgreement);
    }
    public Supplier(String name, String bnNumber, BankAccount bankAccount, String paymentMethod,
                    List<String> fields, Map<String,Pair<String, String>> contactsInfo,
                    List<Product> productList, DeliveryAgreement deliveryAgreement, BillOfQuantities billOfQuantities){
        this(name, bnNumber, bankAccount, paymentMethod, fields, contactsInfo, productList, deliveryAgreement);
        agreement = new Agreement(productList, deliveryAgreement, billOfQuantities);
    }

    public void setName(String name){
        this.name = name;
    }

    public void setBankAccount(String bank, String branch, String accountNumber) {
        this.bankAccount = new BankAccount(bank, branch, accountNumber);
    }

    public void setBnNumber(String bnNumber) {
        this.bnNumber = bnNumber;
    }

    public void removeContactInfo(String contactsName) {
        contactsInfo.remove(contactsName);
    }
    public void addContactInfo(String contactName, String email, String phoneNumber){
        this.contactsInfo.put(contactName, new Pair<>(email, phoneNumber));
    }

    public void setContactsEmail(String contactName, String email){
        contactsInfo.get(contactName).setFirst(email);
    }
    public void setContactsPhoneNumber(String contactName, String phoneNumber){
        contactsInfo.get(contactName).setSecond(phoneNumber);
    }



    public Agreement getAgreement() {
        return agreement;
    }

    public void addProduct(String name, String catalogNumber, double price, int numberOfUnits){
        agreement.addProduct(new Product(name, catalogNumber, price, numberOfUnits));
    }

    public void addField(String field){
        if(!fields.contains(field))
            fields.add(field);
    }

    public boolean productExist(int productId){
        return agreement.getProduct(productId) != null;
    }

    public void removeField(String field){
        fields.remove(field);
    }

    public void addOrder(Order order){
        if(orderHistory == null)
            orderHistory = new LinkedList<>();
        orderHistory.add(order);
    }
    public String getBnNumber(){
        return bnNumber;
    }

    public List<Order> getOrderHistory(){
        return orderHistory;
    }

    public String getName() {
        return name;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public String toString(){
        String contactsInfo = "CONTACTS INFORMATION:";
        for(Map.Entry<String, Pair<String, String>> contact : this.contactsInfo.entrySet())
            contactsInfo = contactsInfo + "\n\t\tName: " + contact.getKey() + " Email: " + contact.getValue().getFirst() + " Phone number: " + contact.getValue().getSecond();
        String fields = "FIELDS:";
        for (String field : this.fields)
            fields = fields + "\n\t\t" + field;
        String bankAccount = "BANK ACCOUNT:\n\t\t" + this.bankAccount.toString();

        String res = "SUPPLIER:\n\tNAME: " + name +"\n\tBN NUMBER: " + bnNumber + "\n\t" + bankAccount + "\n\t" + fields
                + "\n\t" + contactsInfo + "\n\t" + "PAYMENT METHOD: " + paymentMethod +"\n\t" + agreement.toString();
        return res;
    }
}

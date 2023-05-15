package Backend.BusinessLayer.InventoryModule;

import Backend.BusinessLayer.BusinessLayerUsage.Branch;

import java.util.*;

public class Category {
    private String categoryName;
    // map<catalog_number, Product obj>
    private List<Product> productsRelated;


    //map<category_name, Category obj>
    private Map<String, Category> subCategories;


    public Category(String nameCategory, List<Category> subcategories){
        this.categoryName = nameCategory;
        this.subCategories = new HashMap<String, Category>();
        this.productsRelated = new ArrayList<Product>();
        if(!subcategories.isEmpty()){
            for(Category category: subcategories)
                subCategories.put(category.getCategoryName(), category);
        }
    }

    public void addProductToCategory(Product product){
        if(!isProductIDRelated(product.getCatalogNumber(),product.getBranch()))
            productsRelated.add(product);
    }


    public Boolean isProductIDRelated(String catalog_number, Branch branch){
        for(Product product: productsRelated){
            if(product.getBranch() == branch && product.getCatalogNumber() == catalog_number)
                return true;
        }
        return false;
    }

    public String getCategoryName(){return this.categoryName;}

    public Boolean isRelatedProductEmpty(){return productsRelated.isEmpty();}

    public List<Product> getProductsRelated(Branch branch){
        List<Product> productsRelatedList = new ArrayList<Product>();
        for(Product product : productsRelated){
            if(product.getBranch() == branch)
                productsRelatedList.add(product);
        }
        return productsRelatedList;
    }

//            for(Product productRelated: productsRelated){
//        if(productRelated.getBranch() == product.getBranch() && productRelated.getCatalogNumber() == product.getCatalogNumber())
//            productsRelated.remove(product);
//    }
    public void removeProduct(Product product){
        Iterator<Product> iterator = productsRelated.iterator();
        while (iterator.hasNext()) {
            Product productRelated = iterator.next();
            if (productRelated.getBranch() == product.getBranch() && productRelated.getCatalogNumber() == product.getCatalogNumber()) {
                iterator.remove();
            }
        }

    }

    public boolean isSubcategory(String subcategoryName){
        return subCategories.containsKey(subcategoryName);
    }

    public Map<String, Category> getSubcategories(){return subCategories;}
    public void removeSubCategory(String subcategoryName){
        if(isSubcategory(subcategoryName))
            subCategories.remove(subcategoryName);
    }

    public void addSubcategories(List<Category> subcategories){
        if(!subcategories.isEmpty()){
            for(Category category: subcategories)
                subCategories.put(category.getCategoryName(), category);
        }
    }

    private String  getSubCategoriesName(){
        if(subCategories.isEmpty())
            return "there is no subCategory";
        else{
            String ans = "[ ";
            for(Category subcategory: subCategories.values()){
                ans = ans + subcategory.getCategoryName() + " ,";
            }
            ans = ans.substring(0,ans.length()-1) + " ]";
            return ans;
        }
    }
    @Override
    public String toString() {
        return "Category{" +
                "nameCategory='" + categoryName + '\'' +
                ", subCategories=" + getSubCategoriesName() +
                '}';
    }
}

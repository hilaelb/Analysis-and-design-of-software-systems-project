package dev.Inventory.Tests;
import dev.Inventory.BusinessLayer.InventoryModule.Category;
import dev.Inventory.BusinessLayer.InventoryModule.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

public class CategoryTest {
    public static String categoryName = "test_category";
    public static List<Category> sub = new ArrayList<>();

    public static String catalog_number = "0x123";
    public static String catalog_name = "productTest";
    public static String manufacturer = "tnuva";
    public static double storePrice = 5.0;
    public static String branch = "beer sheva";
    private Category category;
    private Product product;

    private Category c;
    @Before
    public void setUp() {
        //create new category
        category = new Category(categoryName, sub);
        //create new product
        product = new Product(catalog_number, catalog_name, manufacturer, storePrice,branch );
    }


    //Test 1
    // Verify that the product was added correctly
    @org.junit.Test
    public void addProductToCategory() {
        category.addProductToCategory(product);
        Assert.assertTrue("failed to add product to category",category.isProductIDRelated(product.getCatalogNumber()));
    }

    //Test 2
    // Verify that the product removed from category
    @org.junit.Test
    public void removeProductFromCategory() {
        category.addProductToCategory(product);
        Assert.assertTrue("failed to add product to category",category.isProductIDRelated(product.getCatalogNumber()));
        category.removeProduct(product.getCatalogNumber());
        Assert.assertFalse("failed to remove product from category",category.isProductIDRelated(product.getCatalogNumber()));
    }

    //Test 3
    // Verify that subcategory added as expected
    @org.junit.Test
    public void addSubcategoryToCategory() {
        Category subcategory = new Category("milk",new ArrayList<>());
        List<Category> subTest = new ArrayList<>();
        subTest.add(subcategory);
        category.addSubcategories(subTest);
        Assert.assertTrue("failed to add the subcategory",category.isSubcategory(subcategory.getCategoryName()));
    }

}


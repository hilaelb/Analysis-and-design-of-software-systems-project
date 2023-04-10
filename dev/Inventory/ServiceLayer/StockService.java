package dev.Inventory.ServiceLayer;

import dev.Inventory.BusinessLayer.DiscountCategoryController;
import dev.Inventory.BusinessLayer.ProductController;

import java.time.LocalDateTime;

public class StockService {
    ProductController productController;
    DiscountCategoryController categoryController;

    public StockService() {
        productController = ProductController.ProductController();
        categoryController = DiscountCategoryController.DiscountCategoryController();
    }


    public Response addProductType(int ProductTypeID, String name, String manufacturer, double supplier_price, double store_price, String branch, int storeAmount, int warehouseAmount) {
        try {
            productController.createProductType(ProductTypeID, branch, name, manufacturer, storeAmount, warehouseAmount, supplier_price, store_price);
            return new Response<>("Product added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

    public Response addProduct(int ProductID, int ProductTypeID, int supplier, int supplier_price, String branch, String location) {
        try {
            productController.createProduct(ProductID, ProductTypeID, branch, supplier, supplier_price, location);
            return new Response<>("Product added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

    public Response updateProductType(String name, int catalog_number, String manufacturer, double supplier_price, double store_price, String category, String sub_category, int min_amount, String branch) {
        try {
            if (name != null) {
                productController.updateProductType(branch, name, catalog_number, null, -1, -1, null, null, -1);
                return new Response<>("Product type updated successfully");
            } else if (manufacturer != null) {
                productController.updateProductType(branch, null, catalog_number, manufacturer, -1, -1, null, null, -1);
                return new Response<>("Product type updated successfully");
            } else if (supplier_price != -1) {
                productController.updateProductType(branch, null, catalog_number, null, supplier_price, -1, null, null, -1);
                return new Response<>("Product type updated successfully");
            } else if (store_price != -1) {
                productController.updateProductType(branch, null, catalog_number, null, -1, store_price, null, null, -1);
                return new Response<>("Product type updated successfully");
            } else if (category != null) {
                productController.updateProductType(branch, null, catalog_number, null, -1, -1, category, null, -1);
                return new Response<>("Product type updated successfully");
            } else if (sub_category != null) {
                productController.updateProductType(branch, null, catalog_number, null, -1, -1, null, sub_category, -1);
                return new Response<>("Product type updated successfully");
            } else if (min_amount != -1) {
                productController.updateProductType(branch, null, catalog_number, null, -1, -1, null, null, min_amount);
                return new Response<>("Product type updated successfully");
            } else {
                return Response.createErrorResponse("Invalid input parameters");
            }
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

    public Response updateProduct(int is_defective, int catalog_number, int serial_num, int is_sold, int supplier, int supplier_price, int sold_price, String location, String branch) {
        try {
            if (is_defective != -1) {
                productController.updateProduct(branch, is_defective, catalog_number, serial_num, -1, -1, -1, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (is_sold != -1) {
                productController.updateProduct(branch, -1, catalog_number, serial_num, is_sold, -1, -1, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (supplier != -1) {
                productController.updateProduct(branch, -1, catalog_number, serial_num, -1, supplier, -1, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (supplier_price != -1) {
                productController.updateProduct(branch, -1, catalog_number, serial_num, -1, -1, supplier_price, -1, null);
                return new Response<>("Product type updated successfully");
            } else if (sold_price != -1) {
                productController.updateProduct(branch, -1, catalog_number, serial_num, -1, -1, -1, sold_price, null);
                return new Response<>("Product type updated successfully");
            } else if (location != null) {
                productController.updateProduct(branch, -1, catalog_number, serial_num, -1, -1, -1, -1, location);
                return new Response<>("Product type updated successfully");
            } else {
                return Response.createErrorResponse("Invalid input parameters");
            }
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating product type: " + e.getMessage());
        }
    }

//    public Response getStockProductsByCategory(){
//        categoryController.getProductsPerCategory()
//    }

    //    public Response getInventoryShortages(){
//
//    }
//
    public Response getDefectiveProducts(String branch) {
        try {
            return new Response<>(productController.getDefectiveProducts(branch));
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating catagory: " + e.getMessage());
        }
    }

    public Response createMainCategory(String name, String branch) {
        try {
            categoryController.createCategory(branch, name, 1);
            return new Response<>("Catagory created successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating catagory: " + e.getMessage());
        }
    }

    public Response createSubCategory(String name, String branch) {
        try {
            categoryController.createCategory(branch, name, 0);
            return new Response<>("Sub-Catagory created successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error creating sub catagory: " + e.getMessage());
        }
    }

    public Response removeMainCategory(String name, String branch) {
        try {
            categoryController.removeCategory(branch, name);
            return new Response<>("Category removed successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response removeSubCategory(String name, String branch) {
        try {
            categoryController.removeCategory(branch, name);
            return new Response<>("Sub-Category removed successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error removing category: " + e.getMessage());
        }
    }

    public Response updateDiscountPerCategory(String name, String branch, double discount, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            categoryController.createCategoryDiscount(name, branch, discount, startDate, endDate);
            return new Response<>("discount added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating discount: " + e.getMessage());
        }
    }

    public Response updateDiscountPerProduct(int ProductTypeID, String branch, double discount, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            categoryController.createStoreDiscount(ProductTypeID, branch, discount, startDate, endDate);
            return new Response<>("discount added successfully");
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating discount: " + e.getMessage());
        }
    }

    public Response getProductDetails(int catalog_num, int serial_num, String branch) {
        try {
            return new Response<>(productController.getProductDetails(branch, catalog_num, serial_num));
        } catch (Exception e) {
            return Response.createErrorResponse("Error updating discount: " + e.getMessage());
        }
    }
}

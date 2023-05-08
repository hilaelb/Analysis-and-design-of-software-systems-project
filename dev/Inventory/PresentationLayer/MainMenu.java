package dev.Inventory.PresentationLayer;

import dev.Inventory.ServiceLayer.CategoriesService;
import dev.Inventory.ServiceLayer.StockService;

import java.util.*;

public class MainMenu {
    protected Scanner in;
    protected StockService stockService;

    protected CategoriesService categoriesService;

    public MainMenu(){
        in = new Scanner(System.in);
        stockService = new StockService();
        categoriesService = new CategoriesService();
    }

    public void run() {
        while(true){
            System.out.println("Please select a branch:(number)");
            String branch = in.nextLine();
            System.out.println("Please select an option:");
            System.out.println("1. Manage reports");
            System.out.println("2. Manage inventory's products");
            System.out.println("3. Manage Discounts");
            System.out.println("4. Manage Categories");
            System.out.println("5. Exit");
            int option = in.nextInt();
            in.nextLine();
            switch (option) {
                case 1 -> new ReportsMenu().run(branch);
                case 2 -> new InventoryProductsManu().run(branch);
                case 3 -> new DiscountsManu().run(branch);
                case 4 -> new CategoriesMenu().run(branch);
                case 5 -> System.exit(0);
                default -> System.out.println("\nInvalid command");
            }
        }
    }
}

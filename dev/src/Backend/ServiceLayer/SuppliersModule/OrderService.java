package Backend.ServiceLayer.SuppliersModule;

import Backend.BusinessLayer.OrderController;
import Backend.BusinessLayer.Pair;
import Backend.BusinessLayer.Supplier;


import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import Backend.BusinessLayer.SupplierController;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class OrderService {
    private OrderController orderController;
    private Gson gson;
    public OrderService(){
        orderController = new OrderController();
        gson = new Gson();
    }

    public String order(Map<Integer, Integer> order){
        try {
            Map<String, Pair<Map<Integer, Integer>, Double>> fullOrder = orderController.order(order);
            Response<Map<String, Pair<Map<Integer, Integer>, Double>>> r = new Response<>(fullOrder);
            return gson.toJson(r);
        }
        catch (RuntimeException exception) {
            return gson.toJson(new Response<Map<String, Pair<Map<Integer, Integer>, Double>>>(exception.getMessage(), true));
        }

    }
}

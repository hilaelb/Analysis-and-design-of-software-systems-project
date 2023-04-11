package transportModule.backend.serviceLayer;

import transportModule.backend.businessLayer.ItemListsController;
import transportModule.records.ItemList;
import utils.JSON;
import utils.Response;

public class ItemListsService {
    private final ItemListsController ilc;

    public ItemListsService(ItemListsController ilc){
        this.ilc = ilc;
    }


    /**
     * @param json serialized ItemList with id -1
     * @return serialized {@link Response} object with the id of the added item list in the data field
     */
    public String addItemList(String json){
        ItemList itemList = ItemList.fromJson(json);
        try{
            Integer id = ilc.addItemList(itemList);
            return new Response("Item list added successfully", true, id).toJson();
        }catch(Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String removeItemList(String json){
        ItemList itemList = ItemList.fromJson(json);
        try{
            ilc.removeItemList(itemList.id());
            return new Response("Item list removed successfully", true).toJson();
        }catch(Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String updateItemList(String json){
        ItemList itemList = ItemList.fromJson(json);
        try{
            ilc.updateItemList(itemList.id(), itemList);
        }catch(Exception e){
            return Response.getErrorResponse(e).toJson();
        }
        return new Response("Item list updated successfully", true).toJson();
    }

    public String getItemList(String json){
        ItemList itemList = ItemList.fromJson(json);
        try{
            itemList = ilc.getItemList(itemList.id());
            return new Response("Item list found successfully", true, itemList).toJson();
        }catch(Exception e){
            return Response.getErrorResponse(e).toJson();
        }
    }

    public String getAllItemLists(){
        return new Response("Item lists found successfully", true, ilc.getAllItemLists()).toJson();
    }

}

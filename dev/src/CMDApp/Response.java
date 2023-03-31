package CMDApp;

import TransportModule.ServiceLayer.JSON;

public class Response<T> {

    private String message;
    private boolean success;
    private T data;

    public Response(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
        data = null;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }
    
    public String getJson(){
        return JSON.serialize(this);
    }
}

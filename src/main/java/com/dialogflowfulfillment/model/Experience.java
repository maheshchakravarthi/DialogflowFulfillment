package com.dialogflowfulfillment.model;



import java.io.Serializable;

public class Experience implements Serializable{

    private String name;
    private String status;

    public Experience(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Experience{" + "name='" + name + '\'' + ", status='" + status + "'}";
    }

}

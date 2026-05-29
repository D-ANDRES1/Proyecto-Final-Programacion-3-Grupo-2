package com.grupo2.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "trees")
public class MongoTree {

    @Id
    private String id;

    @Field("computer_name")
    private String computerName;

    @Field("manufacturer")
    private String manufacturer;

    public MongoTree() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getComputerName() { return computerName; }
    public void setComputerName(String computerName) { this.computerName = computerName; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
}
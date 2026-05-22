package com.grupo2.app.model;

import java.util.UUID;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trees")
public class TreeEntity {
	
	@Id
	
    private UUID id;
	

	
	private String computerName;

    private String manufacturer;
    
    
	
	public UUID getId() {
        return id;
    }
	
	public void setId(UUID id) {
		this.id = id;
	}

	public String getComputerName() {
		return computerName;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

}


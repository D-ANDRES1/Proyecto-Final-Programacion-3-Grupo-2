package com.grupo2.app.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trees")
public class TreeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	

	
	private String computerName;

    private String manufacturer;
    
    
	
	public UUID getId() {
        return id;
    }
	
	public void setId(UUID id) {
		this.id = id;
	}

}


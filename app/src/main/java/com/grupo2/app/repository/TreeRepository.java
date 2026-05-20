package com.grupo2.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo2.app.model.TreeEntity;

public interface TreeRepository extends JpaRepository<TreeEntity, UUID> {}

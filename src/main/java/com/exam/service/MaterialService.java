package com.exam.service;

import com.exam.entity.Material;

import java.util.List;

public interface MaterialService {
    int saveMaterial(Material material);
    List<Material> getAllMaterials();
    Material getMaterialByFileName(String fileName);
    Material getMaterialByStoredName(String storedName);
} 
package com.exam.serviceimpl;

import com.exam.entity.Material;
import com.exam.mapper.MaterialMapper;
import com.exam.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public int saveMaterial(Material material) {
        return materialMapper.insertMaterial(material);
    }

    @Override
    public List<Material> getAllMaterials() {
        return materialMapper.getAllMaterials();
    }

    @Override
    public Material getMaterialByFileName(String fileName) {
        return materialMapper.getMaterialByFileName(fileName);
    }

    @Override
    public Material getMaterialByStoredName(String storedName) {
        return materialMapper.getMaterialByStoredName(storedName);
    }
} 
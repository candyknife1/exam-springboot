package com.exam.mapper;

import com.exam.entity.Material;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MaterialMapper {
    @Insert("INSERT INTO material (file_name, stored_name, upload_time, file_size, uploader) VALUES (#{fileName}, #{storedName}, #{uploadTime}, #{fileSize}, #{uploader})")
    int insertMaterial(Material material);

    @Select("SELECT * FROM material ORDER BY upload_time DESC")
    List<Material> getAllMaterials();

    @Select("SELECT * FROM material WHERE file_name = #{fileName} LIMIT 1")
    Material getMaterialByFileName(@Param("fileName") String fileName);

    @Select("SELECT * FROM material WHERE stored_name = #{storedName} LIMIT 1")
    Material getMaterialByStoredName(@Param("storedName") String storedName);
} 
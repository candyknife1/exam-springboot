package com.exam.controller;

import com.exam.entity.Material;
import com.exam.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/material")
public class MaterialController {
    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB
    private static final List<String> ALLOWED_EXT = Arrays.asList("pdf", "doc", "docx", "ppt", "pptx", "jpg", "jpeg", "png");

    @Value("${material.upload.path:material}")
    private String materialUploadPath;

    @Autowired
    private MaterialService materialService;

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        Map<String, Object> result = new HashMap<>();
        if (file.isEmpty()) {
            result.put("success", false);
            result.put("message", "文件为空");
            return result;
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            result.put("success", false);
            result.put("message", "文件过大，最大20MB");
            return result;
        }
        String originalName = file.getOriginalFilename();
        String ext = StringUtils.getFilenameExtension(originalName);
        if (ext == null || !ALLOWED_EXT.contains(ext.toLowerCase())) {
            result.put("success", false);
            result.put("message", "不支持的文件类型");
            return result;
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String storedName = uuid + "." + ext;
        // 使用配置的文件上传路径
        File dir = new File(materialUploadPath);
        if (!dir.exists()) dir.mkdirs();
        File dest = new File(dir, storedName);
        file.transferTo(dest);
        Material material = new Material();
        material.setFileName(originalName);
        material.setStoredName(storedName);
        material.setUploadTime(new Date());
        material.setFileSize(file.getSize());
        // material.setUploader(当前登录用户); // 可选
        materialService.saveMaterial(material);
        result.put("success", true);
        result.put("message", "上传成功");
        result.put("fileName", originalName);
        return result;
    }

    @GetMapping("/list")
    public List<Map<String, Object>> list() {
        List<Material> materials = materialService.getAllMaterials();
        List<Map<String, Object>> res = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Material m : materials) {
            Map<String, Object> item = new HashMap<>();
            item.put("fileName", m.getFileName());
            item.put("uploadTime", sdf.format(m.getUploadTime()));
            item.put("fileSize", m.getFileSize());
            res.add(item);
        }
        return res;
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("fileName") String fileName) throws IOException {
        Material material = materialService.getMaterialByFileName(fileName);
        if (material == null) {
            return ResponseEntity.status(404).body(null);
        }
        String storedName = material.getStoredName();
        // 使用配置的文件上传路径
        File file = new File(materialUploadPath, storedName);
        if (!file.exists()) {
            return ResponseEntity.status(404).body(null);
        }
        byte[] data = Files.readAllBytes(file.toPath());
        String encodedName = URLEncoder.encode(material.getFileName(), "UTF-8").replaceAll("\\+", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", encodedName);
        return ResponseEntity.ok().headers(headers).body(data);
    }
} 
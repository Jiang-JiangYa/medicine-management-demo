package org.example.myspringboot.controller;
import org.example.myspringboot.common.Result;
import org.example.myspringboot.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.*;
import java.util.*;
@RestController
@RequestMapping("/file")
public class FileController {
    @Value("${app.upload-enabled:true}")
    private boolean uploadEnabled = true;
    private final Path root;
    public FileController(@Value("${app.upload-dir}") String dir) { root = Paths.get(dir).toAbsolutePath().normalize(); }
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (!uploadEnabled) throw new ServiceException("403", "免费演示环境不支持持久文件上传");
        String name = Optional.ofNullable(file.getOriginalFilename()).orElse("");
        String ext = name.substring(name.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
        if (file.isEmpty() || file.getSize() > 5 * 1024 * 1024 || !Arrays.asList("png", "jpg", "jpeg").contains(ext))
            throw new ServiceException("400", "仅支持 5MB 以内 PNG/JPEG 图片");
        try (InputStream in = file.getInputStream()) {
            if (ImageIO.read(in) == null) throw new ServiceException("400", "图片内容无效");
        }
        Files.createDirectories(root);
        String stored = UUID.randomUUID().toString() + "." + ext;
        try (InputStream in = file.getInputStream()) { Files.copy(in, root.resolve(stored)); }
        return Result.success("/api/file/download/" + stored);
    }
    @GetMapping("/download/{name}")
    public void download(@PathVariable String name, HttpServletResponse response) throws IOException {
        if (!name.matches("[0-9a-fA-F-]{36}\\.(png|jpg|jpeg)")) { response.sendError(404); return; }
        Path path = root.resolve(name).normalize();
        if (!path.startsWith(root) || !Files.isRegularFile(path) || Files.isSymbolicLink(path)) { response.sendError(404); return; }
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentType(name.endsWith("png") ? "image/png" : "image/jpeg");
        Files.copy(path, response.getOutputStream());
    }
}

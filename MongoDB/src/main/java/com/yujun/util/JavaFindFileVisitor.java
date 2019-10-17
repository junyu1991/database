package com.yujun.util;

import com.yujun.database.model.VideoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- admin 2019/10/15
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */
@Configuration
public class JavaFindFileVisitor extends FindFileVisitor {

    private List<VideoInfo> videoList;

    public JavaFindFileVisitor() {
        this.videoList = new LinkedList<>();
    }

    @Value("${file.mongo.collection}")
    private String collectionName;

    @Bean(name = "javaMongoFindFileVisitor")
    public JavaFindFileVisitor javaFindFileVisitor() {
        return this;
    }

    @Autowired
    @Qualifier("mongoTemplate")
    private MongoTemplate mongoTemplate;

    @Override
    protected Pattern getFilePattern() {
        String pa = "\\.(java|jsp|jspx)$";
        Pattern pattern = Pattern.compile(pa, Pattern.CASE_INSENSITIVE);
        return pattern;
    }

    @Override
    protected Pattern getDirectoryPattern() {
        return null;
    }

    @Override
    protected void handleFindedFile(Path path, BasicFileAttributes attrs) {
        VideoInfo fileInfo = new VideoInfo();
        fileInfo.setFilename(path.getFileName().toString());
        fileInfo.setFilePath(path.toAbsolutePath().toString());
        fileInfo.setFileMd5(com.yujun.security.FileUtil.fileMd5Sum(path));
        try {
            fileInfo.setFileSize(Files.size(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (this.videoList) {
            this.videoList.add(fileInfo);
            if (this.videoList.size() >= 100) {
                this.mongoTemplate.insert(this.videoList, this.collectionName);
                this.videoList.clear();
            }
        }
    }

    @Override
    protected void handleFindedDirectory(Path dir, BasicFileAttributes attrs) {

    }
}

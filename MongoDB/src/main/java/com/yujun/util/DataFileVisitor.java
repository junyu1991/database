package com.yujun.util;

import com.yujun.database.model.FileInfo;
import com.yujun.security.FileUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

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
@Getter
@Slf4j
public class DataFileVisitor implements FileVisitor<Path> {

    /** 用于存储扫描到的文件 **/
    private List<FileInfo> files;

    public DataFileVisitor() {
        this.files = new ArrayList<FileInfo>();
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        log.info("Visiting directory:" + dir.toAbsolutePath().toString());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        log.info("Visiting file:" + file.toAbsolutePath().toString());
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(file.getFileName().toString());
        fileInfo.setFilePath(file.toAbsolutePath().toString());
        fileInfo.setFileMd5(FileUtil.fileMd5Sum(file));
        fileInfo.setFileSize(Files.size(file));
        synchronized (this.files) {
            this.files.add(fileInfo);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        log.info("Visiting file failed:" + file.toAbsolutePath().toString());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        log.info("After visit directory:" + dir.toAbsolutePath().toString());
        return FileVisitResult.CONTINUE;
    }
}

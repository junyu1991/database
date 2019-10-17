package com.yujun.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- yujun 2019/10/15
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */
@Slf4j
@Configuration
public abstract class FindFileVisitor implements FileVisitor<Path> {
    /**
     * 获取指定文件名正则表达式，用于匹配文件名
     * @author: yujun
     * @date: 2019/10/15
     * @description: TODO
     * @param
     * @return: {@link String}
     * @exception:
    */
    protected abstract Pattern getFilePattern();

    /**
     * 获取指定文件名正则表达式，用于匹配文件夹名
     * @author: yujun
     * @date: 2019/10/15
     * @description: TODO
     * @param
     * @return: {@link String}
     * @exception:
    */
    protected abstract Pattern getDirectoryPattern();

    /**
     * 处理文件名符合getPattern()正则表达式的文件
     * @author: yujun
     * @date: 2019/10/15
     * @description: TODO
     * @param path
     * @param attrs
     * @return:
     * @exception:
    */
    protected abstract void handleFindedFile(Path path, BasicFileAttributes attrs);

    /**
     * 处理文件名符合getPattern()正则表达式的文件夹
     * @author: yujun
     * @date: 2019/10/15
     * @description: TODO
     * @param dir
     * @param attrs
     * @return:
     * @exception:
    */
    protected abstract void handleFindedDirectory(Path dir, BasicFileAttributes attrs);

    @Bean(name="findFileVisitor")
    public FindFileVisitor findFileVisitor() {
        return this;
    }

    @Override
    public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(dir);
        if(this.getDirectoryPattern() != null) {
            //Pattern pattern = Pattern.compile(getDirectoryPattern(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = getDirectoryPattern().matcher(dir.getFileName().toString());
            if (matcher.find()) {
                log.info("Find directory:" + dir.toString());
                this.handleFindedDirectory(dir, attrs);
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(file);
        if(this.getFilePattern() != null) {
//            Pattern pattern = Pattern.compile(getFilePattern(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = getFilePattern().matcher(file.getFileName().toString());
            if (matcher.find()) {
                log.info("Find file:" + file.toString());
                this.handleFindedFile(file, attrs);
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        Objects.requireNonNull(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        Objects.requireNonNull(dir);
        return FileVisitResult.CONTINUE;
    }
}

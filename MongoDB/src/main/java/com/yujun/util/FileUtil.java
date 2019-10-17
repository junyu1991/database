package com.yujun.util;

import com.yujun.database.model.FileInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
public class FileUtil {

    /**
     * 遍历指定文件夹，返回所有的文件列表
     * @author: yujun
     * @date: 2019/10/15
     * @description: TODO
     * @param path
     * @return: {@link List< FileInfo>}
     * @exception:
    */
    public static void walkFile(String path, FileVisitor<Path> fileVisitor) {
        try {
            Files.walkFileTree(Paths.get(path), fileVisitor);
        } catch (IOException e) {
            log.error("Traversing directory " + path + " failed.", e);
            e.printStackTrace();
        }
    }

}

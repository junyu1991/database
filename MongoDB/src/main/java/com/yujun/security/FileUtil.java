package com.yujun.security;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
     * 计算文件path的md5值
     * @author: yujun
     * @date: 2019/10/15
     * @description: TODO 
     * @param path 需要计算md5值的文件，必须是常规文件，不能是文件夹，如果是Symbolic link，则链接的文件也必须是常规文件
     * @return: {@link String}
     * @exception: 
    */
    public static String fileMd5Sum(Path path) throws IllegalArgumentException {
        if(!Files.isRegularFile(path)){
            throw new IllegalArgumentException("File must be a file, can not be a directory or a Symbolic link");
        }
        MessageDigest digest = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            InputStream inputStream = Files.newInputStream(path);
            DigestInputStream digestInputStream = new DigestInputStream(inputStream, digest);
            len = digestInputStream.read(buffer);
            while(len != -1) {
                len = digestInputStream.read(buffer);
            }
            byte[] digest1 = digest.digest();
            String fx = "%0" + (digest.getDigestLength()*2) + "x";
            return String.format(fx, new BigInteger(1, digest1));
        } catch (Exception e) {
            log.error("计算文件[" + path.toAbsolutePath().toString() + "] md5 失败", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算文件path的md5值
     * @author: yujun
     * @date: 2019/10/15
     * @description: TODO
     * @param fileName 需要计算md5值的文件，必须是常规文件，不能是文件夹，如果是Symbolic link，则链接的文件也必须是常规文件
     * @return: {@link String}
     * @exception:
     */
    public static String fileMd5Sum(String fileName) throws IllegalArgumentException {
        Path path = Paths.get(fileName);
        return fileMd5Sum(path);
    }

    /**
     * 计算文件path的md5值
     * @author: yujun
     * @date: 2019/10/15
     * @description: TODO
     * @param file 需要计算md5值的文件，必须是常规文件，不能是文件夹，如果是Symbolic link，则链接的文件也必须是常规文件
     * @return: {@link String}
     * @exception:
     */
    public static String fileMd5Sum(File file) throws IllegalArgumentException{
        return fileMd5Sum(file.toPath());
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(fileMd5Sum("C:/Users/admin/Desktop/pie-legend.html"));
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update("test".getBytes());
        BigInteger bigInteger = new BigInteger(1, digest.digest());
        System.out.println(bigInteger.toString());
    }
}

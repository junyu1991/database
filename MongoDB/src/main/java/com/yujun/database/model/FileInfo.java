package com.yujun.database.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

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
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {

    /** 文件名 **/
    private String filename;
    /** 文件所在文件路径，可能会随时发生变化 **/
    private String filePath;
    /** 文件md5值 **/
    private String fileMd5;
    /** 文件大小 **/
    private long fileSize;

}

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
 * ---- yujun 2019/10/14
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
@NoArgsConstructor
@AllArgsConstructor
public class VideoInfo extends FileInfo {
    /** 视频类型，如电影，电视剧 **/
    private int type;
    /** 视频文件类型，如MP4，MKV等 **/
    private int videoType;
    /** 视频文件长度 **/
    private long videoLength;
}

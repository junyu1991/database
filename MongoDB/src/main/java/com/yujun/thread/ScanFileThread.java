package com.yujun.thread;

import com.yujun.util.FileUtil;
import com.yujun.util.FindFileVisitor;
import com.yujun.util.JavaFindFileVisitor;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- admin 2019/10/16
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */
public class ScanFileThread extends Thread {

    private String path;
    private FindFileVisitor findFileVisitor;

    public ScanFileThread(String path, FindFileVisitor findFileVisitor, String name){
        super(name);
        this.path = path;
        this.findFileVisitor = findFileVisitor;
    }

    @Override
    public void run() {
        FileUtil.walkFile(path, findFileVisitor);
    }
}

package com.yujun.http;

import com.yujun.database.model.FileInfo;
import com.yujun.database.mongodb.file.dao.FileActionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <b>修改记录：</b>
 * <p>
 * <li>
 * <p>
 * ---- admin 2019/10/17
 * </li>
 * </p>
 *
 * <b>类说明：</b>
 * <p>
 *
 * </p>
 */
@RestController
@RequestMapping("/file")
@ComponentScan(basePackages = {"com.yujun"})
public class FileServlet {

    @Autowired
    @Qualifier("fileActionDao1")
    private FileActionDao fileActionDao;

    @RequestMapping("/query")
    public List<FileInfo> queryFileInfo(HttpServletRequest request, Long fileSize, String filename, Integer querySize, String orderFiled) {
        return fileActionDao.queryFileInfo(querySize, fileSize, filename, orderFiled);
    }

    @RequestMapping("/query-page")
    public List<FileInfo> queryFileInfo(HttpServletRequest request, Integer pageSize, Integer pageNo, String filename, String orderFiled) {
        return fileActionDao.queryPageFileInfo(pageSize, pageNo, filename, orderFiled);
    }
    @RequestMapping("/index")
    public String index() {
        return "SUCCESS";
    }

    @RequestMapping("/distinct-filename")
    public List<FileInfo> distinctFileNameBySize (Integer fileSize) {
        return fileActionDao.queryFileInfoByFileSize(fileSize);
    }

}

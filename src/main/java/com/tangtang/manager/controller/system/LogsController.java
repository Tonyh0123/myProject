package com.tangtang.manager.controller.system;

import com.tangtang.manager.common.utils.LogsUtils;
import com.tangtang.manager.dto.NoticeSerachDTO;
import com.tangtang.manager.pojo.BaseNotice;
import com.tangtang.manager.response.PageDataResult;
import com.tangtang.manager.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: NoticeController
 * @Description: 日志管理
 * @author: tangtang
 * @version: 1.0
 * @date: 2020/04/19 11：57
 */
@Controller
@RequestMapping("logs")
public class LogsController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     *
     * 功能描述: 跳到日志显示界面
     *
     * @param:
     * @return:
     * @auther: tangtang
     * @date: 2020/04/19 11：58
     */
    @RequestMapping("/logsPage")
    public String userManage() {
        return "/logsPage";
    }

    /**
     * 查看系统运行日志
     * @Author tangtang
     * @return
     */
    @PostMapping("checkLogs")
    @ResponseBody
    public List<String> checkLogs() {
        LogsUtils logsUtils = new LogsUtils();
        logger.info("查看系统运行日志！");
        ArrayList<String> listFileName = new ArrayList<String>();
        logsUtils.getAllFileName("C:\\Users\\Adminstrador\\logs\\",listFileName);
        List<String> list = new ArrayList<>();
        for(String name:listFileName){
            if(name.endsWith(".gz")){
                logsUtils.doUncompressFile(name); //解压缩.gz文件
            }
        }
        logsUtils.getAllFileName("C:\\Users\\Adminstrador\\logs\\",listFileName);
        for(String name:listFileName){
            if(name.endsWith(".0")){
//                doUncompressFile(name); 解压缩.gz文件
                logsUtils.readFile(name,list);
            }else if (name.endsWith(".log")){
                logsUtils.readFile(name,list);
            }
        }
        Map<String, Object> data = new HashMap<>();

        return list;
    }


}
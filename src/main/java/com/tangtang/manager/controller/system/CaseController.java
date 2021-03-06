package com.tangtang.manager.controller.system;

import com.tangtang.manager.dao.BaseSuccessfulCaseMapper;
import com.tangtang.manager.dto.LoginDTO;
import com.tangtang.manager.dto.SchoolRegistrationDTO;
import com.tangtang.manager.dto.StudentRegistrationDTO;
import com.tangtang.manager.dto.UserSearchDTO;
import com.tangtang.manager.pojo.BaseAdminRole;
import com.tangtang.manager.pojo.BaseAdminUser;
import com.tangtang.manager.pojo.BaseSuccessfulCase;
import com.tangtang.manager.response.PageDataResult;
import com.tangtang.manager.service.AdminUserService;
import com.tangtang.manager.service.CaseService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: CaseController
 * @Description: 成功案例管理
 * @author: tangtang
 * @version: 1.0
 * @date: 2020/03/03 11：57
 */
@Controller
@RequestMapping("case")
public class CaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private CaseService caseService;


    /**
     *
     * 功能描述: 跳到成功案例列表
     *
     * @param:
     * @return:
     * @auther: tangtang
     * @date: 2020/03/03 11：58
     */
    @RequestMapping("/caseManage")
    public String userManage() {
        return "/case/caseManage";
    }

    /**
     *
     * 功能描述: 跳到案例展示界面
     *
     * @param:
     * @return:
     * @auther: tangtang
     * @date: 2020/03/03 11：58
     */
    @RequestMapping("/caseShow")
    public String caseShow() {
        return "/case/caseShow";
    }

    /**
     *
     * 功能描述: 获取案例展示数据
     *
     * @param:
     * @return:
     * @auther: tangtang
     * @date: 2020/03/06 16:28
     */
    @GetMapping("caseShowData")
    public @ResponseBody List<BaseSuccessfulCase> getCaseShowData(){
        logger.info("获取案例展示数据");
        return caseService.getCaseShowData();
    }

    /**
     *
     * 功能描述: 分页查询成功案例列表
     *
     * @param:
     * @return:
     * @auther: tangtang
     * @date: 2020/03/03 11：59
     */
    @RequestMapping(value = "/getCaseList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getUserList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,/*@Valid PageRequest page,*/ BaseSuccessfulCase successfulCase) {
        /*logger.info("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",pageNum:" + page.getPageNum()
                + ",每页记录数量pageSize:" + page.getPageSize());*/
        PageDataResult pdr = new PageDataResult();
        try {
            if(null == pageNum) {
                pageNum = 1;
            }
            if(null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = caseService.getCaseList(successfulCase, pageNum ,pageSize);
            logger.info("案例列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("案例列表查询异常！", e);
        }
        return pdr;
    }

    /**
     * 新增成功案例
     * @param successfulCase
     * @return
     */
    @RequestMapping(value = "/addCase", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addCase(BaseSuccessfulCase successfulCase) {

        Map<String,Object> data = new HashMap();
        if(successfulCase.getId() == null){
            logger.info("新增成功案例 ------> successfulCase:" + successfulCase);
            data = caseService.addCase(successfulCase);
        }else{
            logger.info("编辑成功案例 ------> successfulCase:" + successfulCase);
            data = caseService.updateCase(successfulCase);
        }

        return data;
    }


    /**
     * 删除成功案例
     * @Author tangtang
     * @param id
     * @return
     */
    @PostMapping("del")
    @ResponseBody
    public Map<String, Object> del(@RequestParam("id") Long id) {
        logger.info("删除成功案例！id:" + id);
        Map<String, Object> data = new HashMap<>();
        data = caseService.del(id);
        return data;
    }






























    /**
     *
     * 功能描述: 新增和更新系统用户
     *
     * @param:
     * @return:
     * @auther: tangtang
     * @date: 2020/03/10 10:14
     */
    @RequestMapping(value = "/setUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setUser(BaseAdminUser user) {
        logger.info("设置用户[新增或更新]！user:" + user);
        Map<String,Object> data = new HashMap();
        System.out.println("data"+data);
        if(user.getId() == null){
            data = adminUserService.addUser(user);
        }else{
            data = adminUserService.updateUser(user);
        }
        return data;
    }


    /**
     * 注册用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/regUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> regUser(StudentRegistrationDTO user) {
        logger.info("设置用户[注册用户]！user:" + user);
        Map<String,Object> data = new HashMap();
        System.out.println("data"+data);
        data = adminUserService.regUser(user);
        data.put("url","/login");
        return data;
    }


    /**
     * 院校申请使用
     * @param user
     * @return
     */
    @RequestMapping(value = "/regSchoolUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> regSchoolUser(SchoolRegistrationDTO user) {
        logger.info("院校申请使用 ------> user:" + user);
        Map<String,Object> data = new HashMap();
        data = adminUserService.regSchoolUser(user);
        data.put("url","/login");
        return data;
    }

    /**
     *
     * 功能描述: 删除/恢复 用户
     *
     * @param:
     * @return:
     * @auther: tangtang
     * @date: 2020/03/10 11:59
     */
    @RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateUserStatus(@RequestParam("id") Integer id,@RequestParam("status") Integer status) {
        logger.info("删除/恢复用户！id:" + id+" status:"+status);
        Map<String, Object> data = new HashMap<>();
        if(status == 0){
            //删除用户
            data = adminUserService.delUser(id,status);
        }else{
            //恢复用户
            data = adminUserService.recoverUser(id,status);
        }
        return data;
    }

    @RequestMapping(value = "/userIsExist", method = RequestMethod.POST, consumes="application/json")
    @ResponseBody
    public Boolean userIsExist(@RequestBody BaseAdminUser user) {
        logger.info("注册用户[检测用户名是否存在]！username:" + user.getSysUserName());
        String username = user.getSysUserName();
        System.out.println(username);
        //根据用户名查询管理员(包括status为0的   以防恢复引起bug)
        BaseAdminUser admin1=adminUserService.findByUserName(username);
        if(admin1==null)
        {
            System.out.println(111);
            //返回true则为没有该用户名可以被注册
            return true;
        }else {
            System.out.println(222);
            return false;
        }
    }


}

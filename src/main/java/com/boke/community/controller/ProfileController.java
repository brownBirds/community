package com.boke.community.controller;

import com.boke.community.dto.NotificationDTO;
import com.boke.community.dto.QuestionDTO;
import com.boke.community.mapper.UserMapper;
import com.boke.community.model.User;
import com.boke.community.service.NotificationService;
import com.boke.community.service.QuestionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {
    @Resource
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @Resource
    private  UserMapper userMapper;
    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @RequestParam(defaultValue = "1", value = "pageNum") int pageNum,
                          @RequestParam(defaultValue = "7", value = "pageSize") int pageSize,
                          Model model, @PathVariable(name = "action") String action){
        User user=(User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }
        PageHelper.startPage(pageNum, pageSize);
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            List<QuestionDTO> questionDTO=questionService.listById(user.getId());
            PageInfo<QuestionDTO> pageInfo= new PageInfo<QuestionDTO>(questionDTO);
            model.addAttribute("pageInfo",pageInfo);
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            List<NotificationDTO> notificationDTOS = notificationService.list(user.getId().longValue());
            PageInfo<NotificationDTO> pageInfo=new PageInfo<NotificationDTO>(notificationDTOS);
            model.addAttribute("pageInfo",pageInfo);
        }

         return "profile";

    }

}

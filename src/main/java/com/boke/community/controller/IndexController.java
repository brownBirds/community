package com.boke.community.controller;

import com.boke.community.dto.QuestionDTO;
import com.boke.community.mapper.UserMapper;
import com.boke.community.service.QuestionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Resource
    private UserMapper userMapper;
    @Resource
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        @RequestParam(defaultValue="1",value="pageNum") int pageNum,
                        @RequestParam(defaultValue = "6",value="pageSize") int pageSize,
                        Model model)
    {

        PageHelper.startPage(pageNum, pageSize);
        List<QuestionDTO> questionDTO=questionService.list();
        PageInfo<QuestionDTO> pageInfo= new PageInfo<QuestionDTO>(questionDTO);
        model.addAttribute("pageInfo",pageInfo);
        return "index";
    }

}

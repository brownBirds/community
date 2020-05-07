package com.boke.community.controller;

import com.boke.community.dto.CommentDTO;
import com.boke.community.dto.QuestionDTO;
import com.boke.community.enums.CommentTypeEnum;
import com.boke.community.model.Question;
import com.boke.community.service.CommentService;
import com.boke.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long questionId, Model model){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(questionId, CommentTypeEnum.QUESTION);
        QuestionDTO questionDTO = questionService.getById(Math.toIntExact(questionId));
        List<Question> relatedQuestions = questionService.selectRelated(questionDTO.getId(),questionDTO.getTag());
        questionService.incView(questionId);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments",commentDTOS);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}

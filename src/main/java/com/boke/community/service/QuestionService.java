package com.boke.community.service;

import com.boke.community.dto.QuestionDTO;
import com.boke.community.exception.CustomizeErrorCode;
import com.boke.community.exception.CustomizeException;
import com.boke.community.mapper.QuestionMapper;

import com.boke.community.mapper.UserMapper;
import com.boke.community.model.Question;
import com.boke.community.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class QuestionService {

    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private UserMapper userMapper;

    public List<QuestionDTO> list() {
        List<QuestionDTO> questionDTOList = questionMapper.list();
        return questionDTOList;
    }
    public List<QuestionDTO> listById(Integer id) {
        List<QuestionDTO> questionDTOS = questionMapper.listById(id);
        return questionDTOS;
    }


    public QuestionDTO getById(Integer questionId) {

        QuestionDTO questionDTO=questionMapper.getById(questionId);
        if (questionDTO==null)
        {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.findByCreate(questionDTO.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void creatOrUpdate(Question question) {
        if (question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.creat(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            int updated=  questionMapper.update(question);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }

    }

    public void incView(long questionId) {
        Question question = new Question();
        question.setId(questionId);
        question.setViewCount(1);
        questionMapper.incView(question);
    }

    public List<Question> selectRelated(Long id, String tag) {
        if (tag.isEmpty()) {
            return new ArrayList<>();
        }
        String tags = tag.replace(",","，").replace("，","|");
        List<Question> questionDTOS = questionMapper.selectRelated(id, tags);
        return  questionDTOS;
    }
}

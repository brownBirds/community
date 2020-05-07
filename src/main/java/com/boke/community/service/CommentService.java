package com.boke.community.service;

import com.boke.community.dto.CommentDTO;
import com.boke.community.enums.CommentTypeEnum;
import com.boke.community.enums.NotificationStatusEnum;
import com.boke.community.enums.NotificationTypeEnum;
import com.boke.community.exception.CustomizeErrorCode;
import com.boke.community.exception.CustomizeException;
import com.boke.community.mapper.CommentMapper;
import com.boke.community.mapper.NotificationMapper;
import com.boke.community.mapper.QuestionMapper;
import com.boke.community.mapper.UserMapper;
import com.boke.community.model.Comment;
import com.boke.community.model.Notification;
import com.boke.community.model.Question;
import com.boke.community.model.User;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment dbComment = commentMapper.getByLongId(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 回复问题
            Question question = questionMapper.getByLongId(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.Insert(comment);

            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentMapper.incCommentCount(parentComment);
            creatNotification(comment, dbComment.getCommentator().longValue(), commentator.getName(), question.getTitle(),NotificationTypeEnum.REPLY_COMMENT);
        }else {

            // 回复问题
            Question question = questionMapper.getByLongId(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.Insert(comment);
            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            questionMapper.incCommentCount(parentComment);
            creatNotification(comment,question.getCreator().longValue(), commentator.getName(), question.getTitle(),NotificationTypeEnum.REPLY_QUESTION);
        }


    }

    private void creatNotification(Comment comment, long receiver,String notifierName, String outerTitle, NotificationTypeEnum replyComment) {

        //快捷键 ctrl+alt+m 对一块代码进行重构
        //回复通知
//        if (receiver == comment.getCommentator()) {
//            return;
//        }
        Notification notification=new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(replyComment.getType());
        notification.setNotifier(comment.getCommentator().longValue());
        notification.setReceiver(receiver);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setOuterid(comment.getParentId());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {

        List<Comment> comments = commentMapper.listById(Math.toIntExact(id),type.getType());

        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 获取去重的评论人
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList();
        userIds.addAll(commentators);


        // 获取评论人并转换为 Map
        List<User> users = userMapper.ListById(userIds);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));


        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }




}

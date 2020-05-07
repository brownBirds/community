package com.boke.community.service;

import com.boke.community.dto.NotificationDTO;
import com.boke.community.enums.NotificationStatusEnum;
import com.boke.community.enums.NotificationTypeEnum;
import com.boke.community.exception.CustomizeErrorCode;
import com.boke.community.exception.CustomizeException;
import com.boke.community.mapper.NotificationMapper;
import com.boke.community.model.Notification;
import com.boke.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {


    @Autowired
    private NotificationMapper notificationMapper;

    public List<NotificationDTO> list(Long userId) {
        List<Notification> notifications = notificationMapper.selectListByReceiver(userId);

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        return notificationDTOS;
    }
    public Long unreadCount(Long userId) {
        Notification notification=new Notification();
        notification.setReceiver(userId);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByUnread(notification);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId().longValue())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }



}

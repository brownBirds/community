package com.boke.community.mapper;

import com.boke.community.model.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface NotificationMapper {


    @Insert("Insert into Notification (notifier,receiver,outerid,type,gmt_create,status,NOTIFIER_name,outer_title) values (#{notifier},#{receiver},#{outerid},#{type},#{gmtCreate},#{status},#{notifierName},#{outerTitle})")
    void  insert(Notification notification);


    @Select("select * from Notification where receiver=#{userid} order by gmt_create desc")
    List<Notification> selectListByReceiver(Long userId);

    @Select("select * from Notification where id=#{id}")
    Notification selectByPrimaryKey(Long id);

    @Update("update Notification set status=#{status} where id=#{id}")
    void updateByPrimaryKey(Notification notification);

    @Select("select  COUNT(*) from Notification where receiver=#{receiver} and status=#{status}")
    Long countByUnread(Notification notification);
}

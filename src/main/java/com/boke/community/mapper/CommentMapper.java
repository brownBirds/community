package com.boke.community.mapper;

import com.boke.community.enums.CommentTypeEnum;
import com.boke.community.model.Comment;


import com.boke.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface CommentMapper {

    @Insert("Insert into comment (parent_id,Type,commentator,gmt_create,gmt_modified,like_count,content) values (#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    int Insert(Comment comment);

    @Select("select * from comment where parent_id=#{id} and type=#{type} Order by gmt_create desc")
    List<Comment> listById(@Param("id") Integer id, @Param("type") Integer type);

    @Select("select * from comment where ID=#{parentId} ")
    Comment getByLongId(@Param("parentId") Long parentId);

    @Update("Update COMMENT set Comment_count=Comment_count+#{CommentCount} where id=#{id}")
    void incCommentCount(Comment parentComment);
}
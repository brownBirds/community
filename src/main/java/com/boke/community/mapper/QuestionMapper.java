package com.boke.community.mapper;

import com.boke.community.dto.QuestionDTO;
import com.boke.community.model.Comment;
import com.boke.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
   @Insert("insert into question (title,gmt_create,gmt_modified,creator,comment_count,view_count,like_count,tag,description) " +
           "values (#{title},#{gmtCreate},#{gmtModified},#{creator},#{commentCount},#{viewCount},#{likeCount},#{tag},#{description})")
   void creat(Question question);

   @Select("select t1.*,t2.avatar_url avatarUrl from question t1 left join user t2 on t1.creator=t2.id order by gmt_create desc")
   @Results({
           @Result(property = "user.avatarUrl",column = "avatarUrl")
   })
   List<QuestionDTO> list();

   @Select("select t1.*,t2.avatar_url avatarUrl from question t1 left join user t2 on t1.creator=t2.id and t1.creator=#{id} ")
   @Results({
           @Result(property = "user.avatarUrl",column = "avatarUrl")
   })
   List<QuestionDTO> listById(@Param("id") Integer id);

   @Select("select * from question where ID=#{questionId} ")
   QuestionDTO getById(Integer questionId);

   @Update("Update question set title=#{title},gmt_modified=#{gmtModified},description=#{description},tag=#{tag} where id=#{id}" )
   int update(Question question);

   @Update("Update question set view_count=view_count+#{viewCount} where id=#{id}")
   void incView(Question question);

   @Select("select * from question where ID=#{parentId} ")
   Question getByLongId(Long parentId);

   @Update("Update question set Comment_count=Comment_count+#{CommentCount} where id=#{id}")
   void incCommentCount(Comment parentComment);

   @Select("select * from QUESTION where id != #{id} and tag regexp #{tag} order by gmt_create desc limit 20")
   List<Question> selectRelated(@Param("id") Long id, @Param("tag") String tag);


}

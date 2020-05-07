package com.boke.community.mapper;

import com.boke.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("Insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    public void insert(User user);
    @Select("select * from user where token=#{token}")
    public User findByToken(@Param("token") String token);

    @Select("select * from user where ID=#{creator}")
    User findByCreate(@Param("creator")Long creator);

    @Select("select * from user where account_Id=#{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set gmt_modified=#{gmtModified},avatar_url=#{avatarUrl},name=#{name},token=#{token} where id=#{id}")
    void update(User updateUser);

    @Select(" <script>" +
            "select *" +
            " from user where  id  in "+
            " <foreach collection='id' open='(' item='id' separator=',' close=')'> #{id}</foreach> "+
            " </script>")
    List<User> ListById(@Param("id") List<Integer> id);
}

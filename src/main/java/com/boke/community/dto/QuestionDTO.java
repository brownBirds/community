package com.boke.community.dto;

import com.boke.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;

    private String title;

    private Long gmtCreate;

    private Long gmtModified;

    private Long creator;

    private Integer commentCount;

    private Integer viewCount;

    private Integer likeCount;

    private String tag;

    private String description;

    private String avatarUrl;

    private User user;
}

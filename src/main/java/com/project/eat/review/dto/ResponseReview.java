package com.project.eat.review.dto;

import com.project.eat.review.entity.ReviewPic;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ResponseReview {

    private int reviewId;
    private String reviewComment;
    private List<ReviewPic> reviewPics;
    private Long shopId;
    private Long memberId;
    private int reviewStar;
    private String shopName;
    private LocalDateTime createdAt;


}

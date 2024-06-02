package com.project.eat.review.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class RequestReview {

    private int reviewStar;
    private String reviewComment;
    private List<MultipartFile> files;
    private Long shopId;
    private Long orderId;

}

package com.project.eat.review.service;

import com.project.eat.review.entity.ReviewPic;
import com.project.eat.review.repository.ReviewPicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewPicService {

    private final ReviewPicRepository reviewPicRepository;

    public void saveAll(List<ReviewPic> reviewPics) {
        reviewPicRepository.saveAll(reviewPics);
    }
}

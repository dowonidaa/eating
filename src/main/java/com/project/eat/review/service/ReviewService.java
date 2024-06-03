package com.project.eat.review.service;

import com.project.eat.order.Order;
import com.project.eat.order.OrderService;
import com.project.eat.review.dto.RequestReview;
import com.project.eat.review.entity.ReviewPic;
import com.project.eat.review.entity.ReviewVO;
import com.project.eat.review.repository.ReviewRepository;
import com.project.eat.shop.ShopService;
import com.project.eat.shop.ShopVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {


    private final ImageStore imageStore;
    private final ReviewPicService reviewPicService;
    private final ReviewRepository reviewRepository;
    private final OrderService orderService;

    @Transactional
    public void insertReview(RequestReview request){
        List<ReviewPic> reviewPics = imageStore.storeFiles(request.getFiles());
        Order joinOrder = orderService.findOrderJoinMemberJoinShopById(request.getOrderId());
        reviewPicService.saveAll(reviewPics);
        ReviewVO reviewVO = getReviewVO(request, reviewPics, joinOrder);
        reviewRepository.save(reviewVO);
        for (ReviewPic reviewPic : reviewPics) {
            reviewPic.setReviewVO(reviewVO);
        }
        reviewPicService.saveAll(reviewPics);
    }




    private ReviewVO getReviewVO(RequestReview request, List<ReviewPic> reviewPics, Order joinOrder) {
        ReviewVO reviewVO = new ReviewVO();
        reviewVO.setReviewStar(request.getReviewStar());
        reviewVO.setReviewComent(request.getReviewComment());
        reviewVO.setOrder(joinOrder);
        reviewVO.setShop(joinOrder.getShop());
        reviewVO.setName(joinOrder.getMember().getName());
        reviewVO.setReviewStar(request.getReviewStar());
        reviewVO.setShopName(joinOrder.getShop().getShopName());
        reviewVO.setShopId(joinOrder.getShop().getShopId());
        return reviewVO;
    }

    // by shopId 리뷰리스트 전체조회
    public List<ReviewVO> selectListByShopId(Long shopId, int cpage, int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;
        return reviewRepository.findReviewListByShopId(shopId,startRow-1, pageBlock);
    }

    // by shopId 리뷰리스트 총갯수
    public long getTotalRowsByShopId(Long shopId){
        return reviewRepository.countByShopId(shopId);
    }

    // by userId 마이페이지에서 나의 리뷰리스트 총갯수
    public long getTotalRowsByUserId(int userId){
        return reviewRepository.countByUserId(userId);
    }


    public int findUserIdByMemberId(String memberId) {
        return reviewRepository.findUserIdByMemberId(memberId);
    }

    public List<ReviewVO> selectListByUserId(int userId, int cpage, int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;
        return reviewRepository.findAllByUserId(userId, startRow-1, pageBlock);
    }

    public ReviewVO findOneByReviewId(int reviewId) {
        return reviewRepository.findOneByReviewId(reviewId);
    }

    public void deleteByReviewId(int reviewId){
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public void update(ReviewVO updateReview) {
        ReviewVO findReview = reviewRepository.findOneByReviewId(updateReview.getReviewId());
        findReview.setReviewComent(updateReview.getReviewComent());
        findReview.setReviewStar(updateReview.getReviewStar());
        findReview.setShopName(updateReview.getShopName());

    }

    @Transactional
    public void updateReview(RequestReview request) {
        ReviewVO findReview = reviewRepository.findOneByReviewId(request.getReviewId());
        findReview.setReviewComent(request.getReviewComment());
        findReview.setReviewStar(request.getReviewStar());

    }
}

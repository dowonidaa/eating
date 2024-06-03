package com.project.eat.review.controller;

import com.project.eat.cart.CartService;
import com.project.eat.member.MemberService;
import com.project.eat.member.MemberVO_JPA;
import com.project.eat.review.dto.RequestReview;
import com.project.eat.review.entity.ReviewVO;
import com.project.eat.review.service.ReviewPicService;
import com.project.eat.review.service.ReviewService;
import com.project.eat.review.word.IWordAnalysisService;
import com.project.eat.shop.ShopRepository;
import com.project.eat.shop.ShopService;
import com.project.eat.shop.ShopVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

// 삭제


@Controller
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ShopRepository shopRepository;
    private final ShopService shopService;
    private final CartService cartService;
    private final ReviewPicService uploadService;
    @Resource(name = "WordAnalysisService")
//    @Resource(name="WordAnalysisService")
    private final IWordAnalysisService wordAnalysisService;
    private final MemberService memberService;

    // 리뷰 폼 페이지
    @GetMapping("/review_formPage")
    public String review_formPage() {

        return "thymeleaf/review/reviewFormPage";
    }

    // 리뷰 폼 페이지
    // POST 방식으로 리뷰 폼 페이지 요청
    @PostMapping("/review_formPage")
    public String reviewFormPage(@RequestParam(name = "shopId") Long shopId,
                                 @RequestParam(name = "itemsName") String itemsName,
                                 @RequestParam(name = "orderId") Long orderId,
                                 Model model) {
        // 필요한 데이터를 모델에 추가
        model.addAttribute("shopId", shopId);
        log.info("shopId:{}", shopId);
        model.addAttribute("itemsName", itemsName);
        model.addAttribute("orderId", orderId);
        log.info("itemsName:{}", itemsName);
        return "thymeleaf/review/reviewFormPage";
    }

//    @GetMapping("/review_formPage")
//    public String review_formPage(){
//
//        return "thymeleaf/review/reviewFormPage";
//    }


    // 리뷰폼ok 처리 :form input post방식
    @PostMapping("/review_formdata")
    public String review_formdata(@ModelAttribute RequestReview request) throws Exception {
        boolean checkDoBadWord = wordAnalysisService.checkdoBadWord(request.getReviewComment());
        if (!checkDoBadWord) {
            request.setReviewComment("해당 리뷰는 관리자에 의해 보이지 않습니다");
        }
        reviewService.insertReview(request);
        shopRepository.updateReviewCount();

        return "main";
    }

    // by shopId : 페이지
    @GetMapping("/review_formdata_get")
    public String review_formdata_get(
            @RequestParam(name = "shopId", defaultValue = "1") Long shopId,
            @RequestParam(name = "cpage", defaultValue = "1") int cpage,
            @RequestParam(name = "pageBlock", defaultValue = "8") int pageBlock,
            Model model) throws IOException {
        ShopVO findShop = shopService.findShop(shopId);
        List<ReviewVO> listReviewVos = reviewService.selectListByShopId(shopId, cpage, pageBlock);

        long totalPageCount = getTotalPageCount(pageBlock, reviewService.getTotalRowsByShopId(shopId));

        model.addAttribute("shop", findShop);
        model.addAttribute("listReviewVos", listReviewVos);
        model.addAttribute("totalPageCount", totalPageCount);

        return "thymeleaf/review/reviewListByShopId";
    }


    // 리뷰 마이 페이지
    @GetMapping("/review_Mypage")
    public String review_MypageList(@RequestParam(name = "cpage", defaultValue = "1") int cpage,
                                    @RequestParam(name = "pageBlock", defaultValue = "8") int pageBlock,
                                    Model model, HttpSession session)  {
        String memberId = (String) session.getAttribute("member_id");
        int userId = memberService.findNumByMemberId(memberId);
        List<ReviewVO> myReviewList = reviewService.selectListByUserId(userId, cpage, pageBlock);

        long totalPageCount = getTotalPageCount(pageBlock, reviewService.getTotalRowsByUserId(userId));

        model.addAttribute("myReviewList", myReviewList);
        model.addAttribute("totalPageCount", totalPageCount);

        return "thymeleaf/review/reviewMypageList";
    }


    // selectOne
    @GetMapping("/reviewMypageSelectOne")
    public String reviewMypageSelectOne(@RequestParam(name = "reviewId") int reviewId, Model model, HttpSession session) {
        String memberId = (String) session.getAttribute("member_id");
        if (memberId != null) {
            return "redirect:/main";
        }
        ReviewVO vo = reviewService.findOneByReviewId(reviewId);
        model.addAttribute("vo", vo);
        return "thymeleaf/review/reviewMypageOne";
    }

    // 수정 페이지 조회
    @GetMapping("/reviewMypageUpdateOne")
    public String reviewMypageUpdateOne(@RequestParam(name = "reviewId") int reviewId, Model model, HttpSession session) {
        String memberId = (String) session.getAttribute("member_id");
        if (memberId != null) {
            return "redirect:/main";
        }
        ReviewVO vo = reviewService.findOneByReviewId(reviewId);
        model.addAttribute("vo", vo);
        return "thymeleaf/review/reviewUpdateFormPage";
    }


    @PostMapping("/review_update")
    public String reviewUpdate(@ModelAttribute RequestReview request) throws Exception {
        // 현재 로그인 id와 리뷰 아이디 비교 하고싶으면 이부분 수정
        boolean checkDoBadWord = wordAnalysisService.checkdoBadWord(request.getReviewComment());
        if (!checkDoBadWord) {
            request.setReviewComment("해당 리뷰는 관리자에 의해 보이지 않습니다");
        }
        reviewService.updateReview(request);

        return "redirect:/reviewMypageSelectOne?reviewId=" + request.getReviewId();
    }




    @GetMapping("/reviewMypagedeleteOne")
    public String reviewMypagedeleteOne(
            @RequestParam(name = "reviewId") int reviewId,
            @RequestParam(name = "memberId") String memberId,
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "cpage", defaultValue = "1") int cpage,
            @RequestParam(name = "pageBlock", defaultValue = "3") int pageBlock,
            Model model, HttpSession session) {
              // reviewId 로 삭제
        reviewService.deleteByReviewId(reviewId);

        List<ReviewVO> myReviewList = reviewService.selectListByUserId(userId, cpage, pageBlock);
        long totalPageCount = getTotalPageCount(pageBlock, reviewService.getTotalRowsByUserId(userId));

        model.addAttribute("myReviewList", myReviewList);
        model.addAttribute("totalPageCount", totalPageCount);

        return "redirect:thymeleaf/review/reviewMypageList";
    }

    private long getTotalPageCount(int pageBlock, long total_rows) {
        long totalPageCount = 1;
        if (total_rows / pageBlock == 0) {
            totalPageCount = 1;
        } else if (total_rows % pageBlock == 0) {
            totalPageCount = total_rows / pageBlock;
        } else {
            totalPageCount = total_rows / pageBlock + 1;
        }
        return totalPageCount;
    }
}






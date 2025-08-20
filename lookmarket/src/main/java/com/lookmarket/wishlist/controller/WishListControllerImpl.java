package com.lookmarket.wishlist.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.wishlist.service.WishListService;
import com.lookmarket.wishlist.vo.WishListVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/wishlist")
public class WishListControllerImpl implements WishListController  {

    @Autowired
    private WishListService wishListService;

    @RequestMapping(value="/wishList.do", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView wishList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String mId = (String) session.getAttribute("loginUserId");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("wishlist/wishList"); // JSP 경로
        List<WishListVO> wishList = wishListService.getWishListByMember(mId);
        mav.addObject("wishList", wishList);

        return mav;
    }

    @RequestMapping(value="/delete.do", method={RequestMethod.GET, RequestMethod.POST})
    public String deleteWishList(@RequestParam("wId") int wId) throws Exception {
        wishListService.removeWishList(wId);
        return "redirect:/wishlist/wishList.do";
    }
    
    @RequestMapping(value="/toggle.do", method=RequestMethod.POST)
    @ResponseBody
    public String toggleWishList(@RequestParam("gId") int gId,
                                 HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String m_id = (String) session.getAttribute("loginUserId");
        System.out.println("찜 요청 도착: m_id=" + m_id + ", gId=" + gId);

        // 로그인 안 되어 있으면 로그인 페이지로 보냄
        if (m_id == null) {
        	return "login_required";
        }

        // 이미 찜 되어있으면 삭제, 아니면 추가
        boolean isWished = wishListService.isWished(m_id, gId);
        if (isWished) {
            wishListService.removeWish(m_id, gId);
            return "removed";
        } else {
            wishListService.addWish(m_id, gId);
            return "added";
        }
    }
}

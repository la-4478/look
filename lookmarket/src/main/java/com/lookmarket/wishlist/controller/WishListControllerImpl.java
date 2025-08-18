package com.lookmarket.wishlist.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value="/wishList.do", method=RequestMethod.GET)
    public ModelAndView wishList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String current_id = (String) session.getAttribute("current_id");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("wishlist/wishList"); // JSP 경로
        List<WishListVO> wishList = wishListService.getWishListByMember(current_id);
        mav.addObject("wishList", wishList);

        return mav;
    }

    @RequestMapping(value="/delete.do", method=RequestMethod.POST)
    public String deleteWishList(@RequestParam("wId") int wId) throws Exception {
        wishListService.removeWishList(wId);
        return "redirect:/wishlist/list.do";
    }
}

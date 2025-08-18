package com.lookmarket.wishlist.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface WishListController {
	
	public ModelAndView wishList(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public String deleteWishList(@RequestParam("wId") int wId) throws Exception;

}

package com.lookmarket.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.cart.service.CartService;
import com.lookmarket.cart.vo.CartVO;
import com.lookmarket.common.base.BaseController;
import com.lookmarket.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("cartController")
@RequestMapping(value="/cart")
public class CartControllerImpl extends BaseController implements CartController{
	@Autowired
	private CartService cartService;
	@Autowired
	private CartVO cartVO;
	@Autowired
	private MemberVO memberVO;
	
	@Override
	@RequestMapping(value="/myCartList.do", method=RequestMethod.GET)
	public ModelAndView myCartList(HttpServletRequest request, HttpServletResponse response)  throws Exception {
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		HttpSession session = request.getSession();
		String current_id = (String)session.getAttribute("current_id");
		List<CartVO> cartList = cartService.myCartList(current_id);
		session.setAttribute("cartList", cartList);
		
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/updateCartQty.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateCartQty(@RequestParam("c_id") int c_id, @RequestParam("c_qty") int c_qty, HttpServletRequest request, HttpServletResponse response)  throws Exception {
	    try {
	        cartService.updateQty(c_id, c_qty); // cart 테이블 수량 변경
	        return ResponseEntity.ok("success");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
	    }
	}
	
	@RequestMapping(value="/deleteCartItem.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteCartItem(@RequestParam("c_id") int c_id) {
	    try {
	        cartService.deleteCartItem(c_id);
	        return ResponseEntity.ok("success");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
	    }
	}
}

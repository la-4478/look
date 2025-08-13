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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("cartController")
@RequestMapping(value="/cart")
public class CartControllerImpl extends BaseController implements CartController{
	@Autowired
	private CartService cartService;
    
	@Override
	@RequestMapping(value="/myCartList.do", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView myCartList(HttpServletRequest request, HttpServletResponse response)  throws Exception {
		HttpSession session = request.getSession();
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("common/layout");
		mav.addObject("viewName", viewName);
		
		
		String current_id = (String)session.getAttribute("current_id");
		
		List<CartVO> cartList = cartService.myCartList(current_id);
		mav.addObject("cartList", cartList);
		session.setAttribute("cartList", cartList);
		
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/updateCartQty.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateCartQty(@RequestParam("c_id") int c_id, @RequestParam("c_qty") int c_qty) {
	    try {
	        cartService.updateQty(c_id, c_qty); // cart 테이블 수량 변경
	        return ResponseEntity.ok("success");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
	    }
	}
	
	@Override
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
	
	@Override
	@RequestMapping(value="/addCartItem.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addCartItem(CartVO cartVO, HttpServletRequest request) {
	    try {
	        String current_id = (String) request.getSession().getAttribute("current_id");
	        cartVO.setM_id(current_id); // 세션에서 회원 ID 설정
	        cartVO.setC_qty(1);
	        cartService.addCartItem(cartVO);
	        return ResponseEntity.ok("success");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
	    }
	}
	
	@Override
	@RequestMapping(value="/placeOrder.do", method=RequestMethod.POST)
	@ResponseBody
	//장바구니 상품 주문하기
	public ResponseEntity<String> placeOrder(HttpServletRequest request) {
	    try {
	        String current_id = (String) request.getSession().getAttribute("current_id");
	        cartService.placeOrder(current_id);
	        return ResponseEntity.ok("success");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
	    }
	}


}

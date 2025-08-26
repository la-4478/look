package com.lookmarket.mypage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.common.base.BaseController;
import com.lookmarket.community.vo.ReviewVO;
import com.lookmarket.mypage.service.MyPageService;
import com.lookmarket.mypage.vo.MyPageVO;
import com.lookmarket.order.service.DeliveryService;
import com.lookmarket.order.vo.DeliveryVO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.wishlist.service.WishListService;
import com.lookmarket.wishlist.vo.WishListVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("myPageController")
@RequestMapping(value="/mypage")
public class MyPageControllerImpl extends BaseController implements MyPageController{
	@Autowired
	private MyPageService myPageService;
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private WishListService wishListService; 
	
	//사용자	
	@Override
	@RequestMapping(value="/mypageInfo.do", method=RequestMethod.GET)
	public ModelAndView myPageInfo(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		//마이페이지 첫화면(사용자)
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		String current_id = (String)session.getAttribute("current_id");
		
		MyPageVO myPageVO = myPageService.getMyPageInfo(current_id);
		System.out.println("회원 정보 : " + myPageVO);
		if (myPageVO == null) {
	        mav.addObject("message", "회원 정보를 찾을 수 없습니다.");
	        return mav;
	    }
		
		String m_email = myPageVO.getM_email();
		
		if (m_email != null && m_email.contains("@")) {
		    String[] parts = m_email.split("@", 2);
		    String m_email_id = parts[0];
		    String m_email_domain = parts[1];
		    myPageVO.setM_email_id(m_email_id);
		    myPageVO.setM_email_domain(m_email_domain);
		} else {
		    // 이메일 형식이 올바르지 않을 경우 처리 (필요하면)
		    myPageVO.setM_email_id("");
		    myPageVO.setM_email_domain("");
		}
		
		mav.addObject("myPageInfo", myPageVO);
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		return mav;
	}
	
	//주문내역(사용자)
	@Override
	@RequestMapping(value="/listMyOrderHistory.do", method=RequestMethod.GET)
	public ModelAndView listMyOrderHistory(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		String current_id = (String)session.getAttribute("current_id");
		System.out.println("current_id = " + current_id);
	    if(current_id != null) {
	        // 주문 리스트 가져오기
	        List<OrderVO> orderList = myPageService.getOrdersByMemberId(current_id);
	        mav.addObject("orderList", orderList);
	    } else {
	        mav.addObject("message", "로그인이 필요합니다.");
	    }
	    
		return mav;
	}
	
	//주문상세내역(사용자)
	@Override
	@RequestMapping(value="/myOrderDetail.do", method=RequestMethod.GET)
	public ModelAndView myOrderDetail(@RequestParam("oId") int oId, HttpServletRequest request, HttpServletResponse response)  throws Exception{
		
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		// 주문 상세정보 가져오기
		OrderVO order = myPageService.getOrderById(oId);             // 주문 기본정보 조회
		List<OrderItemVO> orderItems = myPageService.getOrderItemsByOrderId(oId);  // 주문 상품 리스트 조회

		if(order != null) {
	        mav.addObject("order", order);
	        mav.addObject("orderItems", orderItems);
	    } else {
	        mav.addObject("message", "주문 정보를 찾을 수 없습니다.");
	    }
		
		return mav;
	}
	
//	@Override
//	@RequestMapping(value="/.do", method=RequestMethod.GET)
//	public ModelAndView myPageInfo(HttpServletRequest request, HttpServletResponse response)  throws Exception{
//		//배송조회(일단 보류)
//		
//	}
	
	@Override
	@RequestMapping(value="/myWishList.do", method=RequestMethod.GET)
	public ModelAndView myWishList(HttpServletRequest request, HttpServletResponse response)  throws Exception{
        //찜(사용자)
        HttpSession session = request.getSession();
        ModelAndView mav = new ModelAndView();
        String layout = "common/layout";
        mav.setViewName(layout);
        String viewName = (String)request.getAttribute("viewName");
        mav.addObject("viewName", viewName);
        
        session.setAttribute("sideMenu", "reveal");
        session.setAttribute("sideMenu_option", "myPage");
        
        String m_id = (String) session.getAttribute("current_id");
        if(m_id != null) {
            // 찜목록 데이터 가져오기
            List<WishListVO> wishList = myPageService.getMyWishList(m_id);
            for (WishListVO item : wishList) {
            	System.out.println("상품 ID: " + item.getwId());
                System.out.println("상품 ID: " + item.getgId());
                System.out.println("회원 ID: " + item.getmId());
                System.out.println("상품명: " + item.getgName());
                System.out.println("가격: " + item.getgPrice());
                System.out.println("이미지: " + item.getgImage());
                System.out.println("----------------------------");
            }
            mav.addObject("wishList", wishList);
        }
	        return mav;
	}
	@Override
	@RequestMapping(value="/myCommunity.do", method=RequestMethod.GET)
	public ModelAndView myCommunity(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		//커뮤니티(사용자)
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		
		String m_id = (String)session.getAttribute("current_id");
		List<ReviewVO> communityList = myPageService.selectMyCommunityList(m_id);
		
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", "mypage/myCommunity");
		mav.addObject("communityList", communityList);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/updateMyInfo.do", method=RequestMethod.POST)
	public ModelAndView updateMyInfo(@ModelAttribute MyPageVO myPageVO, HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		String m_email_id = myPageVO.getM_email_id();
		String m_email_domain =myPageVO.getM_email_domain();
		myPageVO.setM_email(m_email_id + "@" + m_email_domain);

	    int result = myPageService.updateMyInfo(myPageVO);

	    if(result == 1) {
	    	redirectAttributes.addFlashAttribute("message", "정보가 수정되었습니다.");
	    	mav.setViewName("redirect:/mypage/mypageInfo.do");
	    }else {
	    	redirectAttributes.addFlashAttribute("message", "정보가 수정에 오류가 발생하였습니다. 다시시도해주세요.");
	    	mav.setViewName("redirect:/mypage/mypageInfo.do");
	    }
	    return mav;
	}
	
	@RequestMapping(value="/deleteMyInfo.do", method=RequestMethod.POST)
	public String deleteMember(@RequestParam("m_id") String m_id, HttpSession session, RedirectAttributes rttr) {
	    try {
	    	myPageService.deleteMember(m_id);
	    	
	    	session.invalidate();
	        rttr.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.\n일주일 후 계정 정보가 완전히 삭제됩니다.");
	        return "redirect:/main/sijangbajoMain.do";  // 메인 페이지로 이동
	    } catch (Exception e) {
	    	e.printStackTrace(); // 어떤 예외인지 로그에 자세히 출력
	        rttr.addFlashAttribute("message", "탈퇴 처리 중 오류가 발생했습니다.");
	        return "redirect:/mypage/mypageInfo.do";
	    }
	}
	
	@Override
	@RequestMapping(value="/listMyDelivery.do", method=RequestMethod.GET)
    public ModelAndView listMyDelivery(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		
		String layout = "common/layout";
		mav.setViewName(layout);
		
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		String memberId = (String) session.getAttribute("loginUserId"); // 로그인된 회원 ID
		System.out.println("memberId : " + memberId);
        List<DeliveryVO> listMyDelivery = deliveryService.getDeliveryList(memberId);
        System.out.println("listMy : " + listMyDelivery);
        
        mav.addObject("listMyDelivery", listMyDelivery);
        return mav;
    }
	@Override
	@RequestMapping(value="/cencelDelivery.do", method= {RequestMethod.GET,RequestMethod.POST})
	public String cencelMyDelivery(@RequestParam("o_id") int o_id, HttpServletRequest request ) throws Exception{
		deliveryService.cencelDelivery(o_id);

		return "redirect:/mypage/listMyDelivery.do";
	}
	
	@PostMapping("/issueCoupon.do")
	@ResponseBody
	public Map<String, Object> issueCoupon(@RequestBody Map<String, Integer> body, HttpSession session) throws Exception {
	    String memberId = (String) session.getAttribute("current_id");
	    System.out.println("memberId for coupon issue: " + memberId);
	    Map<String, Object> response = new HashMap<>();
	    
	    int promoId = body.get("promoId");
	    int result = myPageService.issuePromotionCoupon(promoId, memberId);
	    
	    if(result == 1) {
	        response.put("success", true);
	        response.put("message", "쿠폰이 발급되었습니다.");
	    } else {
	        response.put("success", false);
	        response.put("message", "쿠폰 발급 중 오류가 발생했습니다.");
	    }
	    
	    return response;
	}
}

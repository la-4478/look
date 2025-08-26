package com.lookmarket.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.community.Service.CommunityService;
import com.lookmarket.community.vo.BlackBoardVO;
import com.lookmarket.community.vo.ReviewVO;
import com.lookmarket.goods.service.GoodsService;
import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.inquiry.service.InquiryService;
import com.lookmarket.inquiry.vo.CommentVO;
import com.lookmarket.member.service.MemberService;
import com.lookmarket.member.vo.MemberApprovalDTO;
import com.lookmarket.member.vo.MemberVO;
import com.lookmarket.mypage.service.MyPageService;
import com.lookmarket.mypage.vo.MyPageVO;
import com.lookmarket.order.service.DeliveryService;
import com.lookmarket.order.service.OrderService;
import com.lookmarket.order.vo.DeliveryVO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("adminController")
@RequestMapping("/admin")
public class AdminControllerImpl implements AdminController{
	@Autowired
	private MemberService memberService;
	String layout ="common/layout";
	@Autowired
	private CommunityService communityService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private MyPageService myPageService;
	@Autowired
	private InquiryService inquiryService;
	
	//viewName 수정 필요
	@Override
	@RequestMapping(value="/mypage/mypageAdminInfo.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mypageAdminInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//내정보
		HttpSession session = request.getSession();;
		String m_id = (String)session.getAttribute("loginUserId");
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		MyPageVO myPageVO = myPageService.getMyPageInfo(m_id);
		
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
		
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		mav.addObject("member", myPageVO);
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/allGoodsList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView allGoodsList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//상품정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		List<GoodsVO> goodsList = goodsService.getAllGoods();
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		session.setAttribute("goodsList", goodsList);
		return mav;
	}
	
	@Override
	@RequestMapping(value="/allOrderList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView allOrderList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//주문정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		List<OrderVO> orderList = orderService.allOrderList();
		List<OrderItemVO> orderItem = orderService.allItemList();
		List<DeliveryVO> delivery = deliveryService.allDeliList();
		System.out.println("orderList : " + orderList);
		System.out.println("orderItem : " + orderItem);
		System.out.println("delivery : " + delivery);
		for(OrderVO vo : orderList) {
			for(OrderItemVO vo1 : orderItem) {
				System.out.println("o_name : " + vo1.getOtGoodsName());
				System.out.println("금액 : " + vo1.getOtGoodsPrice());
				System.out.println("개수 :" + vo1.getOtGoodsQty());
			}
			System.out.println("o_id : " + vo.getOId());
			System.out.println("oiName : " +vo.getOiName());
			System.out.println("주소 : " + vo.getOiDeliveryAddress());
			
		}
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		session.setAttribute("orderList", orderList);
		session.setAttribute("orderItem", orderItem);
		session.setAttribute("delivery", delivery);
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/allMemberList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView allMemberList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//회원정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		List<MemberVO> memberList = memberService.getMemberList();
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		session.setAttribute("memberList", memberList);
		
		return mav;
	}
	
	
	@Override
	@RequestMapping(value="/community/allBlackBoardList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView allBlackBoardList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//사장님고충방 리스트
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		List<BlackBoardVO> boardList = communityService.allboardList();
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		session.setAttribute("boardList", boardList);
		
		return mav;
	}	
	
	@Override
	@RequestMapping(value="/accountList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView accountList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//회계리스트
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/accountDetail.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView accountDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//회계상세정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		return mav;
	}

	@Override
	@RequestMapping(value="/ApprovalList.do", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView approvalForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("ApprovalList.do 컨트롤러 진입");
		HttpSession session = request.getSession();
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(layout);
		mav.addObject("viewName", viewName);
		

		
		List<MemberVO> m_role = memberService.findbusinessMember(2);

		List<MemberApprovalDTO> approvalData = new ArrayList<>();
		for (MemberVO member : m_role) {
		    MemberApprovalDTO dto = new MemberApprovalDTO();
		    dto.setMember(member);
		    dto.setPendingList(memberService.findbusinessMember2(member.getM_id()));
		    approvalData.add(dto);
		}
		session.setAttribute("sideMenu", "reavel");
		session.setAttribute("sideMenu_option", "myPage_admin");
		mav.addObject("approvalList", approvalData);

		
		return mav;
	}	
	
	@Override
	@RequestMapping(value="/approveBusiness.do", method={RequestMethod.GET, RequestMethod.POST})
	public String approveBusiness(@RequestParam("m_id") String m_id, RedirectAttributes ra) throws Exception {
	    memberService.approve(m_id);
	    ra.addFlashAttribute("msg", "사업자 승인 완료: " + m_id);
	    return "redirect:/admin/ApprovalList.do";
	}
	
	@Override
	@RequestMapping(value="/rejectBusiness.do", method={RequestMethod.GET, RequestMethod.POST})
	public String rejectBusiness(@RequestParam("m_id") String m_id, RedirectAttributes ra)throws Exception{
		memberService.reject(m_id);
	    ra.addFlashAttribute("msg", "사업자 승인 거부: " + m_id);
	    return "redirect:/admin/ApprovalList.do";
	}
	
	@Override
	@RequestMapping(value="/reversBusiness.do", method={RequestMethod.GET, RequestMethod.POST})
	public String reversBusiness(@RequestParam("m_id") String m_id, RedirectAttributes ra)throws Exception{
		memberService.revers(m_id);
		ra.addFlashAttribute("msg", "사업자 승인 되돌리기 성공: " + m_id);
		return "redirect:/admin/ApprovalList.do";
	}

	@Override
	@RequestMapping(value="/community/allCommunityList.do", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView allCommunityList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//커뮤니티 정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		List<ReviewVO> reviewList = communityService.communityList();
		mav.addObject("communityList", reviewList);
		
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		return mav;
	}
	
	@RequestMapping(value="/community/blackBoardDetail.do", method=RequestMethod.GET)
	public ModelAndView blackBoardDetail(@RequestParam("b_id") String b_id, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HttpSession session = request.getSession();
	    ModelAndView mav = new ModelAndView();
	    
	    String layout = "common/layout";
	    mav.setViewName(layout);
	    
	    // 현재 로그인 사용자 ID 가져오기
	    String current_id = (String) session.getAttribute("current_id");

	    // 세션에 저장 (JSP에서 EL로 ${currentUserId} 접근할 수 있도록)
	    session.setAttribute("currentUserId", current_id);
	    
	    BlackBoardVO blackBoard = communityService.blackBoardDetail(b_id);
	    if(blackBoard != null) {
	    	communityService.upBlackHit(b_id);
	    }
	    
	    List<CommentVO> commentList = inquiryService.getcomment(b_id);
	    
	    mav.addObject("commentList", commentList);
	    mav.addObject("blackBoard", blackBoard);
	    
	    mav.addObject("viewName", "admin/community/blackBoardDetail");
	    
	    session.setAttribute("sideMenu", "reveal");
	    session.setAttribute("sideMenu_option", "community_admin");
	    
	    return mav;
	}
	
}

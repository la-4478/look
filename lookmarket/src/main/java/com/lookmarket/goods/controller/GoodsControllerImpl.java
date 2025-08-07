package com.lookmarket.goods.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.goods.service.GoodsService;
import com.lookmarket.goods.vo.GoodsVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller("goodsController")
@RequestMapping(value="/jangbogo")
public class GoodsControllerImpl implements GoodsController{
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private GoodsVO goodsVO;
	
	@Override
	@RequestMapping(value="/goodsList.do", method=RequestMethod.GET)
	public ModelAndView goodsList(@RequestParam("category") String category, HttpServletRequest request, HttpServletResponse response) throws Exception{
		//장보고 상품화면(사용자)
		List<GoodsVO> goodsList = new ArrayList<>();
		if(category.equals("all")) {
			goodsList = goodsService.getAllGoods();
		} else if(category.equals("fresh")) {
			goodsList = goodsService.getFreshGoods();
		} else if(category.equals("processed")) {
				goodsList = goodsService.getProcessed();
			} else if(category.equals("living")) {
				goodsList = goodsService.getLiving();
			} else if(category.equals("fashion")) {
				goodsList = goodsService.getFashion();
			} else if(category.equals("local")) {
				goodsList = goodsService.getLocal();			
		}
		
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		mav.addObject("goodsList", goodsList);
		return mav;
	}
	
	@Override
	@RequestMapping(value="/goodsDetail.do", method=RequestMethod.GET)
	public ModelAndView goodsDetail(@RequestParam("g_id") int g_id, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    GoodsVO goods = goodsService.getGoodsDetail(g_id);
	    //이미지 리스트
	    List<String> detailImageList = new ArrayList<>();
		if (goods.getI_file_name() != null && !goods.getI_file_name().isEmpty()) {
			String[] files = goods.getI_file_name().split(",");
			for (int i = 1; i < files.length; i++) {
			    detailImageList.add(files[i].trim()); // 첫 번째는 대표 이미지니까 제외
			}
	    }

	    ModelAndView mav = new ModelAndView();
	    String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);

	    mav.addObject("goods", goods);
	    mav.addObject("detailImageList", detailImageList);
	    return mav;
	}
	
	@Override
	@RequestMapping(value="/goodsAddForm.do", method=RequestMethod.GET)
	public ModelAndView goodsAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
	    return mav;
	}
	
	@Override
	@RequestMapping(value="/goodsUpdateForm.do", method=RequestMethod.GET)
	public ModelAndView goodsUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    int g_id = 1;
	    GoodsVO goods = goodsService.getGoodsDetail(g_id);
	    ModelAndView mav = new ModelAndView("jangbogo/goodsUpdateForm");
	    mav.addObject("goods", goods);
	    return mav;
	}

}

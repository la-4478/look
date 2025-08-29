package com.lookmarket.order.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lookmarket.account.vo.AccTxnVO;
import com.lookmarket.cart.dao.CartDAO;
import com.lookmarket.cart.vo.CartVO;
import com.lookmarket.order.dao.DeliveryDAO;
import com.lookmarket.order.dao.OrderDAO;
import com.lookmarket.order.portone.PortOneService;
import com.lookmarket.order.vo.AccountingVO;
import com.lookmarket.order.vo.DeliveryVO;
import com.lookmarket.order.vo.OrderDTO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.order.vo.PayVO;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
    private CartDAO cartDAO;
    @Autowired
    private PortOneService portOneService;
    @Autowired private DeliveryDAO deliveryDAO;
	
	public List<OrderVO> listMyOrderGoods(OrderVO orderVO) throws Exception{
		List<OrderVO> orderGoodsList;
		orderGoodsList=orderDAO.listMyOrderGoods(orderVO);
		return orderGoodsList;
	}
	
	public void addNewOrder(List<OrderVO> myOrderList) throws Exception{
		orderDAO.insertNewOrder(myOrderList);
	}	
    @Override
    public void addNewpay(PayVO payVO) throws Exception{
        orderDAO.insertPay(payVO);
        
    }
    
    public OrderVO findMyOrder(String order_id) throws Exception{
        return orderDAO.findMyOrder(order_id);
    }
    
    @Override
    public void removeCartItem(String m_id, int G_id) throws Exception {
        CartVO cartVO = new CartVO();
        cartVO.setM_id(m_id);
        cartVO.setG_id(G_id);

        Integer cart_id = orderDAO.removeCartItem(cartVO);
        if (cart_id != null) {
            orderDAO.deleteCartGoods(cart_id);
        } else {
            System.out.println("삭제할 장바구니 항목이 없습니다.");
        }
    }

	@Override
	public void addOrderItem(OrderItemVO itemVO) throws Exception {
		orderDAO.addOrderItem(itemVO);
		
	}
	
	@Override
    @Transactional
    public void processOrder(String m_id) throws Exception {
        // 1. 회원 장바구니 상품 조회
        List<CartVO> cartList = cartDAO.selectCartByMemberId(m_id);
        
        if (cartList == null || cartList.isEmpty()) {
            throw new Exception("장바구니가 비어있습니다.");
        }
        
        // 2. 주문(헤더+아이템) 저장
        orderDAO.insertOrder(m_id, cartList);
        
        // 3. 장바구니 비우기
        cartDAO.clearCart(m_id);
    }
	
	@Override
	public List<OrderItemVO> getCartItemsByMemberId(String m_id) throws Exception {
	    return orderDAO.getCartItemsByMemberId(m_id);
	}

	@Override
	public String reviewgoodsname(int o_id) throws Exception {
		return orderDAO.reviewgoodsname(o_id);
	}

	@Override
	public Integer whomid(String m_id) throws Exception {
		Integer oId = orderDAO.whomid(m_id);
	if (oId == null) {
		throw new EmptyResultDataAccessException("주문없음: " + m_id, 1);
    }
	return oId;
	}
	 public void processOrderPayment(String paymentKey, int expectAmount, OrderVO order, PayVO pay) throws Exception {
	        // PortOne 결제 검증
	        boolean verified = portOneService.verifyPayment(paymentKey, expectAmount);

	        if (verified) {
	            // 회계 내역 자동 등록
	            AccountingVO acc = new AccountingVO();
	            acc.setOrder_num(order.getOId());
	            acc.setMember_id(order.getMId());
	            acc.setPay_method(pay.getPMethod());
	            acc.setPay_amount(expectAmount);
	            acc.setPay_status("SUCCESS");

	            orderDAO.insertAccounting(acc);
	        } else {
	            throw new RuntimeException("결제 검증 실패: 회계 등록 불가");
	        }
	    }
	 @Override
	 @Transactional
	 public void recordTransactionAfterPayment(OrderVO order, PayVO pay) throws Exception {
		 AccTxnVO txn = new AccTxnVO();
	     txn.setTxnDate(LocalDate.now());
	     txn.setAccountId(3L); // 포트원 정산 계좌 ID (미리 조회해도 됨)
	     txn.setCategoryId(1L); // 상품매출 카테고리 ID
	     txn.setAmount(BigDecimal.valueOf(order.getOiTotalGoodsPrice()));
	     txn.setMemo("결제번호: " + order.getOId());
	     txn.setPartnerName("사업자");
	     txn.setOrderId(order.getOId());
	     txn.setPaymentId(pay.getPTransactionId()); // 있으면

	     orderDAO.insertTxn(txn);
	 }

	@Override
	public List<OrderDTO> getPagedOrderList(int offset, int limit) {
        List<OrderVO> orders = orderDAO.selectOrderPage(offset, limit);
        List<OrderDTO> result = new ArrayList<>();

        for (OrderVO order : orders) {
            List<OrderItemVO> items = orderDAO.selectOrderItemsByOrderId(order.getOId());
            DeliveryVO delivery = deliveryDAO.selectDeliveryByOrderId(order.getOId());

            OrderDTO dto = new OrderDTO();
            dto.setOrder(order);
            dto.setOrderItems(items);
            dto.setDelivery(delivery);

            result.add(dto);
        }

        return result;
    }

	@Override
	public int countAllOrders() {
        return orderDAO.selectOrderCount();
    }

	@Override
	public List<OrderDTO> joinedOrderData() {
		return orderDAO.joinedOrderData();
	}
}

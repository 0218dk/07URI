package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseDao;




@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao{
	
	///Field
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	
	public PurchaseDaoImpl(){
		System.out.println(this.getClass());
	}
	public Purchase getPurchase(int tranNo)throws Exception{
		
		return sqlSession.selectOne("PurchaseMapper.getPurchase", tranNo);
		
	}
	
	public Purchase updatePurchase(Purchase purchase)throws Exception{
		purchase.setDivyDate(purchase.getDivyDate().replace("-",""));		
		System.out.println("딩에오");
		sqlSession.update("PurchaseMapper.updatePurchase", purchase);
		return purchase;
	}
	
	public Purchase addPurchase(Purchase purchase)throws Exception{
		
		purchase.setDivyDate(purchase.getDivyDate().replace("-",""));
		sqlSession.insert("PurchaseMapper.addPurchase", purchase);
		
		return purchase;
	}
	
	public Map<String, Object> getPurchaseList(Search search,String buyerId ) throws Exception{
		
		Map<String , Object>  map = new HashMap<String, Object>();
		
			map.put("search", search);
			map.put("buyerId", buyerId);
		
			
			List<Purchase> list = sqlSession.selectList("PurchaseMapper.getPurchaseList", map); 
			
			System.out.println("디에이오의 리스트다 ===="+list);
		
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setBuyer((User)sqlSession.
				selectOne("UserMapper.getUser", list.get(i).getBuyer().getUserId()));
				
				System.out.println(i+"포문 위");
				
				list.get(i).setPurchaseProd((Product)sqlSession.
				selectOne("ProductMapper.getProduct", list.get(i).getPurchaseProd().getProdNo()));
				
				System.out.println(i+"포문 아래");
			}
			
			map.put("totalCount", sqlSession.selectOne("PurchaseMapper.getTotalCount", buyerId));
	
			map.put("list", list);

		return map;
	}
	
	public int getTotalCount(Search search) throws Exception {
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", search);
	}
}// end of class

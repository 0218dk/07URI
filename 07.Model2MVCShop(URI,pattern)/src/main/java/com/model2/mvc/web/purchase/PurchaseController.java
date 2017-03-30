package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;


//==> ȸ������ Controller
@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	//setter Method ���� ����
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
	//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	//@RequestMapping("/addUserView.do")
	//public String addUserView() throws Exception {
	@RequestMapping( value="addPurchase", method=RequestMethod.GET )
	public String addPurchase(@RequestParam("prod_no") int prodNo,
								Model model) throws Exception{
	
		System.out.println("/purchase/addPurchase : GET");
		
		// Model �� View ����
		Product product = productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping( value="addPurchase", method=RequestMethod.POST )
	public String addPurchase( @ModelAttribute("purchase") Purchase purchase,
								@RequestParam("prodNo") int prodNo,
								@RequestParam("buyerId")String buyerId) throws Exception {

//		private User buyer;
//		private Product purchaseProd;
		System.out.println("/purchase/addPurchase : POST");
		
		Product product = productService.getProduct(prodNo);
		User user = userService.getUser(buyerId);
		//Business Logic
		purchase.setPurchaseProd(product);
		purchase.setBuyer(user);
		
		System.out.println("add�� ��ü�� : "+purchase);
		purchaseService.addPurchase(purchase);
		return "forward:/purchase/addPurchaseView.jsp";
	}
	@RequestMapping( value="listPurchase", method=RequestMethod.GET )
	public String listPurchase( @ModelAttribute("search") Search search,
								@RequestParam("userId")String buyerId,
								 Model model) throws Exception {

		System.out.println(search);
		System.out.println(buyerId);
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String , Object> map=purchaseService.getPurchaseList(search, buyerId);
		
		Page resultPage = new Page( search.getCurrentPage(),
				((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println("����Ʈ������ : "+resultPage);
		// Model �� View ����
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping( value="getPurchase", method=RequestMethod.GET )
	public String getPurchase(@RequestParam("tranNo") int tranNo,
								Model model) throws Exception{
	
		
		System.out.println("/purchase/getPurchase : GET");
		
		// Model �� View ����
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	@RequestMapping( value="updatePurchase", method=RequestMethod.GET )
	public String updatePurchase(@RequestParam("tranNo") int tranNo,
								Model model) throws Exception{
	
		System.out.println("/purchase/updatePurchase : GET"+tranNo);
		
		// Model �� View ����
		Purchase purchase = purchaseService.getPurchase(tranNo);
		//purchase.setDivyDate(purchase.getDivyDate().replace("-",""));
		model.addAttribute("purchase", purchase);
		
		System.out.println("��Ʈ����Ʈ�� ��ü��"+purchase);
		
		return "forward:/purchase/updatePurchase.jsp";
	}
	
	@RequestMapping( value="updatePurchase", method=RequestMethod.POST )
	public String updatePurchase( @ModelAttribute("purchase") Purchase purchase/*,
								  @RequestParam("prodNo") int prodNo,
								  @RequestParam("buyerId")String buyerId*/) throws Exception {
	
		System.out.println("/purchase/updatePurchase : POST");
		
		//Business Logic
		purchaseService.updatePurcahse(purchase);
		// Model �� View ����
		System.out.println("POST�� ��ü�� : "+purchase);
		
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	

	
}
package com.rashed.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.google.zxing.WriterException;
import com.rashed.model.SalesProduct;
import com.rashed.dao.SalesProductDAO;
import com.rashed.model.RequisitionProduct;
import com.rashed.dao.RequisitionProductDAO;
import com.rashed.model.RequisitionMulti;
import com.rashed.dao.RequisitionMultiDAO;
import com.rashed.model.Product;
import com.rashed.dao.ProductDAO;
import com.rashed.model.CustomerInfo;
import com.rashed.dao.CustomerInfoDAO;
import com.rashed.model.Sales;
import com.rashed.dao.SalesDAO;
import com.rashed.model.SalesmanInfo;
import com.rashed.dao.SalesmanInfoDAO;
import com.rashed.model.SalesMain;
import com.rashed.dao.SalesMainDAO;
import com.rashed.model.CustomerPurchaseProduct;
import com.rashed.dao.CustomerPurchaseProductDAO;
import com.rashed.util.*;

public class CustomerPurchaseProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String LIST_CUSTOMERPURCHASEPRODUCT = "/jsp/order/customerPurchaseProductList.jsp";
	private static String ENQUIRY = "/jsp/order/customerPurchaseProductEnquiry.jsp";
	
	//private RequisitionProductDAO rpdao;
	//private ProductDAO pdao;
	//private CustomerInfoDAO cdao;
	//private SalesDAO sdao;
	//private SalesmanInfoDAO smdao;
	//private RequisitionMultiDAO rmdao;
	//private SalesProductDAO spdao;
	//private SalesMainDAO smmdao;
	private CustomerPurchaseProductDAO cppdao;
	
	String action = "";
	String message = "";
	
       
    public CustomerPurchaseProductController() {
        super();
        //rpdao = new RequisitionProductDAO();
        //pdao = new ProductDAO();
        //cdao = new CustomerInfoDAO();
        //sdao = new SalesDAO();
        //smdao = new SalesmanInfoDAO();
        //rmdao = new RequisitionMultiDAO();
        //spdao = new SalesProductDAO();
        //smmdao = new SalesMainDAO();
        cppdao = new CustomerPurchaseProductDAO();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
		action = request.getParameter("action");
		// for set the action in session [S]
		HttpSession session = request.getSession();
		session.setAttribute("action", action);
		// for set the action in session [E]
		
		if(action.equalsIgnoreCase("delete")){
			String purchase_product_id = request.getParameter("purchase_product_id");
			String strOrderStatus = cppdao.getSelectedOtherID(purchase_product_id).getOrder_status();
			if (!strOrderStatus.equals("A")) {
				cppdao.delete(purchase_product_id);
				message = "Purchase " + purchase_product_id + " deleted Successfully!!!";
				request.setAttribute("success", message);
			} else {
				message = "Approved Purchase can not be deleted !!!";
				request.setAttribute("success", message);
			}
			forward=LIST_CUSTOMERPURCHASEPRODUCT;
			request.setAttribute("customerPurchaseProducts", cppdao.getAllCustomerPurchaseProduct());
			/*message = "Data Deleted Successfully!!!";
			request.setAttribute("success", message);*/
		} else if(action.equalsIgnoreCase("cancel")){
			String purchase_product_id = request.getParameter("purchase_product_id");
			String strOrderStatus = cppdao.getSelectedOtherID(purchase_product_id).getOrder_status();
			String strProduct = cppdao.getSelectedOtherID(purchase_product_id).getProduct_id();
			String strRequisition = cppdao.getSelectedOtherID(purchase_product_id).getRequisition_id();
			int intOrderQuantity = cppdao.getSelectedOtherID(purchase_product_id).getOrder_quantity();
			
			if (!strOrderStatus.equals("S")) {
				cppdao.cancel(purchase_product_id);
				
				message = "Purchase " + purchase_product_id + " Cancelled Successfully!!!";
				request.setAttribute("success", message);
			} else {
				message = "Success Purchase can not be cancelled !!!";
				request.setAttribute("success", message);
			}
			
			forward=LIST_CUSTOMERPURCHASEPRODUCT;
			request.setAttribute("customerPurchaseProducts", cppdao.getAllCustomerPurchaseProduct());
		} else if(action.equalsIgnoreCase("customerPurchaseProductList")){
			forward = LIST_CUSTOMERPURCHASEPRODUCT;
			request.setAttribute("customerPurchaseProducts", cppdao.getAllCustomerPurchaseProduct());
		} else if(action.equalsIgnoreCase("enquiry")){
			forward = ENQUIRY;
			String purchase_product_id = request.getParameter("purchase_product_id");
			CustomerPurchaseProduct cpp = cppdao.getCustomerPurchaseProductById(purchase_product_id);
			request.setAttribute("customerPurchaseProduct", cpp);
		} else if(action.equalsIgnoreCase("enquiryEnquiry")){
			forward = ENQUIRY;
			String purchase_product_id = request.getParameter("purchase_product_id");
			CustomerPurchaseProduct cpp = cppdao.getCustomerPurchaseProductById(purchase_product_id);
			request.setAttribute("customerPurchaseProduct", cpp);
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(forward);
		rd.forward(request, response);
	}

}

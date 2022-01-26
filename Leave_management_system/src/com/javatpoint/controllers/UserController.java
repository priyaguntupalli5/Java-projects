package com.javatpoint.controllers;     
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;    
import org.springframework.stereotype.Controller;  
import org.springframework.ui.Model;      
import org.springframework.web.bind.annotation.RequestMapping;
import com.javatpoint.beans.User;    
import com.javatpoint.dao.UserDao; 
import com.javatpoint.beans.Leave;

@Controller
public class UserController {
 	private int id;
 	List<User> leavelist;
 	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	 @Autowired    
	    UserDao dao;
	 	

@RequestMapping("/login")
	public String display(HttpServletRequest req,Model m)
	{
		int sap_id=Integer.parseInt(req.getParameter("sap_id"));
		String password=req.getParameter("password");
		String userType=req.getParameter("usertype");
		if(userType.equals("employee"))
		{
			List<User> list=dao.getEmployees();
			int i;
			for(i=0;i<list.size();i++)
			{
				int emp_sap=list.get(i).getSap_id();
				String first_name=list.get(i).getFirst_name();
				String last_name=list.get(i).getLast_name();
				String emp_pwd=list.get(i).getPassword();
				if(emp_sap==sap_id && password.equals(emp_pwd))
				{
					String msg="Hello "+first_name+" "+last_name;
					setId(sap_id);
					m.addAttribute("message",msg);
					return "employee";
				}
			}
			if(i==list.size()) {
				String msg="Sorry !!! Please enter valid details";
				m.addAttribute("message",msg);
				return "error";
			}
		}
			else
			{
				List<User> list=dao.getManagers();
				int i,mng_sap;
				String mng_pwd,first_name,last_name;
				for(i=0;i<list.size();i++)
				{
					mng_sap=list.get(i).getSap_id();
					mng_pwd=list.get(i).getPassword();
					first_name=list.get(i).getFirst_name();
					last_name=list.get(i).getLast_name();
					if(mng_sap==sap_id && password.equals(mng_pwd))
					{
						setId(sap_id);
						String msg="Hello "+first_name+" "+last_name;
						m.addAttribute("message",msg);
						return "manager";
					}
				}
				if(i==list.size()) {
					String msg="Sorry !!! Please enter valid details";
					m.addAttribute("message",msg);
					return "error";
				}
			}
		m.addAttribute("message","I'm not valid user");
		return "error";
		}

@RequestMapping("/register")
public String register(HttpServletRequest req,Model m)
{
	String password=req.getParameter("password");
	String re_password=req.getParameter("re-password");
	if(password.equals(re_password))
	{
		User user=new User();
		user.setSap_id(Integer.parseInt(req.getParameter("sap_id")));
		user.setFirst_name(req.getParameter("first_name"));
		user.setLast_name(req.getParameter("last_name"));
		user.setPassword(password);
		String userType=req.getParameter("usertype");
		if(userType.equals("employee"))
			dao.registerEmployee(user);
		else
			dao.registerManager(user);
		m.addAttribute("message","You are registered successfully!! Please login");
		return "error";
	}
	m.addAttribute("message","Password mismatch!! Please verify and register again");
	return "error";
	}

@RequestMapping("/leave_request")
	public String leave(HttpServletRequest req,Model m)
	{
		if(req.getParameter("leave")!=null)
		{
			int leave=Integer.parseInt(req.getParameter("leave"));
			List<Leave> leavedetails=dao.getLeaveDetails(leave);
			m.addAttribute("leavedetails",leavedetails);
		}
		if(req.getParameter("approve")!= null)
		{
			int leave_id=Integer.parseInt(req.getParameter("status"));
			dao.getApproved(leave_id);
			leavelist=dao.getLeaveEmployees(id);
			if(leavelist.size()>=1) 
				m.addAttribute("list",leavelist);
				
			else {
				m.addAttribute("message","You have no leave requests");
				return "manager";
			}
		}
		else if(req.getParameter("reject")!=null)
		{
			int leave_id=Integer.parseInt(req.getParameter("status"));
			leavelist=dao.getLeaveEmployees(id);
			dao.getRejected(leave_id);
			if(leavelist.size()>=1) 
				m.addAttribute("list",leavelist);
				
			else {
				m.addAttribute("message","You have no leave requests");
				return "manager";
			}
		}
		leavelist=dao.getLeaveEmployees(id);
		if(leavelist.size()>=1) 
			m.addAttribute("list",leavelist);
			
		else {
			m.addAttribute("message","You have no leave requests");
			return "manager";
		}
		return "leave_request";
	}

@RequestMapping("/add_employee")
	public String add_emp(HttpServletRequest req,Model m)
	{
		if(req.getParameter("employee")!=null)
		{
			int emp=Integer.parseInt(req.getParameter("employee"));
			dao.addEmployee(getId(),emp);
			List<User> list=dao.getEmployeesWithNoManagers();
			m.addAttribute("list",list);
		}
		List<User> list=dao.getEmployeesWithNoManagers();
		m.addAttribute("list",list);
		List<User> emp_mng=dao.getMngEmp(getId());
		if(list.size()<1)
		{
			m.addAttribute("message","There is no employee without manager");
			return "manager";
		}
		else
		{
			m.addAttribute("employees",emp_mng);
	 		return "add_employee";
		}
	}

@RequestMapping("/apply_leave")
	public String apply(HttpServletRequest req,Model m)
	{	
	   return "apply_leave";
	}

@RequestMapping("/apply")
public String applyLeave(HttpServletRequest req,Model m) {
	Leave l=new Leave();
	l.setId(id);
	l.setLeave_type(req.getParameter("leave_type"));
	l.setStart_date(req.getParameter("start_date"));
	l.setEnd_date(req.getParameter("end_date"));
	l.setReason(req.getParameter("reason"));
	dao.applyLeave(id);
	dao.leaveDetails(l);
	String msg="Leave submitted successfully";
	m.addAttribute("message",msg);
	return "employee";
}

@RequestMapping("/notification")
public String notification(HttpServletRequest req,Model m)
{
	String status=dao.getNotifications(getId());
	if(status.equals("approved"))
		m.addAttribute("status","Your leave is approved");
	else if(status.equals("rejected"))
		m.addAttribute("status","Your leave is rejected");
	else if(status.equals("pending"))
		m.addAttribute("status","Your leave is still pending");
	else
	{
		m.addAttribute("message","You have no notifications");
		return "employee";
	}
	return "notification";
}

@RequestMapping("/logout")
public String logout(HttpServletRequest req,Model m)
{
	m.addAttribute("message","Bye bye!!");
	return "error";
}


}

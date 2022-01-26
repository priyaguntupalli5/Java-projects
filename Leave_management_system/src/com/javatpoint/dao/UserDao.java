package com.javatpoint.dao;    
import java.sql.PreparedStatement;
import java.sql.ResultSet;    
import java.sql.SQLException;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;    
import com.javatpoint.beans.User;  
import com.javatpoint.beans.Leave;

public class UserDao {
JdbcTemplate template;
	public void setTemplate(JdbcTemplate template)
	{
		this.template=template;
	}
	//get list of employees
	public List<User> getEmployees()
	{
		return template.query("select * from emp_login",new RowMapper<User>() {
			public User mapRow(ResultSet rs,int row) throws SQLException {
				User e=new User();
				e.setSap_id(rs.getInt(1));
				e.setFirst_name(rs.getString(2));
				e.setLast_name(rs.getString(3));
				e.setPassword(rs.getString(4));
				return e;
			}
		});
	}
	
	//get list of employees with no managers
	public List<User> getEmployeesWithNoManagers()
	{
		return template.query("select t1.* from emp_login as t1 left join emp_mng as t2 on t1.sap_id=t2.emp_sap where t2.emp_sap is null",new RowMapper<User>() {
			public User mapRow(ResultSet rs,int row) throws SQLException {
				User e=new User();
				e.setSap_id(rs.getInt(1));
				e.setFirst_name(rs.getString(2));
				e.setLast_name(rs.getString(3));
				e.setPassword(rs.getString(4));
				return e;
			}
		});
	}
	
	//get list of managers
	public List<User> getManagers()
	{
		return template.query("select * from manager_login",new RowMapper<User>() {
			public User mapRow(ResultSet rs,int row) throws SQLException {
				User m = new User();
				m.setSap_id(rs.getInt(1));
				m.setFirst_name(rs.getString(2));
				m.setLast_name(rs.getString(3));
				m.setPassword(rs.getString(4));
				return m;
			}
		});
	}
	
	//register employee in emp_login
	public Boolean registerEmployee(final User user)
	{
		String sql="insert into emp_login values(?,?,?,?)";
		return template.execute(sql,new PreparedStatementCallback<Boolean>() {
			public Boolean doInPreparedStatement(PreparedStatement ps)  
		            throws SQLException, DataAccessException {  
		        ps.setInt(1,user.getSap_id());
		        ps.setString(2,user.getFirst_name()); 
		        ps.setString(3,user.getLast_name());  
		        ps.setString(4,user.getPassword());
		        return ps.execute();
			}
		});
	}
	
	//register manager in manager_login
	public Boolean registerManager(final User user)
	{
		String sql="insert into manager_login values(?,?,?,?)";
		return template.execute(sql,new PreparedStatementCallback<Boolean>() {
			public Boolean doInPreparedStatement(PreparedStatement ps)  
		            throws SQLException, DataAccessException {  
		        ps.setInt(1,user.getSap_id());
		        ps.setString(2,user.getFirst_name()); 
		        ps.setString(3,user.getLast_name());  
		        ps.setString(4,user.getPassword());
		        return ps.execute();
			}
		});
	}
	
	//get employees of particular manager
	public List<User> getMngEmp(int id)
	{
		String query="select sap_id,first_name,last_name from emp_login,emp_mng where emp_sap=sap_id and mng_sap="+id;
		return template.query(query,new RowMapper<User>() {
			public User mapRow(ResultSet rs,int row) throws SQLException {
				User em = new User();
				em.setSap_id(rs.getInt(1));
				em.setFirst_name(rs.getString(2));
				em.setLast_name(rs.getString(3));
				return em;
			}
		});
	}
	
	//update employee leave status as pending
	public int applyLeave(int id)
	{
		
		String sql1 = "update emp_mng set leave_status='pending' where emp_sap="+id;
		return template.update(sql1);
	}
	
	//insert leave details into leave table
	public Boolean leaveDetails(final Leave l)
	{
		String query="insert into leaves values(?,?,?,?,?)";  
	    return template.execute(query,new PreparedStatementCallback<Boolean>(){  
	    public Boolean doInPreparedStatement(PreparedStatement ps)  
	            throws SQLException, DataAccessException {  
	        ps.setInt(1,l.getId());  
	        ps.setString(2,l.getLeave_type());  
	        ps.setString(3,l.getStart_date());  
	        ps.setString(4,l.getEnd_date());
	        ps.setString(5,l.getReason());
	        return ps.execute();  
	              
	    }  
	    });  		
	}
	
	//get employees who applied for leave
	public List<User> getLeaveEmployees(int id)
	{
		String query="select sap_id,first_name,last_name from emp_login,emp_mng where leave_status='pending' and emp_sap=sap_id and mng_sap="+id;
		return template.query(query,new RowMapper<User>() {
			public User mapRow(ResultSet rs,int row) throws SQLException {
				User le = new User();
				le.setSap_id(rs.getInt(1));
				le.setFirst_name(rs.getString(2));
				le.setLast_name(rs.getString(3));
				return le;
			}
		});
	}
	
	//get leave details of employees who applied for leave
	public List<Leave> getLeaveDetails(int leave)
	{
		String sql="select sap_id,leave_type,start_date,end_date,reason from leaves,emp_mng where emp_sap=sap_id and sap_id="+leave;
		return template.query(sql,new RowMapper<Leave>() {
			public Leave mapRow(ResultSet rs,int row) throws SQLException {
				Leave l = new Leave();
				l.setId(rs.getInt(1));
				l.setLeave_type(rs.getString(2));
				l.setStart_date(rs.getString(3));
				l.setEnd_date(rs.getString(4));
				l.setReason(rs.getString(5));
				return l;
			}
		});
			
	}
	
	//update the leave status as approved
	public int getApproved(int leave) {
		String sql="update emp_mng set leave_status='approved' where emp_sap="+leave;
		return template.update(sql);
	}
	
	//update the leave status as rejected
		public int getRejected(int leave) {
			String sql="update emp_mng set leave_status='rejected' where emp_sap="+leave;
			return template.update(sql);
		}
	
	//add employee to a manager
		public Boolean addEmployee(final int id, final int emp) {
			String sql="insert into emp_mng values(?,?,?)";
			return template.execute(sql,new PreparedStatementCallback<Boolean>(){  
			    public Boolean doInPreparedStatement(PreparedStatement ps)  
			            throws SQLException, DataAccessException {  
			        ps.setInt(1,emp);  
			        ps.setInt(2,id);  
			        ps.setString(3,"none");  
			        return ps.execute(); 
			    }  
			    });  	
		}
		
	//get notifications of an employee
		public String getNotifications(int id) {
			String sql="select leave_status from emp_mng where emp_sap=?";    
			String status = (String) (template.queryForObject(sql, new Object[] {id}, String.class));
			return status;
		}

}


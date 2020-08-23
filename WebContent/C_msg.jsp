
<%@page import="jsp.*,java.sql.*,java.util.*,java.text.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<% 
	String EventName = null;
	String rollno = null;
	String position = null;
	String agenda = null;
	String points = null;
	int page_bit=0;

// 0 default
// 1 Apply for Candidature
// 2 Applications details
// 3 Delete Applications
%>
 
<%try{
    
    M_CandidatureApplication CA = new M_CandidatureApplication();
    String source = request.getPathTranslated();
    System.out.println(source);
    page_bit=1;
    if(session.getAttribute("fname").equals("apply")){
        EventName = (String)session.getAttribute("EventName");
        rollno = (String)(session.getAttribute("user"));
        position = request.getParameter("position");
        agenda = request.getParameter("agenda");
        points = request.getParameter("points");
        int batch_by_rollno = CA.getBatch(rollno);
        int batch_by_position = CA.getBatch(EventName, position);
        System.out.println("batch_by_rollno:"+batch_by_rollno);
        System.out.println("batch_by_position:"+batch_by_position);
        String message;
        if(batch_by_rollno == batch_by_position){
	        boolean isapplied = CA.createAP(EventName,position,rollno,agenda, points); 
	        if(isapplied){
	        	message="Successfully_applied_for_application";
	        	System.out.println("C-msg.jsp: "+message);
	        	response.sendRedirect("Success_MSG.jsp?success="+message);
	       }
	        else{	
	        	message="Invalid Entry";
	        	System.out.println("C-msg.jsp: error in storing in database");
	        	response.sendRedirect("error_pg_msg.jsp?error="+message);
	        }
        }
        else{	
        	message="Invalid Entry";
        	System.out.println("C-msg.jsp: "+message);
        	response.sendRedirect("error_pg_msg.jsp?error="+message);
        }
    }
    
    else if(session.getAttribute("fname").equals("application_detail")){
    	page_bit=2;
    	String id = request.getParameter("ID");
    	String val = (String)session.getAttribute("details");
    	
		String arr[] = val.split(":");
		rollno = arr[0]; 
		EventName = arr[1];
		position = arr[2];
		//System.out.println("id at C_msg.jsp:"+id);
		//System.out.println("rollno in C_MSG at approve:"+rollno);
		//System.out.println("eventname in C_MSG at approve:"+EventName);
		//System.out.println("position in C_MSG at approve:"+position);
        if(id.equals("0")){
        	boolean isapproved =CA.approve(rollno, EventName, position);
        	if(isapproved){
        		System.out.println("C_msg.jsp: application approved");
            	String message="Application_details";
                response.sendRedirect("Success_CEO.jsp?success="+message);
            }
            else{
            	System.out.println("C_msg.jsp: error in approving application");
            	String message="Invalid Entry";
        		response.sendRedirect("error_page.jsp?error="+message);
            }
        }
        else if(id.equals("1")){
        	boolean isrejected =CA.reject(rollno, EventName);
        	if(isrejected){
        		System.out.println("C_msg.jsp: application rejected");
            	String message="Application_details";
                response.sendRedirect("Success_CEO.jsp?success="+message);
            }
            else{
            	System.out.println("C_msg.jsp: error in rejecting application");
            	String message="Invalid Entry";
        		response.sendRedirect("error_page.jsp?error="+message);
            }
        }
    }
    
    else if(session.getAttribute("fname").equals("delete_application")){
    	
    	page_bit=3;
        System.out.println(session.getAttribute("fname"));
        String val = request.getParameter("details");
		String arr[] = val.split(":");
		rollno = arr[0]; 
		EventName = arr[1];
        boolean isdeleted = CA.deleteAP(rollno,EventName);
 		
        if (isdeleted) {
        	String message="Successfully deleted";
            response.sendRedirect("Success_MSG.jsp?success="+message);
        }
        else{
        	
        	String message="Invalid Entry";
    		response.sendRedirect("error_pg_msg.jsp?error="+message);
        }
        
    }
    
}catch(Exception e){
	
	if(EventName == "" || rollno==null ||position==null)
	{
		String message="Invalid input";
		if(page_bit== 1)
		{
			response.sendRedirect("apply_for_cand.jsp?error=" + message);
		}
		else if(page_bit== 2)
		{
			response.sendRedirect("application_detail.jsp?error=" + message);
		}
		else if(page_bit == 3)
		{
			response.sendRedirect("delete_ap.jsp?error=" + message);
		}
		else
		{
			response.sendRedirect("msg.jsp?error=" + message);
		}
	}
    
}
 
%> 
</body>
</html>
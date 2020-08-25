package jsp;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class M_CandidatureApplication {
	
	//method which return list of events along with positions in which a applicant or candidate has applied
	public ArrayList<ArrayList<String> > geteventbyrollno(String rollno, int choice){
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		
		ArrayList<ArrayList<String> > eventpositionlist = new ArrayList<ArrayList<String> >();
		try {
			c = MySQL.connect();
			st = c.createStatement();
			String query = "select eventname, appliedforpost from applicants where isapproved = " + choice + " and rollno = '" + rollno +"';";
			System.out.println("m_CA.java"+ query);
			rs = st.executeQuery(query);
			while(rs.next()) {
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(rs.getString(1));
				temp.add(rs.getString(2));
				eventpositionlist.add(temp);
			}
			System.out.println("Successfully sent eventpositionlist in M_CA.java");
			rs.close();
			st.close();
			return eventpositionlist;
		}
		catch(Exception e) {
			System.out.println("error at M_CA.java in geteventbyrollno function");
			e.printStackTrace();
			return eventpositionlist;
		}
		finally {
			MySQL.close(c);
		}
	}
	
	public ArrayList<String> getuserdetails(String rollno){
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<String> detaillist = new ArrayList<String>();
		try {
			c = MySQL.connect();
			st = c.createStatement();
			String query = "Select name, cgpa, batch, gender, email, mobile from students where verified=1 and rollno='"+rollno+"';";
			rs = st.executeQuery(query);
			while(rs.next()) {
				detaillist.add(rs.getString(1));
				detaillist.add(rs.getString(2));
				detaillist.add(rs.getString(3));
				detaillist.add(rs.getString(4));
				detaillist.add(rs.getString(5));
				detaillist.add(rs.getString(6));
			}
			return detaillist;
		}
		catch(Exception e) {
			e.printStackTrace();
			return detaillist;
		}
		finally {
			MySQL.close(c);
		}
	}
	
	//method to return batch of a student
	public int getBatch(String rollno) {
		rollno = rollno.toUpperCase();
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		int batch = 0;

		try {

			c = MySQL.connect();
			st = c.createStatement();
			String query = "select batch from students where rollno ='"
					+ rollno + "';";
			System.out.println(query+" M_CA.java");
			rs = st.executeQuery(query);

			while (rs.next()) {
				batch = rs.getInt(1);
			}
			rs.close();
			st.close();
			return batch;
		} catch (Exception e) {
			e.printStackTrace();
			return batch;
		} finally {
			MySQL.close(c);
		}
	}
	
	
	//method to return batch of which a student can apply in that event and that position
	public int getBatch(String eventname, String position) {
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		int batch = 0;
		M_ElectionEvent me = new M_ElectionEvent();
		int eid = me.getEEId(eventname);
		try {

			c = MySQL.connect();
			st = c.createStatement();
			String query = "select allowedcandidate from positions where eid ="
					+ eid + " and position ='" + position + "';";
			System.out.println(query+" M_CA.java");
			rs = st.executeQuery(query);
			while (rs.next()) {
				String temp = rs.getString(1);
				System.out.println("temp:---------------"+temp);
				if(temp.equals("UG_First_Year"))
					batch=1;
				else if(temp.equals("UG_Second_Year"))
					batch=2;
				else if(temp.equals("UG_Third_Year"))
					batch=3;
				else if(temp.equals("UG_Fourth_Year"))
					batch=4;
				else if(temp.equals("PG"))
					batch=5;
					
			}
			rs.close();
			st.close();
			return batch;
		} catch (Exception e) {
			e.printStackTrace();
			return batch;
		} finally {
			MySQL.close(c);
		}
	}
	
	
	//method to get cgpa of student
	public double getcgpa(String rollno) {
		rollno = rollno.toUpperCase();
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		double cgpa = 0.0;

		try {

			c = MySQL.connect();
			st = c.createStatement();
			String query = "select cgpa from students where rollno ='" + rollno
					+ "';";
			System.out.println(query+" M_CA.java");
			rs = st.executeQuery(query);

			while (rs.next()) {
				cgpa = rs.getDouble(1);
			}

			rs.close();
			st.close();
			return cgpa;

		} catch (Exception e) {

			e.printStackTrace();
			return cgpa;

		} finally {
			MySQL.close(c);
		}

	}
	
	//method to create a applicant
	public boolean createAP(String EventName, String position, String rollno, String agenda, String points) {
		Connection c = null;
		Statement st = null;
		rollno=rollno.toUpperCase();
		try {
			c = MySQL.connect();
			st = c.createStatement();
			String query = "insert into applicants values ('" + EventName + "','" + position + "',0,'" + rollno + "','"+ agenda + "','"+ points + "');";
			System.out.println(query+" M_CA.java");
			st.executeUpdate(query);

			st.close();
			return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		} finally {

			MySQL.close(c);
		}

	}

	//method which return applicants or candidates according to events 
	public static Map < String,ArrayList<String> > getApplications(int choice){
	    Connection c = null;
			Statement st = null;
			ResultSet rs = null;
			String rollno;
			String eventname;
			Map < String,ArrayList<String> > applications=new HashMap < String,ArrayList<String> >();
			try {
				c = MySQL.connect();
				st = c.createStatement();
				String query = "select eventname,rollno from applicants where isapproved = "+choice;
				System.out.println(query);
				rs = st.executeQuery(query);
				while(rs.next()){
					eventname=rs.getString(1);
					rollno= rs.getString(2);
					if(applications.containsKey(eventname)){
						applications.get(eventname).add(rollno);
					}
					else{
						applications.put(eventname,new ArrayList<String>());
						applications.get(eventname).add(rollno);
					}	
				}
				rs.close();
				st.close();
				return applications;
			} catch (Exception e) {
			e.printStackTrace();
			return applications;
			} finally {
			MySQL.close(c);
			}   
	    }

	//method which return applicant details according to eventname
	public ArrayList<String> getAD(String rollno, String eventName) {
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		ArrayList<String> r = new ArrayList<String>();
		// r.add("test");
		try {
			c = MySQL.connect();
			st = c.createStatement();
			String query = "select * from applicants where isapproved=0 and rollno = '" + rollno
					+ "' and eventname ='" + eventName +"';" ;
			System.out.println(query+" M_CA.java");
			rs = st.executeQuery(query);
			rsmd = rs.getMetaData();
			int ss = rsmd.getColumnCount();

			int i = 0;
			System.out.println(ss);
			while (rs.next()) {
				while (i < ss) {
					i++;
					String t = rs.getString(i);
					if(t==null)
						t="add some value";
					System.out.println(t);
					if(i==3||i==4)
						continue;
					if (t.equals("P"))
						t = "President";
					else if (t.equals("VP"))
						t = "Vice President";
					else if (t.equals("GSS"))
						t = "General Secretary Sports";
					else if (t.equals("GSSC"))
						t = "General Secretary Science and Technology";
					else if (t.equals("GSC"))
						t = "General Secretary Cultural";
					r.add(t);
				}
			}
			rs.close();
			st.close();
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			return r;
		} finally {
			MySQL.close(c);
		}
	}
	
	//method to approve an applicant to become a candidate
	public boolean approve(String rollno, String eventName, String position) {
		Connection c = null;
		Statement st = null;
		try {
			c = MySQL.connect();
			st = c.createStatement();
			rollno = rollno.toUpperCase();
			String query = "update applicants set isapproved = 1 where rollno ='"
					+ rollno + "' and eventname ='" + eventName +"';" ;
			System.out.println(query+" M_CA.java");
			st.executeUpdate(query);
			query = "insert into candidates (rollno, eventname, position, votecount) values ('"
					+ rollno +"', '" + eventName +"', '" + position + "',0);";
			System.out.println(query+" M_CA.java");
			st.executeUpdate(query);
			st.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		} finally {
			MySQL.close(c);
		}
	}
	
	//method to reject an applicant
	public boolean reject(String rollno, String eventName) {
		Connection c = null;
		Statement st = null;
		try {
			c = MySQL.connect();
			st = c.createStatement();
			rollno = rollno.toUpperCase();
			String query = "delete from applicants where rollno ='"
					+ rollno + "' and eventname ='" + eventName +"';" ;
			System.out.println(query+" M_CA.java");
			st.executeUpdate(query);
			st.close();
			return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;

		} finally {

			MySQL.close(c);
		}
	}
	
	
	//method to delete an applicant
	public boolean deleteAP(String rollno, String eventName) {
		System.out.println("delete function called by:"+rollno+"    "+eventName);
		Connection c = null;
		try {
			c = MySQL.connect();
			Statement st = c.createStatement();
			String query1 = "delete from applicants where isapproved=0 and rollno = '" + rollno
					+ "' and eventname ='" + eventName +"';" ;
			System.out.println(query1);
			st.executeUpdate(query1);
			st.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			MySQL.close(c);
		}
	}
	
}
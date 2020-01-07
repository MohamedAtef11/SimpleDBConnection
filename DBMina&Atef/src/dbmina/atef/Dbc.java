/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbmina.atef;


import com.mysql.cj.x.protobuf.MysqlxPrepare;
import java.sql.* ;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hp
 */
public class Dbc {
    Vector<Employee> v =new Vector<>();
    int idx=0;
    Connection conn = null;
    Statement st = null;

    Dbc() throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception ex) {
            Logger.getLogger(Dbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/javaDatabase","root","password");
        } catch (Exception ex) {
            System.out.println("DDDDDD");
            Logger.getLogger(Dbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            st =conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Dbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        getData();
    }
    
    void getData()
    {
        v.clear();
        String queryString = new String ("select * from employee");
        ResultSet rs = null;
        try {
            rs = st.executeQuery(queryString);
            while(rs.next()){
                Employee e = new Employee(rs.getInt(1), rs.getInt(6), rs.getString(2), rs.getString(3), rs.getString(5));
                v.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dbc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Employee first ()
    {
        idx=0;
        return (v.get(0));
    }
    
    public Employee last()
    {
        idx=v.size()-1;
        return (v.get(v.size()-1));
    }
    public Employee next ()
    {
        if(idx<v.size()-1)
            idx ++;
        
        return v.get(idx);
    }
    public Employee prev()
    {
        if(idx>0)
            idx -- ;
        return v.get(idx);
    }
    public void update (int id , int salary , String fname , String lname , String gender)
    {
        try {
            PreparedStatement pst = conn.prepareStatement("update Employee set salary=?,first_name=? , last_name=?, sex=? where emp_id=? ");
            pst.setInt(1, salary);
            pst.setString(2, fname);
            pst.setString(3, lname);
            pst.setString(4, gender);
            pst.setInt(5, id);
            int rs = pst.executeUpdate();
            getData();
        } catch (SQLException ex) {
            Logger.getLogger(Dbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void delete (int id)
    {
        try {
            PreparedStatement pst = conn.prepareStatement("delete from Employee where emp_id=?");
            pst.setInt(1, id);
            int rs = pst.executeUpdate();
            
                idx--;
            getData();
        } catch (SQLException ex) {
            Logger.getLogger(Dbc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void insert(int id , int salary , String fname , String lname , String gender)
    {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO employee (emp_id,first_name, last_name,salary, sex)\n" +
                    "VALUES (?,?,?,?,?);");
            pst.setInt(4, salary);
            pst.setString(2, fname);
            pst.setString(3, lname);
            pst.setString(5, gender);
            pst.setInt(1, id);
            int rs = pst.executeUpdate();
            getData();
        } catch (SQLException ex) {
            Logger.getLogger(Dbc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getLastId()
    {
        return v.get(v.size()-1).getId()+1;
    }
    
}

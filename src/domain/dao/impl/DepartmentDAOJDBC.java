package domain.dao.impl;

import db.DB;
import db.DBException;
import domain.dao.DepartmentDAO;
import domain.entites.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentDAOJDBC implements DepartmentDAO {

    private Connection connection;

    public DepartmentDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {

        PreparedStatement st = null;

        try{
            st = connection.prepareStatement(
                    "INSERT INTO department " +
                        "(Name) " +
                         "VALUES (?)"
                    , Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1, department.getName());

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    department.setId(id);
                    System.out.println("Inserted department with id " + id);
                }else{
                    throw new DBException("Insert failed");
                }
                DB.closeResultSet(rs);
            }



        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }

    }

    @Override
    public void update(Department department) {
        PreparedStatement st = null;
        try{
            st = connection.prepareStatement(
                    "UPDATE department " +
                            "SET Name = ? " +
                            "WHERE id = ?"
            );

            st.setString(1, department.getName());
            st.setInt(2, department.getId());
            int rowsAffects = st.executeUpdate();
            if(rowsAffects > 0){
                System.out.println("Updated department with id " + department.getId());
            }
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try{
            st = connection.prepareStatement(
                    "DELETE FROM department WHERE id = ?"
            );
            st.setInt(1, id);
            int rows = st.executeUpdate();
            if(rows > 0){
                System.out.println("Deleted department with id " + id);
            }else{
                throw new DBException("Delete failed");
            }
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        try{
            st = connection.prepareStatement(
                    "SELECT * FROM department " +
                            "WHERE id = ?"
            );
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                Department department = new Department(rs.getInt("id"), rs.getString("Name"));
                return department;
            }else {
                throw new DBException("Department with id " + id + " not found");
            }

        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = connection.prepareStatement(
                    "select * from department ORDER BY Name"
            );

            rs = st.executeQuery();
            List<Department> departments = new ArrayList<Department>();

            while (rs.next()){
                Department dep = new Department(rs.getInt("Id"), rs.getString("Name"));
                departments.add(dep);
            }
            return departments;
        }catch (SQLException e ){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}

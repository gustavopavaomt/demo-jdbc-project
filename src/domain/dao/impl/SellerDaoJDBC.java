package domain.dao.impl;

import db.DB;
import db.DBException;
import domain.dao.SellerDAO;
import domain.entites.Department;
import domain.entites.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDAO {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller department) {
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(
                    "INSERT INTO seller " +
                            "(Name,Email,BirthDate,BaseSalary,DepartmentId) " +
                            "VALUES " +
                            "(?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, department.getName());
            ps.setString(2, department.getEmail());
            ps.setDate(3,new Date(department.getBirthDate().getTime()));
            ps.setDouble(4, department.getBaseSalary());
            ps.setInt(5,department.getDepartment().getId());

            int rowsAffects = ps.executeUpdate();

            if(rowsAffects > 0){
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    department.setId(id);
                }
                else{
                    throw new DBException("Insert failed 0 rows affected");
                }
                DB.closeResultSet(rs);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void update(Seller obj) {

        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "UPDATE seller " +
                            "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                            "WHERE id = ?"
            );
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());
            st.executeUpdate();

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
            st = conn.prepareStatement(
                    "DELETE FROM seller WHERE id = ?"
            );

            st.setInt(1,id);
            st.executeUpdate();
            System.out.println("Deleted seller with id " + id);

        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "+
                            "FROM seller INNER JOIN department "+
                            "on seller.DepartmentID = department.Id "+
                            "WHERE seller.id = ?"
            );

            ps.setInt(1, id);
            rs = ps.executeQuery();

            if(rs.next()){
                Department dep = instanceDepartment(rs);
                Seller seller = instanceSeller(rs,dep);
                return seller;
            }

        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
        return null;
    }

    private Seller instanceSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(dep);
        return seller;
    }

    private Department instanceDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "on seller.DepartmentId = department.id " +
                            "ORDER BY Name"
            );
            rs = ps.executeQuery();
            List<Seller> sellers = new ArrayList<>();
            Map<Integer,Department> departments = new HashMap<>();

            while (rs.next()){
                Department dep = departments.get(rs.getInt("DepartmentId"));
                if(dep == null){
                    dep = instanceDepartment(rs);
                    departments.put(rs.getInt("DepartmentId"),dep);
                }
                Seller seller = instanceSeller(rs,dep);
                sellers.add(seller);
            }
            return sellers;
        }catch (SQLException e){
            throw new DBException(e.getMessage());
        }finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
         PreparedStatement ps = null;
         ResultSet rs = null;
         try{
             ps = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                         "FROM seller INNER JOIN department " +
                         "on seller.DepartmentId = department.Id " +
                         "WHERE DepartmentId = ? " +
                         "ORDER BY Name"

             );
             ps.setInt(1,department.getId());
             rs = ps.executeQuery();
             List<Seller> sellerList = new ArrayList<>();
             Map<Integer, Department> map = new HashMap<>();

             while (rs.next()){
                 Department dep = map.get(rs.getInt("DepartmentId"));
                 if(dep == null){
                     dep = instanceDepartment(rs);
                     map.put(rs.getInt("DepartmentId"), dep);
                 }
                 Seller seller = instanceSeller(rs,dep);
                 sellerList.add(seller);

             }
             return sellerList;

         }catch (SQLException e){
             throw new DBException(e.getMessage());
         }
         finally {
             DB.closeStatement(ps);
             DB.closeResultSet(rs);
         }

    }
}

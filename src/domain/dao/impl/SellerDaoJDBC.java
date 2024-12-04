package domain.dao.impl;

import db.DB;
import db.DBException;
import domain.dao.SellerDAO;
import domain.entites.Department;
import domain.entites.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    }

    @Override
    public void update(Seller department) {

    }

    @Override
    public void deleteById(Integer id) {

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
        return List.of();
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

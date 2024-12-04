package domain.dao;

import domain.entites.Department;
import domain.entites.Seller;

import java.util.List;

public interface SellerDAO {

    void insert(Seller department);
    void update(Seller department);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
    List<Seller> findByDepartment(Department department);
}

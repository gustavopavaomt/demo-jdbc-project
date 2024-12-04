package application;

import domain.dao.SellerDAO;
import domain.dao.impl.DaoFactory;
import domain.entites.Department;
import domain.entites.Seller;

import java.util.List;

public class Main {

    public static void main(String[] args) {


        SellerDAO sellerDAO = DaoFactory.createSellerDAO();

        System.out.println("====== TEST FINDBYIND ======");
        Seller seller = sellerDAO.findById(4);
        System.out.println(seller);

        System.out.println("====== TEST FINDBYDEPARTMENT ======");
        Department dep = new Department(4, null);
        List<Seller> list = sellerDAO.findByDepartment(dep);
        list.forEach(System.out::println);
    }
}

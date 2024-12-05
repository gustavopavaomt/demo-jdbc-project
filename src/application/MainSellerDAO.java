package application;

import domain.dao.SellerDAO;
import domain.dao.impl.DaoFactory;
import domain.entites.Department;
import domain.entites.Seller;

import java.util.List;

public class MainSellerDAO {

    public static void main(String[] args) {


        SellerDAO sellerDAO = DaoFactory.createSellerDAO();

        System.out.println("====== TEST FINDBYID ======");
        Seller seller = sellerDAO.findById(4);
        System.out.println(seller);
        System.out.println();

        System.out.println("====== TEST FINDBYDEPARTMENT ======");
        Department dep = new Department(4, null);
        List<Seller> list = sellerDAO.findByDepartment(dep);
        list.forEach(System.out::println);
        System.out.println();

        System.out.println("====== TEST FINDALL ======");
        list = sellerDAO.findAll();
        list.forEach(System.out::println);

        /*System.out.println("====== TEST INSERT ======");
        Seller newSeller = new Seller("Joel Black","joel@gmail.com",new Date(),4000.0,dep);
        sellerDAO.insert(newSeller);
        System.out.println("Inserted, id = " + newSeller.getId());*/

       /* System.out.println("====== TEST UPDATE ======");
        seller = sellerDAO.findById(1);
        seller.setName("Jorge Jesus");
        sellerDAO.update(seller);
        System.out.println("Updated Sucessfully");*/


        System.out.println("====== TEST DELETE ======");
        sellerDAO.deleteById(8);

    }
}

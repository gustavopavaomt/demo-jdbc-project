package application;

import domain.dao.SellerDAO;
import domain.dao.impl.DaoFactory;
import domain.entites.Seller;

public class Main {

    public static void main(String[] args) {

        SellerDAO sellerDAO = DaoFactory.createSellerDAO();
        Seller seller = sellerDAO.findById(4);

        System.out.println(seller);
    }
}

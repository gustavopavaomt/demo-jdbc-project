package domain.dao.impl;

import db.DB;
import domain.dao.SellerDAO;

public class DaoFactory {


    public static SellerDAO createSellerDAO() {
        return new SellerDaoJDBC(DB.getConnection());
    }
}

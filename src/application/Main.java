package application;

import domain.entites.Department;
import domain.entites.Seller;

import java.util.Date;

public class Main {

    public static void main(String[] args) {

        Department department = new Department(1,"Computer Science");
        Seller seller = new Seller(1,"Bob","bob@gmail.com",new Date(),3000.0,department);


        System.out.println(seller);
    }
}

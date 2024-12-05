package application;

import domain.dao.DepartmentDAO;
import domain.dao.impl.DaoFactory;
import domain.entites.Department;

import java.util.List;

public class MainDepartmentDAO {

    public static void main(String[] args) {


        DepartmentDAO depDAO = DaoFactory.createDepartmentDAO();
        //INSERT
        Department dep = new Department();
        dep.setName("Development");
        depDAO.insert(dep);

        //UPDATE
        Department dep = new Department(6,"Products");
        depDAO.update(dep);

        //DELETE
        depDAO.deleteById(7)

        //FINDBYID
       Department depResult = depDAO.findById(4);
        System.out.println(depResult);

        //FINDALL
        List<Department> departmentList = depDAO.findAll();
        departmentList.forEach(System.out::println);
    }
}

package service;
import dao.CustomerDao;
import domain.Customer;
import domain.pageBean;
import java.util.List;

/**
 * 封装CustomerDao中的方法以便于调用
 */
public class CustomerService {
    CustomerDao customerDao=new CustomerDao();

    public void add(Customer customer){
        customerDao.add(customer);
    }

    public pageBean<Customer> findAll(int pageNum,int pageRecord){
        return customerDao.findAll(pageNum,pageRecord);
    }

    public Customer find(String id){
        return customerDao.find(id);
    }

    public void edit(Customer customer){
        customerDao.edit(customer);
    }

    public void delete(String id){
        customerDao.delete(id);
    }

    public pageBean<Customer> query(Customer customer,int pageNum,int pageRecord){
        return customerDao.query(customer,pageNum,pageRecord);
    }
}

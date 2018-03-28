package Main;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import dao.CustomerDao;
import domain.Customer;
import org.apache.commons.dbutils.QueryRunner;

/**
 * 主类
 */
public class Main {
    QueryRunner qr=new TxQueryRunner();
    public static void main(String[] args){
        CustomerDao customerDao=new CustomerDao();

        //批量向数据库插入数据
        for(int i=0;i<100;i++){
            Customer customer=new Customer();
            customer.setId(CommonUtils.uuid());
            customer.setName("customer"+i);
            customer.setGender(i%2==0 ?"male":"female");
            customer.setPhone("134233"+i);
            customer.setEmail("customer"+i+"@126.com");
            customer.setDescription("Customer"+i);

            customerDao.add(customer);
        }
    }
}

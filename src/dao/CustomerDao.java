package dao;
import cn.itcast.jdbc.TxQueryRunner;
import domain.Customer;
import domain.pageBean;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.spi.CurrencyNameProvider;

/**
 * dao是Data Access Object的缩写（数据访问接口），进行数据库相关操作
 *   TxQueryRunner它是QueryRunner的子类！(引自commons-dbutils.jar)
 *   可用起来与QueryRunner相似的！
 *   支持事务！它底层使用了JdbcUtils来获取连接！
 *
 * 简化jdbc的操作
 * QueryRunner的三个方法：
 * * update() --> insert、update、delete
 * * query() --> select
 * * batch() --> 批处理
 * 详细信息请查看“关于引入第三方包的说明.txt”
 */

public class CustomerDao {
    private QueryRunner qr=new TxQueryRunner(); //没有给对象提供连接池

    //使用sql添加客户信息
    public void add(Customer c) {
        try {
            String sql = "insert into t_customer values(?,?,?,?,?,?)";
            Object[] paramas = {c.getId(), c.getName(), c.getGender(), c.getPhone(),
                    c.getEmail(), c.getDescription()};   //要确保数据顺序与数据库中插入元素顺序一致
            qr.update(sql, paramas);     //更新数据库，执行sql，不提供连接，它内部会使用JdbcUtils来获取连接
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //查询所有
    public pageBean<Customer> findAll(int pageNum,int pageRecord){
        /**
         * 创建pb对象
         * 设置pb的（当前页码）pageNum和（每页记录数）pageRecord
         * 得到（总记录数）totalRecord，设置给pb
         * 得到（当前页面记录数）beanlist，赋值给pb
         * 最后返回pb
         *
         * 实际上没有使用这个方法，而是用下面的query方法
         * query方法包含了findAll方法的这种情况（即传递的参数都为空）
         */
        try{
            pageBean<Customer> pb=new pageBean<Customer>();
            pb.setPageNum(pageNum);
            pb.setPageRecord(pageRecord);

            String sql="select count(*) from t_customer";
            Number number=(Number) qr.query(sql,new ScalarHandler<>()); //ScalarHandler参数为空，返回第一行第一列的数据

            int totalRecord=number.intValue();  //总记录数
            pb.setTotalRecord(totalRecord);

            //sql="select * from t_customer order by name limit ?,?"; //mysql数据库中 ：从第m行开始，向下取第n行的写法
            /**sqlserver 分页写法（使用于sqlserver2012以上）
             * 要取从第m行开始之后n行的记录时
             * 固定传递每页的记录数
             * 可以按某一属性排序（order by）
             */
            StringBuilder firstSql=new StringBuilder("select * from t_customer");
            StringBuilder whereSql=new StringBuilder(" where 1=1 ");
            StringBuilder nextSql=new StringBuilder(" order by id offset ? row fetch next ? row only");
            List<Object> params=new ArrayList<>();
            params.add((pageNum-1)*pageRecord);
            params.add(pageRecord);
            List<Customer> beanList=qr.query(firstSql.append(whereSql).append(nextSql).toString(),new BeanListHandler<Customer>(Customer.class),params.toArray());

            //Object[] params={pageRecord,(pageNum-1)*pageRecord};  //第m行开始后n行数据，即每一页固定显示的数据
            //List<Customer> beanList=qr.query(sql,new BeanListHandler<>(Customer.class),params);//获取结果集的第一行数据，并将其封装到JavaBean对象

            pb.setBeanList(beanList);
            return pb;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //根据id查询加载客户
    public Customer find(String id){
        try{
            String sql="select * from t_customer where id=?";
            return qr.query(sql,new BeanHandler<Customer>(Customer.class),id);//同上面注释
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //修改操作
    public void edit(Customer customer){
        try{
            String sql="update t_customer set name=?,gender=?,phone=?,email=?,description=? where id=?";
            Object[] params={customer.getName(),customer.getGender(),customer.getPhone(),customer.getEmail(),customer.getDescription(),customer.getId()};
            qr.update(sql,params);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //删除操作
    public void delete(String id){
        try{
            String sql="delete from t_customer where id=?";
            qr.update(sql,id);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //多条件组合查询
    public pageBean<Customer> query(Customer customer,int pageNum,int pageRecord){
        try{
            pageBean<Customer> pb=new pageBean<Customer>();
            pb.setPageNum(pageNum);
            pb.setPageRecord(pageRecord);

            StringBuilder cntSql=new StringBuilder("select count(*) from t_customer");  //StringBuilder对象为动态对象，允许扩充它所封装字符串中字符的数量
            StringBuilder whereSql=new StringBuilder(" where 1=1 ");  //条件始终为true，在不定数量查询条件下，可以很好的规范语句
            List<Object> params=new ArrayList<>();  //创建一个ArrayList，用来装载参数值

            //按名字查询
            String name=customer.getName();
            if(name!=null && !name.trim().isEmpty()){
                whereSql.append(" and name like ? ");
                params.add("%"+name+"%");   //模糊查询，含有输入的name的字符串都会被找出
            }

            //按性别查询
            String gender=customer.getGender();
            if(gender!=null && !gender.trim().isEmpty()){
                whereSql.append(" and gender=? ");
                params.add(gender);
            }

            //按电话号码查询
            String phone=customer.getPhone();
            if(phone!=null && !phone.trim().isEmpty()){
                whereSql.append(" and phone like ? ");
                params.add("%"+phone+"%");   //模糊查询，含有输入的phone的字符串都会被找出
            }

            //按邮箱查询
            String email=customer.getEmail();
            if(email!=null && !email.trim().isEmpty()){
                whereSql.append(" and email like ? ");
                params.add("%"+email+"%");   //模糊查询，含有输入的email的字符串都会被找出
            }

            //得到总记录数，赋给totalRecord
            Number num=qr.query(cntSql.append(whereSql).toString(),new ScalarHandler<>(),params.toArray());
            int totalRecord=num.intValue();
            pb.setTotalRecord(totalRecord);

            //取每一页的记录
            StringBuilder firstSql=new StringBuilder("select * from t_customer");
            StringBuilder nextSql=new StringBuilder(" order by id offset ? row fetch next ? row only");
            params.add((pageNum-1)*pageRecord);
            params.add(pageRecord);
            List<Customer> beanList=qr.query(firstSql.append(whereSql).append(nextSql).toString(),new BeanListHandler<Customer>(Customer.class),params.toArray());

            pb.setBeanList(beanList);
            return pb;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}



package sqlservertest;
import java.sql.*;
import cn.itcast.jdbc.TxQueryRunner;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.spi.CurrencyNameProvider;

public class test {
    public static void main(String[] args) {
        String JDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";    //SQL数据库引擎
        String connectDB = "jdbc:sqlserver://localhost:1433;databaseName=t_customer";    //数据源,IP+端口号(即localhost:1433),数据库名为学生-课程
        try {
            Class.forName(JDriver);    //加载数据库引擎，返回给定字符串名的类
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
            System.out.println("加载数据库引擎失败");
            System.exit(0);
        }
        System.out.println("加载数据库驱动成功");
        try {
            String user = "mysql";        //用户名为mysql
            String password = "123456";    //密码为123456
            Connection con = DriverManager.getConnection(connectDB, user, password);    //连接数据库对象
            System.out.println("连接数据库成功");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库连接错误");
            System.exit(0);
        }

        QueryRunner qr=new TxQueryRunner();
        try{
            String sql="select top ? id from t_customer where id not in(select top ? id from t_customer)";
            Object[] params={10,1};
            //qr.query(sql,new BeanHandler<test>(test.class),params);
            //qr.query(sql,new BeanHandler<>);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}


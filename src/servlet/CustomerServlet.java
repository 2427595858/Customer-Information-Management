package servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import domain.Customer;
import domain.pageBean;
import service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.Comment;
import java.io.*;

public class CustomerServlet extends BaseServlet{
    private CustomerService customerService=new CustomerService();

    //添加客户
    public String add(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        Customer customer=CommonUtils.toBean(request.getParameterMap(),Customer.class);

        customer.setId(CommonUtils.uuid()); //使用uuid返回一个随机的32位长的字符串
        customerService.add(customer);
        request.setAttribute("msg","添加客户成功！");

        return "/msg.jsp";
    }

    //显示页面（每页设置为10条记录）
    public String findAll(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        /**
         * 获取页面传递的当前页面pageNum
         * 设置每页记录数pageRecord
         * 使用pageNum和pageRecord调用service方法，得到pageBean，保存到request域
         * 转发到list.jsp
         *
         * 需要调用getPageNum方法来获取当前页面pageNum
         * 若pageNum参数不存在，说明pageNum=1
         * 若存在，则将其转换为int型
         *
         * 注意：其实并没有用这个方法，下面写的模糊查询的query方法已经包含这种情况
         * 即所要查询的参数为空..
         */
        int pageNum=getPageNum(request);
        int pageRecord=10;  //每页设置为10条记录
        pageBean<Customer> pb=customerService.findAll(pageNum,pageRecord);

        pb.setUrl(getUrl(request));     //getUrl方法在下面

        request.setAttribute("pb",pb);
        return "/list.jsp";
    }

    //获取pageNum的方法
    public int getPageNum(HttpServletRequest request){
        String value=request.getParameter("pageNum");

        if(value==null || value.trim().isEmpty()){
            return 1;   //空则当前页面为1
        }
        return Integer.parseInt(value); //否则将当前页面转换为int型
    }

    /**
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     *
     */
    public String preEdit(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        /**
         * 获取客户id，通过id来调用CustomerService类中的add方法
         * 将对象customer保存到request域中，转发到edit.jsp中
         */
        String id=request.getParameter("id");
        Customer customer=customerService.find(id);
        request.setAttribute("customer",customer);
        return "/edit.jsp";
    }

    public String edit(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        /**
         * 封装表单数据到Customer对象中
         * 调用CustomerService中的edit方法完成修改
         * 保存修改成功的信息到request域中
         * 转发到msg.jsp显示成功信息
         */
        Customer customer= CommonUtils.toBean(request.getParameterMap(),Customer.class);
        customerService.edit(customer);
        request.setAttribute("msg","信息修改成功!");
        return "/msg.jsp";
    }

    public String delete(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        /**
         * 使用request中的getParameter方法获取表单传输过来的id
         * 调用CustomerService中的delete方法删除
         * 保存删除成功的信息到request域中
         * 转发到msg.jsp显示成功信息
         */
        String id=request.getParameter("id");
        customerService.delete(id);
        request.setAttribute("msg","成功删除客户!");
        return "/msg.jsp";
    }

    public Customer encoding(Customer customer) throws UnsupportedEncodingException{
        /**
         * 为防止传输过程中编码格式不同导致乱码
         * 将编码格式统一为utf-8
         */
        String name=customer.getName();
        String gender=customer.getGender();
        String phone=customer.getPhone();
        String email=customer.getEmail();
        String description=customer.getDescription();

        if(name!=null && !name.trim().isEmpty()){
            name=new String(name.getBytes("ISO-8859-1"),"UTF-8");
            customer.setName(name);
        }
        if(gender!=null && !gender.trim().isEmpty()){
            gender=new String(gender.getBytes("ISO-8859-1"),"UTF-8");
            customer.setGender(gender);
        }
        if(phone!=null && !phone.trim().isEmpty()){
            phone=new String(phone.getBytes("ISO-8859-1"),"UTF-8");
            customer.setPhone(phone);
        }
        if(email!=null && !email.trim().isEmpty()){
            email=new String(email.getBytes("ISO-8859-1"),"UTF-8");
            customer.setEmail(email);
        }
        if(description!=null && !description.trim().isEmpty()){
            description=new String(description.getBytes("ISO-8859-1"),"UTF-8");
            customer.setDescription(description);
        }
        return customer;
    }

    public String getUrl(HttpServletRequest request){
        /**
         * 截取url
         *  /项目名/servletPath?参数字符串
         */
        String contextPath=request.getContextPath(); //获取项目名（只获取项目名）
        String servletPath=request.getServletPath(); //获取除根目录下的项目的相对路径，
                                                     // 如"localhost://8080/web/project/list.jsp"中"/web"为绝对路径
        String queryString=request.getQueryString(); //获取查询字符串，即问号之后的部分

        /**
         *
         */
        if(servletPath.equals("/")){
            servletPath="/CustomerServlet?method=findAll";
        }

        //如果传递的查询字符串含有pageNum这个参数，需要截取掉
        if(queryString.contains("&pageNum=")){
            int index=queryString.lastIndexOf("&pageNum=");
            queryString=queryString.substring(0,index);
        }
        return contextPath + servletPath + "?" + queryString;//返回url
    }

    public String query(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        /**
         * 封装表单数据到Customer对象中
         * 先进行编码，防止乱码出现
         * 获取当前页码pageNum
         * 设置每页记录数pageRecord
         * 调用CustomerService中的query方法封装pageBean
         * 保存pageBean到request域中
         * 转发到list.jsp
         */
        Customer customer=CommonUtils.toBean(request.getParameterMap(),Customer.class);
        encoding(customer); //处理GET请求方式编码问题
        int pageNum=getPageNum(request);
        int pageRecord=10;
        pageBean<Customer> pb=customerService.query(customer,pageNum,pageRecord);

        //获取路径url，保存到pb中
        pb.setUrl(getUrl(request));
        request.setAttribute("pb",pb);
        return "/list.jsp";
    }
}

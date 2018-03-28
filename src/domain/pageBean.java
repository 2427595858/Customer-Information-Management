package domain;
import java.util.List;

/**
 * 分页操作,将相关信息进行Java Bean封装
 * 也就是变量全部声明为私有变量，使用公有的set和get方法来设置和获取相应的值
 */
public class pageBean<Object> {
    private int pageNum;    //当前页码
    private int totalPage;  //总页数,需要根据实际情况计算，set方法用不着
    private int totalRecord;    //总记录数
    private int pageRecord; //每页记录数
    private List<Object> beanList;  //当前页的记录
    private String url;     //获取路径url

    public int getPageNum(){
        return pageNum;
    }

    public void setPageNum(int pageNum){
        this.pageNum=pageNum;
    }

    public int getTotalPage(){
        totalPage=totalRecord/pageRecord;
        return totalRecord%pageRecord==0 ?totalPage : totalPage+1;
    }

    public void setTotalPage(int totalPage){
        this.totalPage=totalPage;
    }

    public int getTotalRecord(){
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord){
        this.totalRecord=totalRecord;
    }

    public int getPageRecord(){
        return pageRecord;
    }

    public void setPageRecord(int pageRecord){
        this.pageRecord=pageRecord;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url=url;
    }

    public List<Object> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<Object> beanList) {
        this.beanList = beanList;
    }
}

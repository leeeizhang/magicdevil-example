package top.magicdevil.example.webapp.sample.bookstore.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import top.magicdevil.example.webapp.sample.bookstore.entity.OrderItem;

public interface IOrderItemDAO extends IDAO<Long, OrderItem> {

    @Override
    @Insert(" INSERT INTO OrderItem(uoid,bid,num) "
            + " VALUES (#{userorder.uoid},#{book.bid},#{num}) ")
    @Options(useGeneratedKeys = true, keyProperty = "oiid")
    Boolean doCreate(OrderItem vo) throws Exception;

    @Override
    @Select(" SELECT COUNT(*) AS NUMS FROM OrderItem ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getAllCount() throws Exception;

    @Select(" SELECT SUM(price*num) AS price FROM OrderItem NATURAL JOIN Book "
            + " WHERE uoid IN (#{uoid}) ")
    @Results(@Result(property = "value", column = "price", javaType = Long.class))
    Double getAllBookPriceByUserOrderID(@Param("uoid") Long uoid) throws Exception;

    @Select(" SELECT SUM(num) AS total FROM OrderItem NATURAL JOIN Book "
            + " WHERE uoid IN (#{uoid}) ")
    @Results(@Result(property = "value", column = "total", javaType = Long.class))
    Long getAllBookCountByUserOrderID(@Param("uoid") Long uoid) throws Exception;

    @Deprecated
    @Select(" SELECT COUNT(*) AS NUMS FROM OrderItem "
            + " WHERE ${columnName} LIKE (#{keyword}) ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getCountByColomn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    default Long getCountByColomn(
            String columnName,
            List<String> keyword) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (String item : keyword) {
            stb = stb.append(item.concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.getCountByColomn(columnName, stb.toString());
    }

    default Long getCountByBookID(Long bid) throws Exception {
        return this.getCountByColomn("bid", bid.toString());
    }

    default Long getCountByUserOrderID(Long uoid) throws Exception {
        return this.getCountByColomn("uoid", uoid.toString());
    }

    @Override
    @Select(" SELECT oiid,uoid,bid,num FROM OrderItem WHERE oiid=#{oiid} ")
    @Results(id = "OrderItemMap", value = {
            @Result(property = "oiid", column = "oiid", javaType = Long.class, id = true),
            @Result(property = "num", column = "num", javaType = Long.class),
            @Result(property = "userorder", column = "uoid", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IUserOrderDAO.findByID", fetchType = FetchType.LAZY)),
            @Result(property = "book", column = "bid", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IBookDAO.findByID", fetchType = FetchType.LAZY))
    })
    OrderItem findByID(@Param("oiid") Long id) throws Exception;

    @Select(" SELECT oiid,uoid,bid,num FROM OrderItem WHERE bid IN (#{bid}) ")
    @ResultMap("OrderItemMap")
    List<OrderItem> findByBookID(@Param("bid") Long bid) throws Exception;

    default List<OrderItem> findByBookIDAndSplit(
            Long bid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("bid", bid.toString(), currentIndex, pageSize);
    }

    @Select(" SELECT oiid,uoid,bid,num FROM OrderItem WHERE uoid IN (#{uoid}) ")
    @ResultMap("OrderItemMap")
    List<OrderItem> findByUserOrderID(@Param("uoid") Long uoid) throws Exception;

    default List<OrderItem> findByUserOrderIDAndSplit(
            Long uoid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("uoid", uoid.toString(), currentIndex, pageSize);
    }

    @Override
    @Deprecated
    @Select(" SELECT oiid,uoid,bid,num FROM OrderItem ")
    @ResultMap("OrderItemMap")
    List<OrderItem> findAll() throws Exception;

    @Override
    @Select(" SELECT oiid,uoid,bid,num FROM OrderItem "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("OrderItemMap")
    List<OrderItem> findBunchBySplit(
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Deprecated
    @Select(" SELECT oiid,uoid,bid,num FROM OrderItem "
            + " WHERE ${columnName} IN (#{keyword}) ")
    @ResultMap("OrderItemMap")
    List<OrderItem> findBunchByColumn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    @Deprecated
    default List<OrderItem> findBunchByColumn(
            String columnName,
            List<String> keyword) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (String item : keyword) {
            stb = stb.append(item.concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.findBunchByColumn(columnName, stb.toString());
    }

    @Deprecated
    @Select(" SELECT oiid,uoid,bid,num FROM OrderItem "
            + " WHERE ${columnName} IN (#{keyword}) "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("OrderItemMap")
    List<OrderItem> findBunchByColumnAndSplit(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword,
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Override
    default List<OrderItem> findBunchByColumnAndSplit(
            String columnName,
            List<String> keyword,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (String item : keyword) {
            stb = stb.append(item.concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.findBunchByColumnAndSplit(columnName, stb.toString(), currentIndex, pageSize);
    }

    @Override
    @Update(" UPDATE OrderItem SET uoid=#{userorder.uoid},bid=#{book.bid},num=#{num} "
            + " WHERE oiid=#{oiid} ")
    Boolean doUpdate(OrderItem vo) throws Exception;

    @Override
    @Delete(" DELETE FROM OrderItem WHERE oiid IN (#{oiid}) ")
    Boolean doDeleteByID(@Param("oiid") Long id) throws Exception;

    @Deprecated
    @Delete(" DELETE FROM OrderItem WHERE oiid IN (${oiids}) ")
    Boolean doDeleteBunch(@Param("oiids") String ids) throws Exception;

    @Override
    default Boolean doDeleteBunch(Set<OrderItem> ids) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (OrderItem item : ids) {
            stb = stb.append(String.valueOf(item.getOiid()).concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.doDeleteBunch(stb.toString());
    }

}

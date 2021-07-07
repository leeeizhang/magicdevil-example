package top.magicdevil.example.webapp.sample.bookstore.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import top.magicdevil.example.webapp.sample.bookstore.entity.UserOrder;

public interface IUserOrderDAO extends IDAO<Long, UserOrder> {

    @Override
    @Insert(" INSERT INTO UserOrder(uid,paid,address_id) "
            + " VALUES (#{user.uid},#{paid},#{address.aid}) ")
    @Options(useGeneratedKeys = true, keyProperty = "uoid")
    Boolean doCreate(UserOrder vo) throws Exception;

    @Override
    @Select(" SELECT COUNT(*) AS NUMS FROM UserOrder ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getAllCount() throws Exception;

    @Deprecated
    @Select(" SELECT COUNT(*) AS NUMS FROM UserOrder "
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

    default Long getCountByUserID(Long uid) throws Exception {
        return this.getCountByColomn("uid", uid.toString());
    }

    default Long getCountByAddressID(Long aid) throws Exception {
        return this.getCountByColomn("aid", aid.toString());
    }

    default Long getCountByPaid(Boolean paid) throws Exception {
        return this.getCountByColomn("paid", paid.toString());
    }

    @Override
    @Select(" SELECT uoid,uid,paid,address_id FROM UserOrder "
            + " WHERE uoid=#{uoid} ")
    @Results(id = "UserOrderMap", value = {
            @Result(property = "uoid", column = "uoid", javaType = Long.class, id = true),
            @Result(property = "paid", column = "paid", javaType = Integer.class),
            @Result(property = "user", column = "uid", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IUserDAO.findByID", fetchType = FetchType.LAZY)),
            @Result(property = "address", column = "address_id", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IAddressDAO.findByID", fetchType = FetchType.LAZY)),
            @Result(property = "total", column = "uoid", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IOrderItemDAO.getAllBookCountByUserOrderID", fetchType = FetchType.LAZY)),
            @Result(property = "price", column = "uoid", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IOrderItemDAO.getAllBookPriceByUserOrderID", fetchType = FetchType.LAZY)),
            @Result(property = "orderitem", column = "uoid", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IOrderItemDAO.findByUserOrderID", fetchType = FetchType.LAZY))
    })
    UserOrder findByID(@Param("uoid") Long id) throws Exception;

    @Select(" SELECT uoid,uid,paid,address_id FROM UserOrder "
            + " WHERE uid IN (#{uid}) ")
    @ResultMap("UserOrderMap")
    UserOrder findByUserID(@Param("uid") Long uid) throws Exception;

    default List<UserOrder> findByUserIDAndSplit(
            Long uid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("uid", uid.toString(), currentIndex, pageSize);
    }

    @Select(" SELECT uoid,uid,paid,address_id FROM UserOrder "
            + " WHERE address_id IN (#{aid}) ")
    @ResultMap("UserOrderMap")
    UserOrder findByAddressID(@Param("aid") Long aid) throws Exception;

    default List<UserOrder> findByAddressIDAndSplit(
            Long aid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("aid", aid.toString(), currentIndex, pageSize);
    }

    default List<UserOrder> findByPaid(Boolean paid) throws Exception {
        return this.findBunchByColumn("paid", paid ? "1" : "0");
    }

    default List<UserOrder> findByPaidAndSplit(
            Boolean paid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("paid", paid ? "1" : "0", currentIndex, pageSize);
    }

    @Override
    @Deprecated
    @Select(" SELECT uoid,uid,paid,address_id FROM UserOrder ")
    @ResultMap("UserOrderMap")
    List<UserOrder> findAll() throws Exception;

    @Override
    @Select(" SELECT uoid,uid,paid,address_id FROM UserOrder "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("UserOrderMap")
    List<UserOrder> findBunchBySplit(
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Deprecated
    @Select(" SELECT uoid,uid,paid,address_id FROM UserOrder "
            + " WHERE ${columnName} IN (#{keyword}) ")
    @ResultMap("UserOrderMap")
    List<UserOrder> findBunchByColumn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    @Deprecated
    default List<UserOrder> findBunchByColumn(
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
    @Select(" SELECT uoid,uid,paid,address_id "
            + " FROM UserOrder "
            + " WHERE ${columnName} IN (#{keyword}) "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("UserOrderMap")
    List<UserOrder> findBunchByColumnAndSplit(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword,
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Override
    default List<UserOrder> findBunchByColumnAndSplit(
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
    @Update(" UPDATE UserOrder SET uid=#{user.uid},paid=#{paid},address_id=#{address.aid} "
            + " WHERE uoid=#{uoid} ")
    Boolean doUpdate(UserOrder vo) throws Exception;

    default Boolean doUpdatePaidBunch(Integer paidState, Set<UserOrder> ids) throws Exception {
        for (UserOrder item : ids) {
            item.setPaid(paidState);
            if (this.doUpdate(item) == false) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Delete(" DELETE FROM UserOrder WHERE uoid IN (#{uoid}) ")
    Boolean doDeleteByID(@Param("uoid") Long id) throws Exception;

    @Deprecated
    @Delete(" DELETE FROM UserOrder WHERE uoid IN (${uoids}) ")
    Boolean doDeleteBunch(@Param("uoids") String ids) throws Exception;

    @Override
    default Boolean doDeleteBunch(Set<UserOrder> ids) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (UserOrder item : ids) {
            stb = stb.append(String.valueOf(item.getUoid()).concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.doDeleteBunch(stb.toString());
    }

}

package top.magicdevil.example.webapp.sample.bookstore.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import top.magicdevil.example.webapp.sample.bookstore.entity.Address;

public interface IAddressDAO extends IDAO<Long, Address> {

    @Override
    @Insert(" INSERT INTO Address(uid,location) "
            + " VALUES (#{user.uid},#{location}) ")
    @Options(useGeneratedKeys = true, keyProperty = "aid")
    Boolean doCreate(Address vo) throws Exception;

    @Override
    @Select(" SELECT COUNT(*) AS NUMS FROM Address ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getAllCount() throws Exception;

    @Deprecated
    @Select(" SELECT COUNT(*) AS NUMS FROM Address "
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

    @Override
    @Select(" SELECT aid,uid,location FROM Address "
            + " WHERE aid=#{aid} ")
    @Results(id = "AddressMap", value = {
            @Result(property = "aid", column = "aid", javaType = Long.class, id = true),
            @Result(property = "location", column = "location", javaType = String.class),
            @Result(property = "user", column = "uid", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IUserDAO.findByID", fetchType = FetchType.LAZY)),
            @Result(property = "userorder", column = "aid", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IUserOrderDAO.findByAddressID", fetchType = FetchType.LAZY))
    })
    Address findByID(@Param("aid") Long id) throws Exception;

    @Select(" SELECT aid,uid,location FROM Address "
            + " WHERE uid IN (#{uid}) ")
    @ResultMap("AddressMap")
    List<Address> findByUserID(@Param("uid") Long uid) throws Exception;

    default List<Address> findByUserIDAndSplit(
            Long uid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("uid", uid.toString(), currentIndex, pageSize);
    }

    @Override
    @Deprecated
    @Select(" SELECT aid,uid,location FROM Address ")
    @ResultMap("AddressMap")
    List<Address> findAll() throws Exception;

    @Override
    @Select(" SELECT aid,uid,location FROM Address "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("AddressMap")
    List<Address> findBunchBySplit(
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Deprecated
    @Select(" SELECT aid,uid,location FROM Address "
            + " WHERE ${columnName} IN (#{keyword}) ")
    @ResultMap("AddressMap")
    List<Address> findBunchByColumn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    @Deprecated
    default List<Address> findBunchByColumn(
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
    @Select(" SELECT aid,uid,location "
            + " FROM Address "
            + " WHERE ${columnName} IN (#{keyword}) "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("AddressMap")
    List<Address> findBunchByColumnAndSplit(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword,
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Override
    default List<Address> findBunchByColumnAndSplit(
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
    @Update(" UPDATE Address SET uid=#{user.uid},location=#{location} "
            + " WHERE aid=#{aid}")
    Boolean doUpdate(Address vo) throws Exception;

    @Override
    @Delete(" DELETE FROM Address WHERE aid IN (#{aid}) ")
    Boolean doDeleteByID(@Param("aid") Long id) throws Exception;

    @Deprecated
    @Delete(" DELETE FROM Address WHERE aid IN (${aids}) ")
    Boolean doDeleteBunch(@Param("aids") String ids) throws Exception;

    @Override
    default Boolean doDeleteBunch(Set<Address> ids) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (Address item : ids) {
            stb = stb.append(String.valueOf(item.getAid()).concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.doDeleteBunch(stb.toString());
    }

}
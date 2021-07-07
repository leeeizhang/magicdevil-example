package top.magicdevil.example.webapp.sample.bookstore.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import top.magicdevil.example.webapp.sample.bookstore.entity.Career;

public interface ICareerDAO extends IDAO<Long, Career> {

    @Override
    @Insert(" INSERT INTO Career(cname) VALUES (#{cname}) ")
    @Options(useGeneratedKeys = true, keyProperty = "cid")
    Boolean doCreate(Career vo) throws Exception;

    @Override
    @Select(" SELECT COUNT(*) AS NUMS FROM Career ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getAllCount() throws Exception;

    @Deprecated
    @Select(" SELECT COUNT(*) AS NUMS FROM Career "
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

    @Override
    @Select(" SELECT cid,cname FROM Career WHERE cid=#{cid} ")
    @Results(id = "CareerMap", value = {
            @Result(property = "cid", column = "cid", javaType = Long.class, id = true),
            @Result(property = "cname", column = "cname", javaType = String.class),
            @Result(property = "user", column = "cid", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IUserDAO.findByCareerID", fetchType = FetchType.LAZY))
    })
    Career findByID(@Param("cid") Long id) throws Exception;

    @Override
    @Deprecated
    @Select(" SELECT cid,cname FROM Career ")
    @ResultMap("CareerMap")
    List<Career> findAll() throws Exception;

    @Override
    @Select(" SELECT cid,cname FROM Career LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("CareerMap")
    List<Career> findBunchBySplit(
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Deprecated
    @Select(" SELECT cid,cname FROM Career WHERE ${columnName} IN (#{keyword}) ")
    @ResultMap("CareerMap")
    List<Career> findBunchByColumn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    @Deprecated
    default List<Career> findBunchByColumn(
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
    @Select(" SELECT cid,cname "
            + " FROM Career "
            + " WHERE ${columnName} IN (#{keyword}) "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("CareerMap")
    List<Career> findBunchByColumnAndSplit(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword,
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Override
    default List<Career> findBunchByColumnAndSplit(
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
    @Update(" UPDATE Career SET cid=#{cid},cname=#{cname} WHERE cid=#{cid} ")
    Boolean doUpdate(Career vo) throws Exception;

    @Override
    @Delete(" DELETE FROM Career WHERE cid IN (#{cid}) ")
    Boolean doDeleteByID(@Param("cid") Long id) throws Exception;

    @Deprecated
    @Delete(" DELETE FROM Career WHERE cid IN (${cids}) ")
    Boolean doDeleteBunch(@Param("cids") String ids) throws Exception;

    @Override
    default Boolean doDeleteBunch(Set<Career> ids) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (Career item : ids) {
            stb = stb.append(String.valueOf(item.getCid()).concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.doDeleteBunch(stb.toString());
    }

}
package top.magicdevil.example.webapp.sample.bookstore.dao;

import java.util.Date;
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

import top.magicdevil.example.webapp.sample.bookstore.entity.UserBookVisit;

public interface IUserBookVisitDAO extends IDAO<Long, UserBookVisit> {

    @Override
    @Insert(" INSERT INTO UserBookVisit(uid,bid,vtime) "
            + " VALUES (#{user.uid},#{book.bid},#{vtime}) ")
    @Options(useGeneratedKeys = true, keyProperty = "vid")
    Boolean doCreate(UserBookVisit vo) throws Exception;

    @Override
    @Select(" SELECT COUNT(*) AS NUMS FROM UserBookVisit ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getAllCount() throws Exception;

    @Deprecated
    @Select(" SELECT COUNT(*) AS NUMS FROM UserBookVisit "
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

    default Long getCountByBookID(Long bid) throws Exception {
        return this.getCountByColomn("bid", bid.toString());
    }

    @Override
    @Select("SELECT vid,uid,bid,vtime FROM UserBookVisit "
            + " WHERE vid=#{vid} ORDER BY vtime DESC ")
    @Results(id = "UserBookVisitMap", value = {
            @Result(property = "vid", column = "vid", javaType = Long.class, id = true),
            @Result(property = "vtime", column = "vtime", javaType = Date.class),
            @Result(property = "user", column = "uid", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IUserDAO.findByID", fetchType = FetchType.LAZY)),
            @Result(property = "book", column = "bid", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IBookDAO.findByID", fetchType = FetchType.LAZY))
    })
    UserBookVisit findByID(@Param("vid") Long id) throws Exception;

    @Select("SELECT vid,uid,bid,vtime FROM UserBookVisit "
            + " WHERE uid IN (#{uid}) ORDER BY vtime DESC ")
    @ResultMap("UserBookVisitMap")
    List<UserBookVisit> findByUserID(@Param("uid") Long uoid) throws Exception;

    default List<UserBookVisit> findByUserIDAndSplit(
            Long uid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("uid", uid.toString(), currentIndex, pageSize);
    }

    @Select("SELECT vid,uid,bid,vtime FROM UserBookVisit "
            + " WHERE bid IN (#{bid}) ORDER BY vtime DESC")
    @ResultMap("UserBookVisitMap")
    List<UserBookVisit> findByBookID(@Param("bid") Long uoid) throws Exception;

    default List<UserBookVisit> findByBookIDAndSplit(
            Long bid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("bid", bid.toString(), currentIndex, pageSize);
    }

    @Override
    @Deprecated
    @Select("SELECT vid,uid,bid,vtime FROM UserBookVisit "
            + " ORDER BY vtime DESC ")
    @ResultMap("UserBookVisitMap")
    List<UserBookVisit> findAll() throws Exception;

    @Override
    @Select(" SELECT vid,uid,bid,vtime FROM UserBookVisit "
            + " ORDER BY vtime DESC LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("UserBookVisitMap")
    List<UserBookVisit> findBunchBySplit(
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Deprecated
    @Select(" SELECT vid,uid,bid,vtime FROM UserBookVisit "
            + " WHERE ${columnName} IN (#{keyword}) ORDER BY vtime DESC")
    @ResultMap("UserBookVisitMap")
    List<UserBookVisit> findBunchByColumn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    default List<UserBookVisit> findBunchByColumn(
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
    @Select(" SELECT vid,uid,bid,vtime FROM UserBookVisit "
            + " WHERE ${columnName} IN (#{keyword}) "
            + " ORDER BY vtime DESC LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("UserBookVisitMap")
    List<UserBookVisit> findBunchByColumnAndSplit(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword,
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Override
    default List<UserBookVisit> findBunchByColumnAndSplit(
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
    @Update("UPDATE UserBookVisit SET uid=#{user.uid},bid=#{book.bid},vtime=#{vtime} WHERE vid=#{vid}")
    Boolean doUpdate(UserBookVisit vo) throws Exception;

    @Override
    @Delete("DELETE FROM UserBookVisit WHERE vid IN (#{vid})")
    Boolean doDeleteByID(@Param("vid") Long id) throws Exception;

    @Deprecated
    @Delete("DELETE FROM UserBookVisit WHERE vid IN (${vids})")
    Boolean doDeleteBunch(@Param("vids") String ids) throws Exception;

    @Override
    default Boolean doDeleteBunch(Set<UserBookVisit> ids) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (UserBookVisit item : ids) {
            stb = stb.append(String.valueOf(item.getVid()).concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.doDeleteBunch(stb.toString());
    }

}

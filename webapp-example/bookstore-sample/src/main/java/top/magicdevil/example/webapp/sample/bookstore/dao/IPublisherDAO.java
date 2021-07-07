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

import top.magicdevil.example.webapp.sample.bookstore.entity.Publisher;

public interface IPublisherDAO extends IDAO<Long, Publisher> {

    @Override
    @Insert(" INSERT INTO Publisher(pname) VALUES (#{pname}) ")
    @Options(useGeneratedKeys = true, keyProperty = "pid")
    Boolean doCreate(Publisher vo) throws Exception;

    @Override
    @Select(" SELECT COUNT(*) AS NUMS FROM Publisher ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getAllCount() throws Exception;

    @Deprecated
    @Select(" SELECT COUNT(*) AS NUMS FROM Publisher "
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
    @Select(" SELECT pid,pname FROM Publisher WHERE pid=#{pid} ")
    @Results(id = "PublisherMap", value = {
            @Result(property = "pid", column = "pid", javaType = Long.class, id = true),
            @Result(property = "pname", column = "pname", javaType = String.class),
            @Result(property = "book", column = "pid", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IBookDAO.findByPublisherID", fetchType = FetchType.LAZY))
    })
    Publisher findByID(Long id) throws Exception;

    @Override
    @Deprecated
    @Select(" SELECT pid,pname FROM Publisher ")
    @ResultMap("PublisherMap")
    List<Publisher> findAll() throws Exception;

    @Override
    @Select(" SELECT pid,pname FROM Publisher LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("PublisherMap")
    List<Publisher> findBunchBySplit(
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Deprecated
    @Select(" SELECT pid,pname FROM Publisher WHERE ${columnName} IN (#{keyword}) ")
    @ResultMap("PublisherMap")
    List<Publisher> findBunchByColumn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    @Deprecated
    default List<Publisher> findBunchByColumn(
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
    @Select(" SELECT pid,pname "
            + " FROM Publisher "
            + " WHERE ${columnName} IN (#{keyword}) "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("PublisherMap")
    List<Publisher> findBunchByColumnAndSplit(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword,
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Override
    default List<Publisher> findBunchByColumnAndSplit(
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
    @Update(" UPDATE Publisher SET pname=#{pname} WHERE pid=#{pid} ")
    Boolean doUpdate(Publisher vo) throws Exception;

    @Override
    @Delete(" DELETE FROM Publisher WHERE pid IN (#{pid}) ")
    Boolean doDeleteByID(@Param("id") Long id) throws Exception;

    @Deprecated
    @Delete(" DELETE FROM Publisher WHERE pid IN (${pids}) ")
    Boolean doDeleteBunch(@Param("pids") String ids) throws Exception;

    @Override
    default Boolean doDeleteBunch(Set<Publisher> ids) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (Publisher item : ids) {
            stb = stb.append(String.valueOf(item.getPid()).concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.doDeleteBunch(stb.toString());
    }

}

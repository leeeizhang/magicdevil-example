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

import top.magicdevil.example.webapp.sample.bookstore.entity.BookType;

public interface IBookTypeDAO extends IDAO<Long, BookType> {

    @Override
    @Insert(" INSERT INTO BookType(tname) VALUES (#{tname}) ")
    @Options(useGeneratedKeys = true, keyProperty = "tid")
    Boolean doCreate(BookType vo) throws Exception;

    @Override
    @Select(" SELECT COUNT(*) AS NUMS FROM BookType ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getAllCount() throws Exception;

    @Deprecated
    @Select(" SELECT COUNT(*) AS NUMS FROM BookType "
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
    @Select(" SELECT tid,tname FROM BookType WHERE tid=#{tid} ")
    @Results(id = "BookTypeMap", value = {
            @Result(property = "tid", column = "tid", javaType = Long.class, id = true),
            @Result(property = "tname", column = "tname", javaType = String.class),
            @Result(property = "book", column = "tid", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IBookDAO.findByBookTypeID", fetchType = FetchType.LAZY))
    })
    BookType findByID(@Param("tid") Long id) throws Exception;

    @Override
    @Deprecated
    @Select(" SELECT tid,tname FROM BookType ")
    @ResultMap("BookTypeMap")
    List<BookType> findAll() throws Exception;

    @Override
    @Select(" SELECT tid,tname FROM BookType LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("BookTypeMap")
    List<BookType> findBunchBySplit(
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Deprecated
    @Select(" SELECT tid,tname FROM BookType WHERE ${columnName} IN (#{keyword}) ")
    @ResultMap("BookTypeMap")
    List<BookType> findBunchByColumn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    @Deprecated
    default List<BookType> findBunchByColumn(
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
    @Select(" SELECT tid,tname "
            + " FROM BookType "
            + " WHERE ${columnName} IN (#{keyword}) "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("BookTypeMap")
    List<BookType> findBunchByColumnAndSplit(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword,
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Override
    default List<BookType> findBunchByColumnAndSplit(
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
    @Update(" UPDATE BookType SET tname=#{tname} WHERE tid=#{tid} ")
    Boolean doUpdate(BookType vo) throws Exception;

    @Override
    @Delete(" DELETE FROM BookType WHERE tid IN (#{tid}) ")
    Boolean doDeleteByID(@Param("tid") Long id) throws Exception;

    @Deprecated
    @Delete(" DELETE FROM BookType WHERE tid IN (${tids}) ")
    Boolean doDeleteBunch(@Param("tids") String ids) throws Exception;

    @Override
    default Boolean doDeleteBunch(Set<BookType> ids) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (BookType item : ids) {
            stb = stb.append(String.valueOf(item.getTid()).concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.doDeleteBunch(stb.toString());
    }

}
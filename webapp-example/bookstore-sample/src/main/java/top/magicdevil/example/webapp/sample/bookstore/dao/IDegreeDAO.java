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

import top.magicdevil.example.webapp.sample.bookstore.entity.Degree;

public interface IDegreeDAO extends IDAO<Long, Degree> {

    @Override
    @Insert(" INSERT INTO Degree(dname) VALUES (#{dname}) ")
    @Options(useGeneratedKeys = true, keyProperty = "did")
    Boolean doCreate(Degree vo) throws Exception;

    @Override
    @Select(" SELECT COUNT(*) AS NUMS FROM Degree ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getAllCount() throws Exception;

    @Deprecated
    @Select(" SELECT COUNT(*) AS NUMS FROM Degree "
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
    @Select(" SELECT did,dname FROM Degree WHERE did=#{did} ")
    @Results(id = "DegreeMap", value = {
            @Result(property = "did", column = "did", javaType = Long.class, id = true),
            @Result(property = "dname", column = "dname", javaType = String.class),
            @Result(property = "user", column = "did", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IUserDAO.findByDegreeID", fetchType = FetchType.LAZY))
    })
    Degree findByID(@Param("did") Long id) throws Exception;

    @Override
    @Deprecated
    @Select(" SELECT did,dname FROM Degree ")
    @ResultMap("DegreeMap")
    List<Degree> findAll() throws Exception;

    @Override
    @Select(" SELECT did,dname FROM Degree LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("DegreeMap")
    List<Degree> findBunchBySplit(
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize);

    @Deprecated
    @Select(" SELECT did,dname FROM Degree WHERE ${columnName} IN (#{keyword}) ")
    @ResultMap("DegreeMap")
    List<Degree> findBunchByColumn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    @Deprecated
    default List<Degree> findBunchByColumn(
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
    @Select(" SELECT did,dname "
            + " FROM Degree "
            + " WHERE ${columnName} IN (#{keyword}) "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("DegreeMap")
    List<Degree> findBunchByColumnAndSplit(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword,
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Override
    default List<Degree> findBunchByColumnAndSplit(
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
    @Update(" UPDATE Degree SET did=#{did},dname=#{dname} WHERE did=#{did} ")
    Boolean doUpdate(Degree vo) throws Exception;

    @Override
    @Delete(" DELETE FROM Degree WHERE did IN (#{did}) ")
    Boolean doDeleteByID(@Param("did") Long id) throws Exception;

    @Deprecated
    @Delete(" DELETE FROM Degree WHERE did IN (${dids}) ")
    Boolean doDeleteBunch(@Param("dids") String ids) throws Exception;

    @Override
    default Boolean doDeleteBunch(Set<Degree> ids) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (Degree item : ids) {
            stb = stb.append(String.valueOf(item.getDid()).concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.doDeleteBunch(stb.toString());
    }

}

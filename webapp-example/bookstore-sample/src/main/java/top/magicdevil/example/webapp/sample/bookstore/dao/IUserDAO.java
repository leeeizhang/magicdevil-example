package top.magicdevil.example.webapp.sample.bookstore.dao;

import java.util.Date;
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

import top.magicdevil.example.webapp.sample.bookstore.entity.Address;
import top.magicdevil.example.webapp.sample.bookstore.entity.User;

public interface IUserDAO extends IDAO<Long, User> {

    @Override
    @Insert(" INSERT INTO User(uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag) "
            + " VALUES (#{uname},#{password},#{sex},#{birth},#{phone},#{email},#{career.cid},#{degree.did},#{admin_tag}) ")
    @Options(useGeneratedKeys = true, keyProperty = "uid")
    Boolean doCreate(User vo) throws Exception;

    @Override
    @Select(" SELECT COUNT(*) AS NUMS FROM User ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getAllCount() throws Exception;

    @Deprecated
    @Select(" SELECT COUNT(*) AS NUMS FROM User "
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

    default Long getCountByCareerID(Long cid) throws Exception {
        return this.getCountByColomn("career_id", cid.toString());
    }

    default Long getCountByDegreeID(Long did) throws Exception {
        return this.getCountByColomn("degree_id", did.toString());
    }

    default Long getCountByAdminTag(Integer admin_tag) throws Exception {
        return this.getCountByColomn("admin_tag", admin_tag.toString());
    }

    default Long getCountLikeUname(String uname) throws Exception {
        return this.getCountByColomn("uname", uname);
    }

    default Long getCountLikePhone(String phone) throws Exception {
        return this.getCountByColomn("phone", phone);
    }

    default Long getCountLikeEmail(String email) throws Exception {
        return this.getCountByColomn("email", email);
    }

    @Override
    @Select(" SELECT uid,uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag "
            + " FROM User WHERE uid=#{uid} ")
    @Results(id = "UserMap", value = {
            @Result(property = "uid", column = "uid", javaType = Long.class, id = true),
            @Result(property = "uname", column = "uname", javaType = String.class),
            @Result(property = "password", column = "password", javaType = String.class),
            @Result(property = "sex", column = "sex", javaType = Integer.class),
            @Result(property = "birth", column = "birth", javaType = Date.class),
            @Result(property = "phone", column = "phone", javaType = String.class),
            @Result(property = "email", column = "email", javaType = String.class),
            @Result(property = "career", column = "career_id", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.ICareerDAO.findByID", fetchType = FetchType.LAZY)),
            @Result(property = "degree", column = "degree_id", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IDegreeDAO.findByID", fetchType = FetchType.LAZY)),
            @Result(property = "admin_tag", column = "admin_tag", javaType = Integer.class),
            @Result(property = "address", column = "uid", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IAddressDAO.findByUserID", fetchType = FetchType.LAZY)),
            @Result(property = "userorder", column = "uid", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IUserOrderDAO.findByUserID", fetchType = FetchType.LAZY)),
            @Result(property = "userbookvisit", column = "uid", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IUserBookVisitDAO.findByUserID", fetchType = FetchType.LAZY))
    })
    User findByID(@Param("uid") Long id) throws Exception;

    @Select(" SELECT uid,uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag "
            + " FROM User WHERE career_id=#{cid} ")
    @ResultMap("UserMap")
    List<Address> findByCareerID(@Param("cid") Long cid) throws Exception;

    default List<User> findByCareerIDAndSplit(
            Long cid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("career_id", cid.toString(), currentIndex, pageSize);
    }

    @Select(" SELECT uid,uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag "
            + " FROM User WHERE degree_id=#{did} ")
    @ResultMap("UserMap")
    List<Address> findByDegreeID(@Param("did") Long did) throws Exception;

    default List<User> findByDegreeIDAndSplit(
            Long did,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("degree_id", did.toString(), currentIndex, pageSize);
    }

    default List<User> findByAdminTag(Integer admin_tag) throws Exception {
        return this.findBunchByColumn("admin_tag", admin_tag.toString());
    }

    default List<User> findByAdminTagAndSplit(
            Integer admin_tag,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("admin_tag", admin_tag.toString(), currentIndex,
                pageSize);
    }

    default List<User> findByUname(String uname) throws Exception {
        return this.findBunchByColumn("uname", uname);
    }

    default List<User> findLikeUnameAndSplit(
            String username,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("uname", "%" + username + "%", currentIndex,
                pageSize);
    }

    default List<User> findByPhone(String phone) throws Exception {
        return this.findBunchByColumn("phone", phone);
    }

    default List<User> findLikePhoneAndSplitFuzzy(
            String phone,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("phone", "%" + phone + "%", currentIndex,
                pageSize);
    }

    default List<User> findByEmail(String email) throws Exception {
        return this.findBunchByColumn("email", email);
    }

    default List<User> findLikeEmailAndSplitFuzzy(
            String email,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("email", "%" + email + "%", currentIndex,
                pageSize);
    }

    @Override
    @Deprecated
    @Select(" SELECT uid,uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag "
            + " FROM User ")
    @ResultMap("UserMap")
    List<User> findAll() throws Exception;

    @Override
    @Select(" SELECT uid,uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag "
            + " FROM User LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("UserMap")
    List<User> findBunchBySplit(
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Deprecated
    @Select(" SELECT uid,uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag FROM User "
            + " WHERE ${columnName} IN (#{keyword}) ")
    @ResultMap("UserMap")
    List<User> findBunchByColumn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    @Deprecated
    default List<User> findBunchByColumn(
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
    @Select(" SELECT uid,uname,password,sex,birth,phone,email,career_id,degree_id,admin_tag "
            + " FROM User "
            + " WHERE ${columnName} LIKE (#{keyword}) "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("UserMap")
    List<User> findBunchByColumnAndSplit(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword,
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Override
    default List<User> findBunchByColumnAndSplit(
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
    @Update(" UPDATE User SET "
            + " uname=#{uname},password=#{password},sex=#{sex}, "
            + " birth=#{birth},phone=#{phone},email=#{email}, "
            + " career_id=#{career.cid},degree_id=#{degree.did},admin_tag=#{admin_tag} "
            + " WHERE uid=#{uid} ")
    Boolean doUpdate(User vo) throws Exception;

    @Override
    @Delete(" DELETE FROM User WHERE uid IN (#{uid}) ")
    Boolean doDeleteByID(@Param("uid") Long id) throws Exception;

    @Deprecated
    @Delete(" DELETE FROM User WHERE uid IN (${uids}) ")
    Boolean doDeleteBunch(@Param("uids") String ids) throws Exception;

    @Override
    default Boolean doDeleteBunch(Set<User> ids) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (User item : ids) {
            stb = stb.append(String.valueOf(item.getUid()).concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.doDeleteBunch(stb.toString());
    }

}
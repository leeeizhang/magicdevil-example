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

import top.magicdevil.example.webapp.sample.bookstore.entity.Book;

public interface IBookDAO extends IDAO<Long, Book> {

    @Override
    @Insert(" INSERT INTO Book(bname,isbn,price,stock,pic,type_id,publisher_id) "
            + " VALUES (#{bname},#{isbn},#{price},#{stock},#{pic},#{booktype.tid},#{publisher.pid}) ")
    @Options(useGeneratedKeys = true, keyProperty = "bid")
    Boolean doCreate(Book vo) throws Exception;

    @Override
    @Select(" SELECT COUNT(*) AS NUMS FROM Book ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getAllCount() throws Exception;

    @Deprecated
    @Select(" SELECT COUNT(*) AS NUMS FROM Book "
            + " WHERE ${columnName} LIKE (#{keyword}) ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getCountByColomn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    default Long getCountByPublisherID(Long pid) throws Exception {
        return this.getCountByColomn("publisher_id", pid.toString());
    }

    default Long getCountByBookTypeID(Long tid) throws Exception {
        return this.getCountByColomn("type_id", tid.toString());
    }

    default Long getCountLikeBname(String bname) throws Exception {
        return this.getCountByColomn("bname", bname);
    }

    default Long getCountLikeISBN(String isbn) throws Exception {
        return this.getCountByColomn("isbn", isbn);
    }

    @Select(" SELECT COUNT(*) AS NUMS "
            + " FROM Book JOIN Publisher JOIN BookType "
            + " ON Book.publisher_id = Publisher.pid AND Book.type_id = BookType.tid "
            + " WHERE Book.bname LIKE #{keyword} "
            + " OR Publisher.pname LIKE #{keyword} "
            + " OR BookType.tname LIKE #{keyword} "
            + " OR Book.isbn LIKE #{keyword} ")
    @Results(@Result(property = "value", column = "NUMS", javaType = Long.class))
    Long getCountLikeKeyword(String keyword) throws Exception;

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
    @Select(" SELECT bid,bname,isbn,price,stock,pic,type_id,publisher_id "
            + " FROM Book WHERE bid=#{bid} ")
    @Results(id = "BookMap", value = {
            @Result(property = "bid", column = "bid", javaType = Long.class, id = true),
            @Result(property = "bname", column = "bname", javaType = String.class),
            @Result(property = "isbn", column = "isbn", javaType = String.class),
            @Result(property = "price", column = "price", javaType = Double.class),
            @Result(property = "stock", column = "stock", javaType = Integer.class),
            @Result(property = "pic", column = "pic", javaType = String.class),
            @Result(property = "booktype", column = "type_id", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IBookTypeDAO.findByID", fetchType = FetchType.LAZY)),
            @Result(property = "publisher", column = "publisher_id", one = @One(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IPublisherDAO.findByID", fetchType = FetchType.LAZY)),
            @Result(property = "userbookvisit", column = "bid", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IUserBookVisitDAO.findByBookID", fetchType = FetchType.LAZY)),
            @Result(property = "orderitem", column = "bid", many = @Many(select = "top.magicdevil.example.webapp.sample.bookstore.dao.IOrderItemDAO.findByBookID", fetchType = FetchType.LAZY))
    })
    Book findByID(@Param("bid") Long id) throws Exception;

    @Select(" SELECT bid,bname,isbn,price,stock,pic,type_id,publisher_id "
            + " FROM Book WHERE type_id=#{tid} ")
    @ResultMap("BookMap")
    List<Book> findByBookTypeID(@Param("tid") Long tid) throws Exception;

    default List<Book> findByBookTypeIDAndSplit(
            Long tid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("type_id", tid.toString(), currentIndex, pageSize);
    }

    @Select(" SELECT bid,bname,isbn,price,stock,pic,type_id,publisher_id "
            + " FROM Book WHERE publisher_id=#{pid} ")
    @ResultMap("BookMap")
    List<Book> findByPublisherID(@Param("pid") Long pid) throws Exception;

    default List<Book> findByPublisherIDAndSplit(
            Long pid,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("publisher_id", pid.toString(), currentIndex,
                pageSize);
    }

    default List<Book> findLikeBnameAndSplit(
            String bname,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("bname", "%" + bname + "%", currentIndex,
                pageSize);
    }

    default List<Book> findLikeISBNAndSplit(
            String isbn,
            Integer currentIndex,
            Integer pageSize) throws Exception {
        return this.findBunchByColumnAndSplit("isbn", "%" + isbn + "%", currentIndex,
                pageSize);
    }

    @Select(" SELECT bid,bname,isbn,price,stock,pic,type_id,publisher_id "
            + " FROM Book JOIN Publisher JOIN BookType "
            + " ON Book.publisher_id = Publisher.pid AND Book.type_id = BookType.tid "
            + " WHERE Book.bname LIKE CONCAT('%',#{keyword},'%') "
            + " OR Publisher.pname LIKE CONCAT('%',#{keyword},'%') "
            + " OR BookType.tname LIKE CONCAT('%',#{keyword},'%') "
            + " OR Book.isbn LIKE CONCAT('%',#{keyword},'%') "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("BookMap")
    List<Book> findLikeKeywordAndSplit(
            String keyword,
            Integer currentIndex,
            Integer pageSize) throws Exception;

    @Override
    @Deprecated
    @Select(" SELECT bid,bname,isbn,price,stock,pic,type_id,publisher_id FROM Book ")
    @ResultMap("BookMap")
    List<Book> findAll() throws Exception;

    @Override
    @Select(" SELECT bid,bname,isbn,price,stock,pic,type_id,publisher_id FROM Book "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("BookMap")
    List<Book> findBunchBySplit(
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Deprecated
    @Select(" SELECT bid,bname,isbn,price,stock,pic,type_id,publisher_id FROM Book "
            + " WHERE ${columnName} IN (#{keyword}) ")
    @ResultMap("BookMap")
    List<Book> findBunchByColumn(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword) throws Exception;

    @Override
    default List<Book> findBunchByColumn(
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
    @Select(" SELECT bid,bname,isbn,price,stock,pic,type_id,publisher_id "
            + " FROM Book "
            + " WHERE ${columnName} LIKE (#{keyword}) "
            + " LIMIT #{currentIndex},#{pageSize} ")
    @ResultMap("BookMap")
    List<Book> findBunchByColumnAndSplit(
            @Param("columnName") String columnName,
            @Param("keyword") String keyword,
            @Param("currentIndex") Integer currentIndex,
            @Param("pageSize") Integer pageSize) throws Exception;

    @Override
    default List<Book> findBunchByColumnAndSplit(
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
    @Update(" UPDATE Book SET "
            + " bname=#{bname},isbn=#{isbn},price=#{price}, "
            + " stock=#{stock},pic=#{pic},type_id=#{booktype.tid}, "
            + " publisher_id=#{publisher.pid} "
            + " WHERE bid=#{bid}")
    Boolean doUpdate(Book vo) throws Exception;

    @Override
    @Delete(" DELETE FROM Book WHERE bid IN (#{bid}) ")
    Boolean doDeleteByID(@Param("bid") Long id) throws Exception;

    @Deprecated
    @Delete(" DELETE FROM Book WHERE bid IN (${bids}) ")
    Boolean doDeleteBunch(@Param("bids") String ids) throws Exception;

    @Override
    default Boolean doDeleteBunch(Set<Book> ids) throws Exception {
        StringBuffer stb = new StringBuffer(128);
        for (Book item : ids) {
            stb = stb.append(String.valueOf(item.getBid()).concat(","));
        }
        stb = stb.deleteCharAt(stb.length() - 1);
        return this.doDeleteBunch(stb.toString());
    }

}
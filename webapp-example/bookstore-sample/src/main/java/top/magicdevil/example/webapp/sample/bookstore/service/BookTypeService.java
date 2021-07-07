package top.magicdevil.example.webapp.sample.bookstore.service;

import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.dao.IBookTypeDAO;
import top.magicdevil.example.webapp.sample.bookstore.entity.BookType;

@Service
public class BookTypeService extends SpringAbstractService<Long, BookType, IBookTypeDAO> {

    private static final long serialVersionUID = 1L;

}

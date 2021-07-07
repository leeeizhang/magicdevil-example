package top.magicdevil.example.webapp.sample.bookstore.service;

import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.dao.IDegreeDAO;
import top.magicdevil.example.webapp.sample.bookstore.entity.Degree;

@Service
public class DegreeService extends SpringAbstractService<Long, Degree, IDegreeDAO> {

    private static final long serialVersionUID = 1L;

}

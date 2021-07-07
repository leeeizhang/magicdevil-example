package top.magicdevil.example.webapp.sample.bookstore.service;

import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.dao.ICareerDAO;
import top.magicdevil.example.webapp.sample.bookstore.entity.Career;

@Service
public class CareerService extends SpringAbstractService<Long, Career, ICareerDAO> {

    private static final long serialVersionUID = 1L;

}

package top.magicdevil.example.webapp.sample.bookstore.service;

import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.dao.IPublisherDAO;
import top.magicdevil.example.webapp.sample.bookstore.entity.Publisher;

@Service
public class PublisherService extends SpringAbstractService<Long, Publisher, IPublisherDAO> {

    private static final long serialVersionUID = 1L;

}

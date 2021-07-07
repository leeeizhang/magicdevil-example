package top.magicdevil.example.webapp.sample.bookstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.magicdevil.example.webapp.sample.bookstore.dao")
public class BookStoreSpringApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BookStoreSpringApplication.class, args);
    }

}

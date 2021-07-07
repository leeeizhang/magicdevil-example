package top.magicdevil.example.webapp.sample.bookstore.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import top.magicdevil.example.webapp.sample.bookstore.dao.IUserDAO;
import top.magicdevil.example.webapp.sample.bookstore.entity.Career;
import top.magicdevil.example.webapp.sample.bookstore.entity.Degree;
import top.magicdevil.example.webapp.sample.bookstore.entity.User;

@Service
public class UserService extends SpringAbstractService<Long, User, IUserDAO> {

    private static final long serialVersionUID = 1L;

    public User login(String username, String password) throws Exception {
        List<User> users = new ArrayList<>();
        users.addAll(this.getDao().findByEmail(username));
        users.addAll(this.getDao().findByPhone(username));
        users.addAll(this.getDao().findByUname(username));
        for (User item : users) {
            if (item.getPassword().matches(password)) {
                return item;
            }
        }
        return null;
    }

    public User register(String uname, String password, Integer sex, Date birth, String phone,
                         String email, Career career, Degree degree, Integer admin_tag) throws Exception {
        User user = new User();
        user.setUname(uname);
        user.setPassword(password);
        user.setSex(sex);
        user.setBirth(birth);
        user.setPhone(phone);
        user.setEmail(email);
        user.setCareer(career);
        user.setDegree(degree);
        user.setAdmin_tag(admin_tag);
        return this.insert(user) ? user : null;
    }

    public Long getCountByAdminUser() throws Exception {
        return this.getDao().getCountByAdminTag(1);
    }

    public Long getCountByDegree(Degree degree) throws Exception {
        return this.getDao().getCountByDegreeID(degree.getDid());
    }

    public Long getCountByCareer(Career career) throws Exception {
        return this.getDao().getCountByCareerID(career.getCid());
    }

    public Long getCountLikeUsername(String username) throws Exception {
        return this.getDao().getCountLikeUname(username);
    }

    public Long getCountLikePhone(String phone) throws Exception {
        return this.getDao().getCountLikePhone(phone);
    }

    public Long getCountLikeEmail(String email) throws Exception {
        return this.getDao().getCountLikePhone(email);
    }

    public List<User> getByDegreeAndSplit(
            Degree degree,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByDegreeIDAndSplit(degree.getDid(), (currentPage - 1) * pageSize,
                pageSize);
    }

    public List<User> getByCareerAndSplit(
            Career career,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByCareerIDAndSplit(career.getCid(), (currentPage - 1) * pageSize,
                pageSize);
    }

    public List<User> getByAdminUserAndSplit(
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findByAdminTagAndSplit(1, (currentPage - 1) * pageSize, pageSize);
    }

    public List<User> getLikeUsernameAndSplit(
            String username,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findLikeUnameAndSplit(username, (currentPage - 1) * pageSize,
                pageSize);
    }

    public List<User> getLikePhoneAndSplit(
            String phone,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findLikePhoneAndSplitFuzzy(phone, (currentPage - 1) * pageSize,
                pageSize);
    }

    public List<User> getLikeEmailAndSplit(
            String email,
            Integer currentPage,
            Integer pageSize) throws Exception {
        return this.getDao().findLikeEmailAndSplitFuzzy(email, (currentPage - 1) * pageSize,
                pageSize);
    }

}

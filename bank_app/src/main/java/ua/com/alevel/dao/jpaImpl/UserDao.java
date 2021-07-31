package ua.com.alevel.dao.jpaImpl;

import org.hibernate.Session;
import ua.com.alevel.model.entity.User;
import java.util.List;
import java.util.Optional;

public class UserDao extends JPAFinanceManagerDao<User> {

    public UserDao(Session session) {
        super(session);
    }

    @Override
    public Long create(User user) throws Exception {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) throws Exception {
        return null;
    }

    @Override
    public List<User> readAll() {
        return null;
    }

    @Override
    public void update(User user) throws Exception {

    }

    @Override
    public void delete(Long id) {

    }

    public Boolean checkIfExist(Long id){
        return null;
    }
}

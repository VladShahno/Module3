package ua.com.alevel.dao.jpaImpl;

import org.hibernate.Session;
import ua.com.alevel.model.dto.authorization.AuthorizationRequest;
import ua.com.alevel.model.entity.Account;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AccountDao extends JPAFinanceManagerDao<Account> {

    public AccountDao(Session session) {
        super(session);
    }

    @Override
    public Long create(Account account) throws Exception {
        return null;
    }

    @Override
    public Optional<Account> findById(Long id) throws Exception {
        try {
            return Optional.ofNullable(session.find(Account.class, id));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Account> readAll() {
        return null;
    }

    @Override
    public void update(Account account) throws Exception {
        try {
            session.update(account);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {

    }

    public Boolean checkAuthorization(AuthorizationRequest request) throws Exception {
        try {
            TypedQuery<Boolean> checkIfExists = session.createQuery(
                    "select (count(c) > 0) as exists " +
                            "from Account c where c.userName = :userName and c.user.id =: userId",
                    Boolean.class);

            checkIfExists.setParameter("userName", request.getUserName());
            checkIfExists.setParameter("userId", request.getUserId());
            return checkIfExists.getSingleResult();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Optional<Account> findByUserName(String userName) throws Exception {
        try {
            TypedQuery<Account> checkIfExists = session.createQuery(
                    "select c " +
                            "from Account c where c.userName = :userName ",
                    Account.class);
            checkIfExists.setParameter("userName", userName);
            return Optional.ofNullable(checkIfExists.getSingleResult());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Boolean checkIfExistById(Long id) throws Exception {
        try {
            TypedQuery<Boolean> checkIfExists = session.createQuery(
                    "select (count(c) > 0) as exists " +
                            "from Account c where c.id = :id ",
                    Boolean.class);
            checkIfExists.setParameter("id", id);
            return checkIfExists.getSingleResult();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}

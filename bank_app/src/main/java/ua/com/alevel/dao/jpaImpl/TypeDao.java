package ua.com.alevel.dao.jpaImpl;

import org.hibernate.Session;
import ua.com.alevel.model.entity.Type;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class TypeDao extends JPAFinanceManagerDao<Type> {

    public TypeDao(Session session) {
        super(session);
    }

    @Override
    public Long create(Type type) throws Exception {
        return null;
    }

    @Override
    public Optional<Type> findById(Long id) throws Exception {
        try {
            return Optional.ofNullable(session.find(Type.class, id));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Type> readAll() throws Exception {
        try {
            TypedQuery<Type> findByType = session.createQuery(
                    "select t from Type t",
                    Type.class);
            return findByType.getResultList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void update(Type type) throws Exception {

    }

    @Override
    public void delete(Long id) {

    }

    public Boolean checkIfExistById(Long id) throws Exception {
        try {
            TypedQuery<Boolean> checkIfExists = session.createQuery(
                    "select (count(t) > 0) as exists " +
                            "from Type t where t.id = :id ",
                    Boolean.class);
            checkIfExists.setParameter("id", id);
            return checkIfExists.getSingleResult();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

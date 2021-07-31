package ua.com.alevel.dao.jpaImpl;

import org.hibernate.Session;
import ua.com.alevel.model.entity.Operation;
import java.util.List;
import java.util.Optional;

public class OperationDao extends JPAFinanceManagerDao<Operation> {

    public OperationDao(Session session) {
        super(session);
    }

    @Override
    public Long create(Operation operation) throws Exception {
        try {
            session.persist(operation);
            return operation.getId();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Optional<Operation> findById(Long id) throws Exception {
        return null;
    }

    @Override
    public List<Operation> readAll() {
        return null;
    }

    @Override
    public void update(Operation operation) throws Exception {

    }

    @Override
    public void delete(Long id) {

    }
}

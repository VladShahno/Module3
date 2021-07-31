package ua.com.alevel.dao.jpaImpl;

import org.hibernate.Session;
import ua.com.alevel.model.entity.Category;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CategoryDao extends JPAFinanceManagerDao<Category> {

    public CategoryDao(Session session) {
        super(session);
    }

    @Override
    public Long create(Category category) throws Exception {
        return null;
    }

    @Override
    public Optional<Category> findById(Long id) throws Exception {
        try {
            return Optional.ofNullable(session.find(Category.class, id));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Category> readAll() {
        return null;
    }

    @Override
    public void update(Category category) throws Exception {

    }

    @Override
    public void delete(Long id) {

    }

    public List<Category> findByTypeId(Long id) throws Exception {
        try {
            TypedQuery<Category> findByType = session.createQuery(
                    "select c from Category c where c.type.id =: id",
                    Category.class);
            findByType.setParameter("id", id);
            return findByType.getResultList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}

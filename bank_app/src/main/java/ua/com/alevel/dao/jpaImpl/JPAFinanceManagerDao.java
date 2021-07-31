package ua.com.alevel.dao.jpaImpl;

import org.hibernate.Session;
import ua.com.alevel.dao.FinanceManagerDao;

public abstract class JPAFinanceManagerDao<T> implements FinanceManagerDao<T> {

    Session session;

    public JPAFinanceManagerDao(Session session) {
        this.session = session;
    }

}

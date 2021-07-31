package ua.com.alevel.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.*;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(String password) {
        if (sessionFactory != null)
            return sessionFactory;
        try {
            Configuration configuration = new Configuration();
            configuration.setProperty("connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/module13");
            configuration.setProperty("hibernate.connection.username", "root");
            configuration.setProperty("hibernate.connection.password", password);
            configuration.setProperty("dialect", "org.hibernate.dialect.MySQL55Dialect");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("show_sql", "true");
            configuration.setProperty("format_sql", "true");
            configuration.addAnnotatedClass(ua.com.alevel.model.entity.Type.class);
            configuration.addAnnotatedClass(ua.com.alevel.model.entity.Account.class);
            configuration.addAnnotatedClass(ua.com.alevel.model.entity.User.class);
            configuration.addAnnotatedClass(ua.com.alevel.model.entity.Category.class);
            configuration.addAnnotatedClass(ua.com.alevel.model.entity.Operation.class);
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
            return sessionFactory;
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void closeSessionFactory() {
        if (!sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}

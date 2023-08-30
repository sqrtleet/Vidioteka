package org.example.util;

import org.example.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static SessionFactory sessionFactory;


    public static <T> SessionFactory getSessionFactory(Class<T> entity) {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(entity);
        configuration.configure();

        try {
            sessionFactory = configuration.buildSessionFactory();
        }
        catch (Exception e) {
            System.out.println("Исключение! " + e);
        }

        return sessionFactory;
    }

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(AuthDataEntity.class);
        configuration.addAnnotatedClass(CdEntity.class);
        configuration.addAnnotatedClass(ClientEntity.class);
        configuration.addAnnotatedClass(EmployeeEntity.class);
        configuration.addAnnotatedClass(FilmEntity.class);
        configuration.addAnnotatedClass(GenreEntity.class);
        configuration.addAnnotatedClass(OrderEntity.class);
        configuration.addAnnotatedClass(ProducerEntity.class);
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}

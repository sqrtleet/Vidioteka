package org.example.functions;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.*;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collections;
import java.util.List;

public class AuthDataFunc {
    public static void addData(String login, String password){
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            AuthDataEntity data = AuthDataEntity.builder()
                    .login(login)
                    .password(password)
                    .build();
            session.persist(data);
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static void editData(int id, String login, String password) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            AuthDataEntity data = session.get(AuthDataEntity.class, id);
            data.setLogin(login);
            data.setPassword(password);
            session.merge(data);
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static AuthDataEntity getDataById(int id) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            AuthDataEntity data = session.get(AuthDataEntity.class, id);
            session.getTransaction().commit();
            session.clear();
            return data;
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
            return null;
        }
    }
    public static AuthDataEntity getDataByLogin(String login) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<AuthDataEntity> criteriaQuery = criteriaBuilder.createQuery(AuthDataEntity.class);
            Root<AuthDataEntity> root = criteriaQuery.from(AuthDataEntity.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("login"), login));
            AuthDataEntity data = session.createQuery(criteriaQuery).uniqueResult();
            session.getTransaction().commit();
            session.close();
            return data;
        } catch (Exception e) {
            System.out.println("Exception! " + e);
            return null;
        }
    }
    public static List<AuthDataEntity> getAllData() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<AuthDataEntity> criteria = builder.createQuery(AuthDataEntity.class);
                Root<AuthDataEntity> root = criteria.from(AuthDataEntity.class);
                criteria.select(root);
                List<AuthDataEntity> datas = session.createQuery(criteria).getResultList();
                session.getTransaction().commit();
                session.close();
                return datas;
            }
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return Collections.emptyList();
        }
    }
    public static void deleteData(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            AuthDataEntity data = session.get(AuthDataEntity.class, id);
            if (data != null) {
                session.remove(data);
            }
            session.getTransaction().commit();
            session.clear();
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
        }
    }
}

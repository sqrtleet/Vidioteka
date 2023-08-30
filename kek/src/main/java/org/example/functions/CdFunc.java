package org.example.functions;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.*;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class CdFunc {
    public static void addCd(String name, int price, List<FilmEntity> films){
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CdEntity cd = CdEntity.builder()
                    .films(films)
                    .name(name)
                    .price(price)
                    .build();
            session.persist(cd);
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static void editCd(int id, String name, int price, List<FilmEntity> films) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CdEntity cd = session.get(CdEntity.class, id);
            cd.setName(name);
            cd.setPrice(price);
            cd.setFilms(films);
            session.merge(cd);
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static CdEntity getCdById(int id) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CdEntity cd = session.get(CdEntity.class, id);
            session.getTransaction().commit();
            session.clear();
            return cd;
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
            return null;
        }
    }
    public static CdEntity getCdByName(String name) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<CdEntity> query = builder.createQuery(CdEntity.class);
            Root<CdEntity> root = query.from(CdEntity.class);
            query.select(root).where(builder.equal(root.get("name"), name));
            List<CdEntity> results = session.createQuery(query).getResultList();
            session.getTransaction().commit();
            session.clear();
            return results.isEmpty() ? null : results.get(0);
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
            return null;
        }
    }
    public static List<CdEntity> getAllCds() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<CdEntity> criteria = builder.createQuery(CdEntity.class);
                Root<CdEntity> root = criteria.from(CdEntity.class);
                criteria.select(root);
                List<CdEntity> cds = session.createQuery(criteria).getResultList();
                session.getTransaction().commit();
                session.close();
                return cds;
            }
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return Collections.emptyList();
        }
    }
    public static void deleteCd(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CdEntity cd = session.get(CdEntity.class, id);
            if (cd != null) {
                session.remove(cd);
            }
            session.getTransaction().commit();
            session.clear();
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
        }
    }

}

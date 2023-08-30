package org.example.functions;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.CdEntity;
import org.example.entity.ClientEntity;
import org.example.entity.GenreEntity;
import org.example.entity.ProducerEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collections;
import java.util.List;

public class ProducerFunc {
    public static void addProducer(String name, String bio){
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            ProducerEntity producer = ProducerEntity.builder()
                    .name(name)
                    .biographyData(bio)
                    .build();
            session.persist(producer);
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static void editProducer(int id, String name, String bio) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            ProducerEntity producer = session.get(ProducerEntity.class, id);
            producer.setName(name);
            producer.setBiographyData(bio);
            session.merge(producer);
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static ProducerEntity getProducerById(int id) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            ProducerEntity producer = session.get(ProducerEntity.class, id);
            session.getTransaction().commit();
            session.clear();
            return producer;
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
            return null;
        }
    }
    public static ProducerEntity getProducerByName(String name) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ProducerEntity> criteriaQuery = criteriaBuilder.createQuery(ProducerEntity.class);
            Root<ProducerEntity> root = criteriaQuery.from(ProducerEntity.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
            ProducerEntity producer = session.createQuery(criteriaQuery).uniqueResult();
            session.getTransaction().commit();
            session.close();
            return producer;
        } catch (Exception e) {
            System.out.println("Exception! " + e);
            return null;
        }
    }
    public static List<ProducerEntity> getAllProducers() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<ProducerEntity> criteria = builder.createQuery(ProducerEntity.class);
                Root<ProducerEntity> root = criteria.from(ProducerEntity.class);
                criteria.select(root);
                List<ProducerEntity> producers = session.createQuery(criteria).getResultList();
                session.getTransaction().commit();
                session.close();
                return producers;
            }
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return Collections.emptyList();
        }
    }
    public static void deleteProducer(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            ProducerEntity producer = session.get(ProducerEntity.class, id);
            if (producer != null) {
                session.remove(producer);
            }
            session.getTransaction().commit();
            session.clear();
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
        }
    }
}

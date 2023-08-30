package org.example.functions;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.CdEntity;
import org.example.entity.FilmEntity;
import org.example.entity.GenreEntity;
import org.example.entity.ProducerEntity;
import org.example.entity.enums.AgeRating;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class GenreFunc {
    public static void addGenre(String name){
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            GenreEntity genre = GenreEntity.builder()
                    .name(name)
                    .build();
            session.persist(genre);
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static void editGenre(int id, String name) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            GenreEntity genre = session.get(GenreEntity.class, id);
            genre.setName(name);
            session.merge(genre);
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static GenreEntity getGenreById(int id) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            GenreEntity genre = session.get(GenreEntity.class, id);
            session.getTransaction().commit();
            session.clear();
            return genre;
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
            return null;
        }
    }
    public static GenreEntity getGenreByName(String name) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GenreEntity> criteriaQuery = criteriaBuilder.createQuery(GenreEntity.class);
            Root<GenreEntity> root = criteriaQuery.from(GenreEntity.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
            GenreEntity genre = session.createQuery(criteriaQuery).uniqueResult();
            session.getTransaction().commit();
            session.close();
            return genre;
        } catch (Exception e) {
            System.out.println("Exception! " + e);
            return null;
        }
    }
    public static List<GenreEntity> getAllGenres() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<GenreEntity> criteria = builder.createQuery(GenreEntity.class);
                Root<GenreEntity> root = criteria.from(GenreEntity.class);
                criteria.select(root);
                List<GenreEntity> genres = session.createQuery(criteria).getResultList();
                session.getTransaction().commit();
                session.close();
                return genres;
            }
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return Collections.emptyList();
        }
    }
    public static void deleteGenre(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            GenreEntity genre = session.get(GenreEntity.class, id);
            if (genre != null) {
                session.remove(genre);
            }
            session.getTransaction().commit();
            session.clear();
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
        }
    }
}

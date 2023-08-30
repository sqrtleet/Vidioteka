package org.example.functions;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.*;
import org.example.entity.enums.AgeRating;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilmFunc {
    public static void addFilm(String name, LocalDate releaseDate, String country, AgeRating ageRating, GenreEntity genre, ProducerEntity producer){
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            FilmEntity film = FilmEntity.builder()
                    .genre(genre)
                    .producer(producer)
                    .name(name)
                    .releaseDate(releaseDate)
                    .country(country)
                    .ageRating(ageRating)
                    .build();
            session.persist(film);
//            List<CdEntity> cdsCopy = new ArrayList<>(cds);
//            for (CdEntity cd : cdsCopy){
//                cd.addFilm(film);
//                session.merge(cd);
//            }
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static void editFilm(int id, String name, LocalDate releaseDate, String country, AgeRating ageRating, GenreEntity genre, ProducerEntity producer) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            FilmEntity film = session.get(FilmEntity.class, id);
            film.setName(name);
            film.setReleaseDate(releaseDate);
            film.setCountry(country);
            film.setAgeRating(ageRating);
            film.setGenre(genre);
            film.setProducer(producer);
            session.merge(film);
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static FilmEntity getFilmById(int id) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            FilmEntity film = session.get(FilmEntity.class, id);
            session.getTransaction().commit();
            session.clear();
            return film;
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
            return null;
        }
    }
    public static FilmEntity getFilmByName(String name) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<FilmEntity> criteriaQuery = criteriaBuilder.createQuery(FilmEntity.class);
            Root<FilmEntity> root = criteriaQuery.from(FilmEntity.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
            FilmEntity film = session.createQuery(criteriaQuery).uniqueResult();
            session.getTransaction().commit();
            session.close();
            return film;
        } catch (Exception e) {
            System.out.println("Exception! " + e);
            return null;
        }
    }
    public static List<FilmEntity> getAllFilms() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<FilmEntity> criteria = builder.createQuery(FilmEntity.class);
                Root<FilmEntity> root = criteria.from(FilmEntity.class);
                criteria.select(root);
                List<FilmEntity> list = session.createQuery(criteria).getResultList();
                session.getTransaction().commit();
                session.close();
                return list;
            }
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return Collections.emptyList();
        }
    }
    public static void deleteFilm(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            FilmEntity film = session.get(FilmEntity.class, id);
            if (film != null) {
                session.remove(film);
            }
            session.getTransaction().commit();
            session.clear();
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
        }
    }
}

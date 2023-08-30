package org.example.functions;

import jakarta.persistence.criteria.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.entity.*;
import org.example.util.HibernateUtil;
import org.hibernate.Cache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


public class EmployeeFunc {
    // Добавление работника
    public static void addEmployee(String name, String phone_number, AuthDataEntity authData) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            authData = (AuthDataEntity) session.merge(authData);
            EmployeeEntity employee = EmployeeEntity.builder()
                    .name(name)
                    .phoneNumber(phone_number)
                    .authData(authData)
                    .build();
            CriteriaQuery<EmployeeEntity> criteriaQuery2 = builder.createQuery(EmployeeEntity.class);
            Root<EmployeeEntity> root2 = criteriaQuery2.from(EmployeeEntity.class);
            Predicate predicate2 = builder.or(
                    builder.equal(root2.get("name"), employee.getName()),
                    builder.equal(root2.get("phoneNumber"), employee.getPhoneNumber())
            );
            criteriaQuery2.where(predicate2);
            List<EmployeeEntity> results2 = session.createQuery(criteriaQuery2).getResultList();
            if (results2.isEmpty()) {
                session.persist(employee);
            }
            session.getTransaction().commit();
            session.clear();
            session.close();
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
        }
    }
    public static void editEmployee(int id, String name, String phone_number, AuthDataEntity authData) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            EmployeeEntity employee = session.get(EmployeeEntity.class, id);
            authData = (AuthDataEntity) session.merge(authData);
            employee.setAuthData(authData);
            employee.setName(name);
            employee.setPhoneNumber(phone_number);
            session.merge(employee);
            session.getTransaction().commit();
        } catch (Exception e){
            System.out.println("Исключение! " + e);
        }
    }

    // Получить работника по id
    public static EmployeeEntity getEmployeeById(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                EmployeeEntity employee = session.get(EmployeeEntity.class, id);
                session.getTransaction().commit();
                session.close();
                return employee;
            }
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return null;
        }
    }
    // Получить список всех работников
    public static List<EmployeeEntity> getAllEmployees() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<EmployeeEntity> criteria = builder.createQuery(EmployeeEntity.class);
                Root<EmployeeEntity> root = criteria.from(EmployeeEntity.class);
                root.fetch("authData", JoinType.LEFT).fetch("employees", JoinType.LEFT);
                criteria.select(root);
                List<EmployeeEntity> list = session.createQuery(criteria).getResultList();
                session.getTransaction().commit();
                session.close();
                return list;
            }
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return Collections.emptyList();
        }
    }
    // Проверка авторизации
    public static boolean checkAuth(String name, String login, String password) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try(Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                // Находим объект AuthData по логину и паролю
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<EmployeeEntity> criteriaQuery = builder.createQuery(EmployeeEntity.class);
                Root<EmployeeEntity> root = criteriaQuery.from(EmployeeEntity.class);
                Join<EmployeeEntity, AuthDataEntity> authDataJoin = root.join("authData");
                criteriaQuery.select(root).where(builder.and(
                        builder.equal(authDataJoin.get("login"), login),
                        builder.equal(authDataJoin.get("password"), password)
                ));
                List<EmployeeEntity> employees = session.createQuery(criteriaQuery).getResultList();
                EmployeeEntity foundEmployee = employees.stream()
                        .filter(e -> name.equals(e.getName()))
                        .findFirst()
                        .orElse(null);

                if (foundEmployee == null) {
                    return false;
                }

                session.getTransaction().commit();
                session.clear();
                session.close();
                return true;
            }

        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return false;
        }
    }
    public static EmployeeEntity getEmployeeByName(String name){
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<EmployeeEntity> criteriaQuery = criteriaBuilder.createQuery(EmployeeEntity.class);
            Root<EmployeeEntity> root = criteriaQuery.from(EmployeeEntity.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
            EmployeeEntity employee = session.createQuery(criteriaQuery).uniqueResult();
            session.getTransaction().commit();
            session.close();
            return employee;
        } catch (Exception e) {
            System.out.println("Exception! " + e);
            return null;
        }
    }

    public static void deleteEmployee(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            EmployeeEntity employee = session.get(EmployeeEntity.class, id);
            if (employee != null) {
                session.remove(employee);
            }
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }




}

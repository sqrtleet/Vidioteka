package org.example.functions;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.*;
import org.example.entity.enums.CollateralType;
import org.example.entity.enums.OrderStatus;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderFunc {
    // === сохранение заказа ===
    public static void saveOrder(List<CdEntity> cds, EmployeeEntity employee, ClientEntity client, LocalDate date, LocalDate returnDate, OrderStatus status, CollateralType collateralData, String collateralValue) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            int sum = 0;
            for (CdEntity cd : cds){
                sum += cd.getPrice();
            }
            OrderEntity order = OrderEntity.builder()
                    .cdList(cds)
                    .employee(employee)
                    .client(client)
                    .sum(sum)
                    .date(date)
                    .returnDate(returnDate)
                    .status(status)
                    .collateralData(collateralData)
                    .collateralDataValue(collateralValue)
                    .cdList(cds)
                    .build();
            session.persist(order);
            List<CdEntity> cdsCopy = new ArrayList<>(cds);
            for (CdEntity cd2 : cdsCopy){
                cd2.addOrder(order);
                session.merge(cd2);
            }
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    public static void editOrder(int orderId, List<CdEntity> cds, EmployeeEntity employee, ClientEntity client, LocalDate date, LocalDate returnDate, OrderStatus status, CollateralType collateralData, String collateralValue) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            OrderEntity order = session.get(OrderEntity.class, orderId);
            List<CdEntity> a = order.getCdList();
            for (CdEntity q : a) {
                q.setOrder(null);
                session.merge(q);
            }

            int sum = 0;
            for (CdEntity cd : cds){
                sum += cd.getPrice();
            }
            order.setEmployee(employee);
            order.setClient(client);
            order.setSum(sum);
            order.setDate(date);
            order.setReturnDate(returnDate);
            order.setStatus(status);
            order.setCollateralData(collateralData);
            order.setCollateralDataValue(collateralValue);
            order.setCdList(cds);
            session.persist(order);
            List<CdEntity> cdsCopy = new ArrayList<>(cds);
            for (CdEntity cd2 : cdsCopy){
                cd2.addOrder(order);
                session.merge(cd2);
            }
            session.getTransaction().commit();
            session.clear();
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
    // === получить все заказы клиента ===
    public static List<OrderEntity> getOrdersByClientId(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<OrderEntity> criteria = builder.createQuery(OrderEntity.class);
            Root<OrderEntity> root = criteria.from(OrderEntity.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get("client"), ClientFunc.getClientById(id)));
            List<OrderEntity> orders = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            session.clear();
            return orders;
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return Collections.emptyList();
        }
    }
    public static List<OrderEntity> getOrdersByEmployeeId(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<OrderEntity> criteria = builder.createQuery(OrderEntity.class);
            Root<OrderEntity> root = criteria.from(OrderEntity.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get("employee"), EmployeeFunc.getEmployeeById(id)));
            List<OrderEntity> orders = session.createQuery(criteria).getResultList();
            session.getTransaction().commit();
            session.clear();
            return orders;
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return Collections.emptyList();
        }
    }
    public static List<OrderEntity> getAllOrders() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<OrderEntity> criteria = builder.createQuery(OrderEntity.class);
                Root<OrderEntity> root = criteria.from(OrderEntity.class);
                criteria.select(root);
                List<OrderEntity> list = session.createQuery(criteria).getResultList();
                session.getTransaction().commit();
                session.close();
                return list;
            }
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
            return Collections.emptyList();
        }
    }
    public static void deleteOrder(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            OrderEntity order = session.get(OrderEntity.class, id);
            if (order != null) {
                session.remove(order);
            }
            session.getTransaction().commit();
            session.clear();
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
        }
    }

}

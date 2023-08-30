package org.example.functions;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.entity.*;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;


public class ClientFunc {
    // === поиск клиента по id ===
    public static ClientEntity getClientById(int id) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            ClientEntity client = session.get(ClientEntity.class, id);
            session.getTransaction().commit();
            session.clear();
            return client;
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
            return null;
        }
    }
    // === поиск клиента по name ===
    public static ClientEntity getClientByName(String name) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ClientEntity> criteriaQuery = criteriaBuilder.createQuery(ClientEntity.class);
            Root<ClientEntity> root = criteriaQuery.from(ClientEntity.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
            ClientEntity client = session.createQuery(criteriaQuery).uniqueResult();
            session.getTransaction().commit();
            session.close();
            return client;
        } catch (Exception e) {
            System.out.println("Exception! " + e);
            return null;
        }
    }

    // === список всех клиентов ===
    public static List<ClientEntity> getAllClients() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ClientEntity> criteriaQuery = criteriaBuilder.createQuery(ClientEntity.class);
            Root<ClientEntity> root = criteriaQuery.from(ClientEntity.class);
            criteriaQuery.select(root);
            List<ClientEntity> clients = session.createQuery(criteriaQuery).getResultList();
            session.getTransaction().commit();
            session.close();
            return clients;
        } catch (Exception e) {
            System.out.println("Exception! " + e);
            return null;
        }
    }
    // === добавление клиента ===
    public static void saveClient(String name, String phone_number, boolean blacklist, LocalDate date) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            ClientEntity client = ClientEntity.builder()
                    .name(name)
                    .phoneNumber(phone_number)
                    .blacklist(blacklist)
                    .dateOfBirth(date)
                    .build();
            session.persist(client);
            session.getTransaction().commit();
            session.clear();
        }
    }

    // === обновление клиента ===
    public static void updateClient(Integer id, String name, String phone_number, boolean blacklist, LocalDate date) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            ClientEntity client = session.get(ClientEntity.class, id);
            client.setName(name);
            client.setPhoneNumber(phone_number);
            client.setBlacklist(blacklist);
            client.setDateOfBirth(date);
            session.getTransaction().commit();
            session.clear();
        }
    }

    // === удаление клиента ===
    public static void deleteClient(int id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            ClientEntity client = session.get(ClientEntity.class, id);
            if (client != null) {
                session.remove(client);
            }
            session.getTransaction().commit();
            session.clear();
        }
        catch (Exception e){
            System.out.println("Исключение! " + e);
        }
    }
    public static void generateReport() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ClientEntity> criteriaQuery = criteriaBuilder.createQuery(ClientEntity.class);
            Root<ClientEntity> root = criteriaQuery.from(ClientEntity.class);
            criteriaQuery.select(root);
            List<ClientEntity> clients = session.createQuery(criteriaQuery).getResultList();

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Отчет по клиентам");

            XSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID клиента");
            headerRow.createCell(1).setCellValue("Имя клиента");
            headerRow.createCell(2).setCellValue("Номер телефона клиента");
            headerRow.createCell(3).setCellValue("ID заказа");
            headerRow.createCell(4).setCellValue("Цена диска");

            int rowIndex = 1;
            for (ClientEntity client : clients) {
                for (OrderEntity order : client.getOrderList()) {
                    XSSFRow row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(client.getId());
                    row.createCell(1).setCellValue(client.getName());
                    row.createCell(2).setCellValue(client.getPhoneNumber());
                    row.createCell(3).setCellValue(order.getId());
                    int sum = 0;
                    for(CdEntity cd : order.getCdList()){
                        sum += cd.getPrice();
                    }
                    row.createCell(4).setCellValue(sum);
                }
            }

            FileOutputStream fileOut = new FileOutputStream("report.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            session.getTransaction().commit();
            session.close();

            System.out.println("Отчет успешно создан!");
        } catch (Exception e) {
            System.out.println("Исключение! " + e);
        }
    }
}

package org.example;

import jakarta.persistence.criteria.Order;
import org.example.entity.*;
import org.example.entity.enums.*;
import org.example.functions.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
//        EmployeeEntity employee = EmployeeFunc.getEmployeeByName("Скрыбыкин Айсен");
//        System.out.println(employee.getName());
//        ClientEntity client = ClientFunc.getClientById(3);
//        OrderFunc.saveOrder(employee, client, 1000, LocalDate.of(2023,3,22), LocalDate.of(2023,4,22), OrderStatus.NEW, CollateralType.MONEY, "1000");
//        List<OrderEntity> arr = OrderFunc.getOrdersByClientId(2);
//        for(OrderEntity e : arr){
//            System.out.println(e.getId() + " " + e.getSum() + " " + e.getStatus().getDisplayName());
//        }
//        List<OrderEntity> list = OrderFunc.getOrdersByClientId(2);
//        for(OrderEntity e : list){
//            System.out.println(e.getId() + " " + e.getSum());
//        }
        //ClientFunc.saveClient("Oyzen", "89647428322", true, LocalDate.of(2001, 3, 29));
//        EmployeeFunc.addEmployee("Васильев Александр", "89142249872");
//        List<OrderEntity> orders = OrderFunc.getAllOrders();
//        List<OrderEntity> orders2 = OrderFunc.getOrdersByEmployeeId(1);
//        for(OrderEntity e : orders){
//            System.out.println(e.getId());
//        }
//        for(OrderEntity e : orders2){
//            System.out.println(e.getId());
//        }
//        CdFunc.addCd("Сборник 1", 1500);
//        List<CdEntity> cds = CdFunc.getAllCds();
//        for(CdEntity cd : cds){
//            System.out.println(cd.getName());
//        }
//        cds.add(CdFunc.getCdById(1));
//        cds.add(CdFunc.getCdById(2));
//        cds.add(CdFunc.getCdById(3));
        //FilmFunc.addFilm("Kekw", LocalDate.of(2001,10,5), "Russia", "18+", cds, GenreFunc.getGenreById(1), ProducerFunc.getProducerById(1));
        //OrderFunc.saveOrder(cds, EmployeeFunc.getEmployeeById(1), ClientFunc.getClientById(1), LocalDate.of(2023,3,27), LocalDate.of(2023,4,27), OrderStatus.NEW, CollateralType.MONEY, "1000");
        //OrderFunc.editOrder(1,cds, EmployeeFunc.getEmployeeById(1), ClientFunc.getClientById(1), LocalDate.of(2023,3,27), LocalDate.of(2023,4,27), OrderStatus.NEW, CollateralType.MONEY, "1000");
    }
}
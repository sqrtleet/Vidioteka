package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cd", schema = "app2")
public class CdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "film_cd", joinColumns = @JoinColumn(name = "cd_id"), inverseJoinColumns = @JoinColumn(name = "film_id"))
    private List<FilmEntity> films = new ArrayList<>();
    public void addFilm(FilmEntity film){
        films.add(film);
        film.getCds().add(this);
    }
    public void addOrder(OrderEntity order){
        this.order = order;
        order.getCdList().add(this);
    }

}

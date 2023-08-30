package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.AgeRating;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "film", schema = "app2")
public class FilmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "release_date")
    private LocalDate releaseDate;
    @Column(name = "country")
    private String country;
    @Enumerated(EnumType.STRING)
    @Column(name = "age_rating")
    private AgeRating ageRating;
    @ManyToOne
    @JoinColumn(name = "producer_id")
    private ProducerEntity producer;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;
    @ManyToMany(mappedBy = "films")
    private List<CdEntity> cds = new ArrayList<>();
}

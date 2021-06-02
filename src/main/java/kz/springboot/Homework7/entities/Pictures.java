package kz.springboot.Homework7.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity

@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "pictures")
public class Pictures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate=new Date();

    @ManyToOne
    private Items items;

}

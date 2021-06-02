package kz.springboot.Homework7.repositories;

import kz.springboot.Homework7.entities.Items;
import kz.springboot.Homework7.entities.Pictures;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Pictures,Long> {
    List<Pictures> findAllByItems(Items item);
}

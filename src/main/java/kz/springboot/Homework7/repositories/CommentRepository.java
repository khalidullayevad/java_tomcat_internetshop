package kz.springboot.Homework7.repositories;

import kz.springboot.Homework7.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments,Long> {
    List<Comments> findAllByItemsIdOrderByAddedDateDesc(Long id);

}

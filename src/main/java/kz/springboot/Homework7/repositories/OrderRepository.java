package kz.springboot.Homework7.repositories;

import kz.springboot.Homework7.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}

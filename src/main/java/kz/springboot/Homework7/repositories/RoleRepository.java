package kz.springboot.Homework7.repositories;

import kz.springboot.Homework7.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoleRepository  extends JpaRepository<Roles, Long> {
    Roles findByName(String name);
}

package kz.springboot.Homework7.services;

import kz.springboot.Homework7.entities.Roles;
import kz.springboot.Homework7.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
   Users getUserByEmail(String email);
   Roles getRoleByName(String role);
   Users addUser(Users user);
   List<Users> getAllUsers();
   Users getUser(Long id);
   void deleteUser(Users users);
   Users saveUser(Users users);
   Users saveUserRole(Users users);
   Users saveUserNewPass(Users users, String oldpass, String newpass);
   Roles addRole(Roles roles);
   List<Roles> getAllRoles();
   Roles getRole(Long id);
   void deleteRole(Roles roles);
   Roles saveRole(Roles roles);

}

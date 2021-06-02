package kz.springboot.Homework7.services.impl;

import kz.springboot.Homework7.entities.Roles;
import kz.springboot.Homework7.entities.Users;
import kz.springboot.Homework7.repositories.RoleRepository;
import kz.springboot.Homework7.repositories.UserRepository;
import kz.springboot.Homework7.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users myUser = userRepository.findByEmail(s);
        if(myUser != null){
            User secUser = new User(myUser.getEmail(),myUser.getPassword(), myUser.getRoles());
            return secUser;
        }
        throw new UsernameNotFoundException("User mot found");
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Roles getRoleByName(String role) {
        return roleRepository.findByName(role);
    }

    @Override
    public Users addUser(Users user) {
        Users checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser == null) {
            Roles role = roleRepository.findByName("ROLE_USER");
            if (role != null) {
                ArrayList<Roles> roles = new ArrayList<>();
                roles.add(role);
                user.setRoles(roles);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                return userRepository.save(user);
            }
        }
        return null;

    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users getUser(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public void deleteUser(Users users) {
        userRepository.delete(users);
    }


    @Override
    public Users saveUser(Users users) {

            Roles role = roleRepository.findByName("ROLE_USER");
            if (role != null) {
                int i = 0;
                if (users.getRoles() == null) {
                    ArrayList<Roles> roles = new ArrayList<>();
                    roles.add(role);
                    users.setRoles(roles);
                }

                if (users.getRoles() != null) {
                    for (Roles r : users.getRoles()) {
                        if (r.getName().equals("ROLE_USER")) {
                            i++;
                        }
                    }
                }
                if (i == 0) {
                    List<Roles> usersRoles = users.getRoles();
                    usersRoles.add(roleRepository.findByName("ROLE_USER"));
                    users.setRoles(usersRoles);

                }
                users.setPassword(passwordEncoder.encode(users.getPassword()));
                return userRepository.save(users);
            }

        return null;

    }


    @Override
    public Users saveUserRole(Users users) {
        Roles role = roleRepository.findByName("ROLE_USER");
        if (role != null) {
            int i = 0;
            if (users.getRoles() == null) {
                ArrayList<Roles> roles = new ArrayList<>();
                roles.add(role);
                users.setRoles(roles);
            }

            if (users.getRoles() != null) {
                for (Roles r : users.getRoles()) {
                    if (r.getName().equals("ROLE_USER")) {
                        i++;
                    }
                }
            }
            if (i == 0) {
                List<Roles> usersRoles = users.getRoles();
                usersRoles.add(roleRepository.findByName("ROLE_USER"));
                users.setRoles(usersRoles);

            }
            return userRepository.save(users);
        }
        return null;

    }

    @Override
    public Users saveUserNewPass(Users users, String pass, String newpass) {
            System.out.println(pass);
            System.out.println(passwordEncoder.encode(users.getPassword()));
            System.out.println(passwordEncoder.encode(pass));
        if(passwordEncoder.matches(pass,users.getPassword()))
        {
                users.setPassword(passwordEncoder.encode(newpass));
                return userRepository.save(users);
            }

        return null;
    }

    @Override
    public Roles addRole(Roles roles) {
        return roleRepository.save(roles);
    }

    @Override
    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Roles getRole(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public void deleteRole(Roles roles) {
        roleRepository.delete(roles);
    }

    @Override
    public Roles saveRole(Roles roles) {
        return roleRepository.save(roles);
    }


}

package ru.kata.spring.boot_security.demo.util;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collections;


@Component
public class UtilStart {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilStart(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void initAdminAndRoles() {


        if (roleRepository.findByName("ROLE_USER") == null) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
            System.out.println("✅ РОЛЬ 'ROLE_USER' СОЗДАНА!");
        }

        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
            System.out.println("✅ РОЛЬ 'ROLE_ADMIN' СОЗДАНА!");
        }

        // Создаём админа если его нет
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setName("Администратор");
            admin.setEmail("admin@mail.ru");
            admin.setAge(35);

            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            admin.setRoles(Collections.singleton(adminRole));

            userRepository.save(admin);
            System.out.println("АДМИН 'admin/admin' СОЗДАН В MySQL!");
        }


        if (userRepository.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setName("Пользователь");
            user.setEmail("user@mail.ru");
            user.setAge(25);

            Role userRole = roleRepository.findByName("ROLE_USER");
            user.setRoles(Collections.singleton(userRole));

            userRepository.save(user);
            System.out.println("ЮЗЕР 'user/user' СОЗДАН В MySQL!");
        }
    }
}

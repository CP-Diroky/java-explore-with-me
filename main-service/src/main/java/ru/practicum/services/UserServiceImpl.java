package ru.practicum.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.models.User;
import ru.practicum.repositories.UserRepository;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    @Override
    public User addUser(User user) {
        String[] emailParts = user.getEmail().split("@");

        if (emailParts[0].length() > 64)
            throw new BadRequestException("Email local-part size should not be greater than 64");
        else if (!userRepository.findByEmail(user.getEmail()).isEmpty()) {
            throw new ConflictException("This email already exists");
        }
        String[] domains = emailParts[1].split("\\.");

        for (int i = 0; i < domains.length; i++) {
            if (domains[i].length() > 63)
                throw new BadRequestException("Email domain size should not be greater than 64");
        }

        return userRepository.save(user);
    }

    @Override
    public Collection<User> getUsers(Collection<Long> ids, int size, int from) {
        if (ids == null || ids.isEmpty()) {
            return userRepository.getUsers(size, from);
        }
        return userRepository.getUsers(ids, size, from);
    }


    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (userRepository.findById(id).isEmpty()) throw new NotFoundException("User with id=" + id + " was not found.");
        userRepository.deleteById(id);
    }
}

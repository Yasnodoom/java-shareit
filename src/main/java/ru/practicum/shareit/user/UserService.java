package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.user.mapper.UserMapper.mapToUserDto;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<UserDto> getAll() {
        return repository
                .findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public Optional<UserDto> get(Long userId) {
        return repository.findById(userId).map(UserMapper::mapToUserDto);
    }

    public UserDto create(User user) {
        validate(user);
        validateIsEmailUnique(user);

        return mapToUserDto(repository.save(user));
    }

    public UserDto update(User newUser, Long userId) {
        User existUser = findUserById(userId);

        if (newUser.getEmail() != null && !existUser.getEmail().equals(newUser.getEmail())) {
            validateIsEmailUnique(newUser);
            existUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null && !existUser.getName().equals(newUser.getName())) {
            existUser.setName(newUser.getName());
        }

        return mapToUserDto(repository.save(existUser));
    }

    public void delete(Long userId) {
        repository.deleteById(userId);
    }

    public User findUserById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    private void validate(User user) {
        if (user.getEmail() == null)
            throw new ValidationException("mail is empty");
        if (user.getEmail().isEmpty() || user.getEmail().isBlank())
            throw new ValidationException("mail is empty");
        if (!user.getEmail().contains("@"))
            throw new ValidationException("mail must contains @");
        if (user.getName() == null)
            throw new ValidationException("login incorrect");
        if (user.getName().isEmpty() || user.getName().isBlank())
            throw new ValidationException("login is empty");
    }

    private void validateIsEmailUnique(User user) {
        if (repository
                .findAll()
                .stream()
                .map(User::getEmail)
                .anyMatch(el -> el.equals(user.getEmail()))) {
            throw new DuplicateEmailException();
        }
    }
}

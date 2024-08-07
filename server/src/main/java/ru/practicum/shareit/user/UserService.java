package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.user.mapper.UserMapper.mapToUserDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;

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
        return repository
                .findById(userId)
                .map(UserMapper::mapToUserDto);
    }

    public UserDto create(UserDto dto) {
        validateIsEmailUnique(dto);

        return mapToUserDto(repository.save(toUser(dto)));
    }

    public UserDto update(UserDto newUser, Long userId) {
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

    private void validateIsEmailUnique(UserDto user) {
        repository
                .findAll()
                .stream()
                .map(User::getEmail)
                .filter(el -> el.equals(user.getEmail()))
                .findAny()
                .ifPresent(s -> {
                    throw new DuplicateEmailException();
                });
    }

}

package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.request.ItemRequestMapper.toDto;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserService userService;
    private final ItemService itemService;

    public ItemRequestDto create(ItemRequestDto requestDto, Long requesterId) {
        ItemRequest request = ItemRequestMapper.toRequest(requestDto);
        request.setRequester(userService.findUserById(requesterId));
        request.setCreated(LocalDateTime.now());
        return toDto(repository.save(request));
    }

    public ItemRequestDto getOne(Long id) {
        ItemRequestDto dto = toDto(findItemRequestById(id));
        dto.setItemList(itemService.findItemsByRequestId(id));
        return dto;
    }

    public List<ItemRequestDto> findPersonal(Long id) {
        return repository.findAllByRequesterId(id)
                .stream()
                .map(ItemRequestMapper::toDto)
                .peek(item -> item.setItemList(itemService.findItemsByRequestId(item.getId())))
                .toList();
    }

    public List<ItemRequestDto> findAll(Integer from, Integer size) {
        return repository
                .findAll(PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created")))
                .stream()
                .map(ItemRequestMapper::toDto)
                .toList();
    }

    public ItemRequest findItemRequestById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

}

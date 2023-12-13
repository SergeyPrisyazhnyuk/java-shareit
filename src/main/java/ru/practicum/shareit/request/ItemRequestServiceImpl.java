package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoReturn;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemRequestDtoReturn save(Long userId, ItemRequestDto itemRequestDto) {

        User user = userRequestValidation(userId);

        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(user);

        itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDtoReturn(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDtoReturn> getRequestByUserId(Long userId) {

        userRequestValidation(userId);

        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestorId(userId);

        return itemRequestList.stream().map(ItemRequestMapper::toItemRequestDtoReturn).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDtoReturn> getAllRequestsFromToSize(Long userId, Integer from, Integer size) {
        User user = userRequestValidation(userId);

        Pageable pageable = PageRequest.of(from, size);

        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequestorIdNotOrderByCreated(userId, pageable);

        return itemRequestList.stream().map(ItemRequestMapper::toItemRequestDtoReturn).collect(Collectors.toList());

    }

    @Override
    public ItemRequestDtoReturn getRequestById(Long userId, Long requestId) {

        userRequestValidation(userId);

        Optional<ItemRequest> requestById = itemRequestRepository.findById(requestId);

        if (requestById.isEmpty()) {
            throw new NotFoundException("Request with id " + requestId + " was not found");
        }

        return ItemRequestMapper.toItemRequestDtoReturn(requestById.get());

    }

    private User userRequestValidation(Long userId) {

        Optional<User> userO = userRepository.findById(userId);

        if (userO == null || userO.isEmpty()) {
            throw new NotFoundException("Не найден юзер с id: " + userId);
        }

        return userO.get();
    }
}

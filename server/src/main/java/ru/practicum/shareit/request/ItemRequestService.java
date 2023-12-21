package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoReturn;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoReturn save(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDtoReturn> getRequestByUserId(Long userID);

    List<ItemRequestDtoReturn> getAllRequestsFromToSize(Long userId, Integer from, Integer size);

    ItemRequestDtoReturn getRequestById(Long userId, Long requestId);

}

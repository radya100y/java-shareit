package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceJpa implements ItemRequestService {

    @Autowired
    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final ItemService itemService;


    @Override
    @Transactional
    public ItemRequestDtoOut save(ItemRequestDtoIn ir) {
        ItemRequest savedItemRequest = itemRequestRepository.save(
                ItemRequestMapper.toItemRequestFromIn(ir, userService.getModel(ir.getUserId())));

        return ItemRequestMapper.toItemRequestOut(savedItemRequest,
                itemService.getItemsForRequest(savedItemRequest.getId()));
    }

    @Override
    public List<ItemRequestDtoOut> getAllByAuthor(Long userId) {
        userService.get(userId);

        return itemRequestRepository.findAllByAuthor_IdOrderByIdDesc(userId).stream()
                .map(x -> ItemRequestMapper.toItemRequestOut(x, itemService.getItemsForRequest(x.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDtoOut getById(Long itemRequestId, Long userId) {
        userService.get(userId);

        ItemRequest ir = itemRequestRepository.findById(itemRequestId).orElseThrow(() ->
                new NotFoundException("Запрос с идентификатором " + itemRequestId + " не найден"));
        return ItemRequestMapper.toItemRequestOut(ir, itemService.getItemsForRequest(ir.getId()));
    }

    @Override
    public List<ItemRequestDtoOut> getAll(Long userId, Pageable pageable) {
        return itemRequestRepository.findAllByAuthor_IdNot(userId, pageable).stream()
                .map(x -> ItemRequestMapper.toItemRequestOut(x, itemService.getItemsForRequest(x.getId())))
                .collect(Collectors.toList());
    }

}

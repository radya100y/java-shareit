package ru.practicum.shareit.request;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ItemRequestRepository extends PagingAndSortingRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByAuthor_IdOrderByIdDesc(Long userId);

}

package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwner(long owner);

    @Query(value = "select it.id, it.name, it.description, it.available, it.owner_id as owner, it.request_id " +
            "from items as it where ((it.name ilike %?1%) or (it.description ilike %?1%)) and it.available",
            nativeQuery = true)
    List<Item> getByNameOrDescrAndAvail(String query);

    List<Item> findAllByRequest_IdOrderByIdDesc(Long requestId);
}

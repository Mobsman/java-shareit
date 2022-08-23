package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemByOwnerId(Long ownerId);

    Page<Item> findItemByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableIsTrue(String name, String description, Pageable pageable);

    @Query("SELECT i FROM Item AS i WHERE i.request.id=?1")
    List<Item> findItemByRequest(Long requestId);


}

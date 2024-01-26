package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.ItemRequestServiceJpa;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository mockItemRequestRepository;

    @Mock
    private UserService mockUserService;

    @Mock
    private ItemService mockItemService;


    @Test
    void testCreateRequest() {

        ItemRequestService itemRequestService = new ItemRequestServiceJpa(mockItemRequestRepository,
                mockUserService, mockItemService);

        Mockito
                .when(mockUserService.getModel(anyLong()))
                .thenReturn(User.builder().id(1).name("qwe").email("q@q.com").build());

        Mockito
                .when(mockItemService.getItemsForRequest(anyLong()))
                .thenReturn(List.of(ItemDto.builder().id(1).name("qwe").description("qwe").available(true).build()));

//        Mockito
//                .when(mockItemRequestRepository.save(ArgumentMatchers.any(ItemRequestDtoIn.class)))
//                .thenReturn(null);
//
//        ItemRequestDtoOut ir =
//                itemRequestService.save(new ItemRequestDtoIn(1L, "qwe", LocalDateTime.now()));

//        Assertions.assertEquals(ir.getDescription(), "qwe");
    }
}

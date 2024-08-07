package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class RequestControllerTest {
    @MockBean
    private ItemRequestService service;

    @Autowired
    private MockMvc mockMvc;

    private ItemRequestDto itemRequestDto;
    private final List<ItemRequestDto> requests = new ArrayList<>();
    private final ObjectMapper mapper = JsonMapper
            .builder()
            .findAndAddModules()
            .build();

    @BeforeEach
    void initData() {
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        List<Item> items = Collections.singletonList(item);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("desc");
        itemRequestDto.setRequesterId(1L);
        itemRequestDto.setItems(items);

        requests.add(itemRequestDto);
    }

    @AfterEach
    void clearData() {
        requests.clear();
    }

    @Test
    void create() throws Exception {
        when(service.create(any(), anyLong())).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requesterId", is(itemRequestDto.getRequesterId()), Long.class))
                .andExpect(jsonPath("$.items", is(Matchers.notNullValue())));

        verify(service, times(1)).create(any(), anyLong());
    }

    @Test
    void getOne() throws Exception {
        when(service.getOne(anyLong())).thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requesterId", is(itemRequestDto.getRequesterId()), Long.class))
                .andExpect(jsonPath("$.items", is(Matchers.notNullValue())));

        verify(service, times(1)).getOne(anyLong());
    }

    @Test
    void findPersonal() throws Exception {
        when(service.findPersonal(anyLong())).thenReturn(requests);

        mockMvc.perform(get("/requests").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.[0].requesterId", is(itemRequestDto.getRequesterId()), Long.class))
                .andExpect(jsonPath("$.[0].items", is(Matchers.notNullValue())));

        verify(service, times(1)).findPersonal(anyLong());
    }

}

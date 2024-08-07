package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.CommentText;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    private ItemDto itemDto;
    private Comment comment;
    private CommentText commentText;

    private final List<ItemDto> items = new ArrayList<>();
    private final ObjectMapper mapper = JsonMapper
            .builder()
            .findAndAddModules()
            .build();


    @BeforeEach
    void initData() {
        comment = new Comment();
        comment.setId(1L);
        comment.setText("it is comment");

        commentText = new CommentText();
        commentText.setText("comment text");

        itemDto = new ItemDto(1L, "item name", "item desc", true, 1L, 1L, Collections.singletonList(comment));
        items.add(itemDto);
    }

    @AfterEach
    void clearData() {
        items.clear();
    }

    @Test
    void create() throws Exception {
        when(itemService.create(any(), anyLong())).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

        verify(itemService, times(1)).create(any(), anyLong());
    }

    @Test
    void findAll() throws Exception {
        when(itemService.getAll(anyLong())).thenReturn(items);

        mockMvc.perform(get("/items").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())));

        verify(itemService, times(1)).getAll(anyLong());
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), anyString())).thenReturn(comment);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentText))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())));

        verify(itemService, times(1)).addComment(anyLong(), anyLong(), anyString());
    }

}

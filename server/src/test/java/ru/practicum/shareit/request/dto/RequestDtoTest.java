package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {

        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        List<Item> items = Collections.singletonList(item);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("desc");
        itemRequestDto.setRequesterId(1L);
        itemRequestDto.setItems(items);


        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result)
                .hasJsonPath("$.id")
                .hasJsonPath("$.requesterId")
                .hasJsonPath("$.description")
                .hasJsonPath("$.created")
                .hasJsonPath("$.items");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(itemRequestDto.getId()));
        assertThat(result).extractingJsonPathNumberValue("$.requesterId")
                .satisfies(requesterId -> assertThat(requesterId.longValue())
                        .isEqualTo(itemRequestDto.getRequesterId()));
        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(description -> assertThat(description).isEqualTo(itemRequestDto.getDescription()));
        assertThat(result).extractingJsonPathArrayValue("$.items")
                .satisfies(i -> assertThat(i).isNotEmpty());
    }
}

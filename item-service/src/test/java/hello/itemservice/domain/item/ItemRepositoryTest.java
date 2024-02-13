package hello.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {
    ItemRepository itemRepository = new ItemRepository();
    @AfterEach
    void afterEach(){
        itemRepository.clearStore();
    }

    @Test
    void save() {
        Item item = new Item("itemA", 10000, 10);

        Item saveItem = itemRepository.save(item); // 저장 한 item 리턴 해줌

        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(saveItem);
    }

    @Test
    void findAll() {
        Item item1 = new Item("itemA", 10000, 10);
        Item item2 = new Item("itemB", 50000, 10);

        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> result = itemRepository.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(item1, item2);
    }

    @Test
    void update() {
        Item item = new Item("itemA", 10000, 10);

        Item saveItem = itemRepository.save(item);
        Long itemId = saveItem.getId();

        Item updateParam = new Item("itemUpdate", 30000, 10);
        itemRepository.update(itemId, updateParam);

        Item findItem = itemRepository.findById(itemId);

        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }

    @Test
    void delete() {
        Item item = new Item("itemA", 10000, 10);
        Item saveItem = itemRepository.save(item);

        itemRepository.delete(saveItem.getId());

        // 아이템을 조회하여 null 값이 오면 존재하지 않음을 확인
        Item deletedItem = itemRepository.findById(saveItem.getId());
        assertThat(deletedItem).isNull();
    }
}
package hello.itemservice.domain.item.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

//    @Autowired
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute(item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "/basic/addForm";
    }
//    @PostMapping("/add")
    public String addItemV1(@RequestParam("itemName") String itemName,
                            @RequestParam("price") int price,
                            @RequestParam("quantity") Integer quantity,
                            Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        itemRepository.save(item);

        model.addAttribute("item",item);

        return "/basic/item";
    }
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item")Item item, Model model) {

        itemRepository.save(item);

//        model.addAttribute("item",item);

        return "/basic/item";
    }
    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {

        itemRepository.save(item);

        return "/basic/item";
    }
    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     */
//    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }
    /**
     * PRG - Post/Redirect/Get
     */
//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }
    @PostMapping("/add")
    public String addItemV6(@ModelAttribute Item item,
                            RedirectAttributes redirectAttributes) {
        Item saveItem = itemRepository.save(item);
        // @PathVariable 처리
        redirectAttributes.addAttribute("itemId", saveItem.getId());
        // 나머지는 쿼리파라미터로 처리
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit") // 수정 폼
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute(item);
        return "basic/editForm";
    }
    @PostMapping("/{itemId}/edit") // 수정 폼
    public String editForm(@PathVariable("itemId") Long itemId,
                           @ModelAttribute("item") Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}"; // @PathVariable 사용함
    }

    @GetMapping("/{itemId}/delete") // 삭제 컨트롤러 추가
    public String itemDelete(@PathVariable("itemId") Long itemId) {
        itemRepository.delete(itemId);
        return "redirect:/basic/items";
    }


    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct // 미리 데이터를 넣어줌
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));
    }
}

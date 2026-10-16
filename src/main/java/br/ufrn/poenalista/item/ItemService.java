package br.ufrn.poenalista.item;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.poenalista.shoppinglist.ShoppingList;
import br.ufrn.poenalista.shoppinglist.ShoppingListRepository;
import br.ufrn.poenalista.user.User;
import br.ufrn.poenalista.user.UserRepository;

/** Casos de uso da história #3: incluir, alterar quantidade e remover itens da lista. */
@Service
public class ItemService {

	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final ShoppingListRepository shoppingListRepository;
	private final UserRepository userRepository;

	public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository,
			ShoppingListRepository shoppingListRepository, UserRepository userRepository) {
		this.itemRepository = itemRepository;
		this.categoryRepository = categoryRepository;
		this.shoppingListRepository = shoppingListRepository;
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public List<Item> listItems(Long shoppingListId) {
		return itemRepository.findAllWithCategoryByShoppingListId(shoppingListId);
	}

	@Transactional
	public Item addItem(Long shoppingListId, Long addedByUserId, String name, int quantity, Long categoryId) {
		if (quantity < 1) {
			throw new IllegalArgumentException("A quantidade deve ser no mínimo 1.");
		}
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException(categoryId));
		ShoppingList list = shoppingListRepository.getReferenceById(shoppingListId);
		User addedBy = userRepository.getReferenceById(addedByUserId);

		return itemRepository.save(new Item(list, name.strip(), quantity, category, addedBy));
	}

	@Transactional
	public Item updateQuantity(Long shoppingListId, Long itemId, int quantity) {
		Item item = findInList(shoppingListId, itemId);
		item.updateQuantity(quantity);
		return item;
	}

	@Transactional
	public void removeItem(Long shoppingListId, Long itemId) {
		itemRepository.delete(findInList(shoppingListId, itemId));
	}

	private Item findInList(Long shoppingListId, Long itemId) {
		return itemRepository.findByIdAndShoppingListId(itemId, shoppingListId)
				.orElseThrow(() -> new ItemNotFoundException(itemId));
	}

}


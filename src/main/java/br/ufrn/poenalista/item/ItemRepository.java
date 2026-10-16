package br.ufrn.poenalista.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

	/**
	 * Itens de uma lista, já com a categoria carregada (a view lê {@code item.category.name}
	 * fora da transação, pois {@code spring.jpa.open-in-view=false}).
	 */
	@Query("select i from Item i left join fetch i.category where i.shoppingList.id = :shoppingListId order by i.id")
	List<Item> findAllWithCategoryByShoppingListId(Long shoppingListId);

	/** Garante que o item pertence à lista informada. */
	Optional<Item> findByIdAndShoppingListId(Long id, Long shoppingListId);

}


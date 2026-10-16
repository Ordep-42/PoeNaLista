package br.ufrn.poenalista.shoppinglist;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

	/** O par (casa, nome) é único — ver {@code uk_shopping_lists_house_name}. */
	Optional<ShoppingList> findByHouseIdAndName(Long houseId, String name);

}


package br.ufrn.poenalista.shoppinglist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Garante, ao subir a aplicação, que a lista padrão já exista antes da primeira requisição
 * (evitando que duas requisições simultâneas tentem criá-la ao mesmo tempo).
 */
@Component
public class DefaultShoppingListInitializer implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(DefaultShoppingListInitializer.class);

	private final DefaultShoppingListService defaultShoppingListService;

	public DefaultShoppingListInitializer(DefaultShoppingListService defaultShoppingListService) {
		this.defaultShoppingListService = defaultShoppingListService;
	}

	@Override
	public void run(ApplicationArguments args) {
		ShoppingListContext context = defaultShoppingListService.getOrCreateDefault();
		log.info("Lista padrão pronta: '{}' (id={}) da casa '{}'", context.shoppingListName(),
				context.shoppingListId(), context.houseName());
	}

}


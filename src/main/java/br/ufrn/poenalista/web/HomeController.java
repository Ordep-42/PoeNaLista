package br.ufrn.poenalista.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.ufrn.poenalista.item.CategoryService;
import br.ufrn.poenalista.item.ItemForm;
import br.ufrn.poenalista.item.ItemService;
import br.ufrn.poenalista.shoppinglist.DefaultShoppingListService;
import br.ufrn.poenalista.shoppinglist.ShoppingListContext;

@Controller
public class HomeController {

	private final DefaultShoppingListService defaultShoppingListService;
	private final ItemService itemService;
	private final CategoryService categoryService;

	public HomeController(DefaultShoppingListService defaultShoppingListService, ItemService itemService,
			CategoryService categoryService) {
		this.defaultShoppingListService = defaultShoppingListService;
		this.itemService = itemService;
		this.categoryService = categoryService;
	}

	/** Exibe a lista de compras padrão com o formulário de inclusão e os controles de cada item. */
	@GetMapping("/")
	public String home(Model model) {
		ShoppingListContext context = defaultShoppingListService.getOrCreateDefault();

		model.addAttribute("lista", context);
		model.addAttribute("itens", itemService.listItems(context.shoppingListId()));
		model.addAttribute("categorias", categoryService.listAll());
		// Após um POST inválido, o formulário (com os erros) chega como atributo flash.
		if (!model.containsAttribute("itemForm")) {
			model.addAttribute("itemForm", new ItemForm());
		}
		return "index";
	}

}

package br.ufrn.poenalista.item;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufrn.poenalista.shoppinglist.DefaultShoppingListService;
import br.ufrn.poenalista.shoppinglist.ShoppingListContext;
import jakarta.validation.Valid;

/**
 * Ações sobre os itens da lista (história #3). Segue o padrão Post/Redirect/Get: toda ação
 * redireciona para a página inicial, que exibe a lista; mensagens e erros de validação
 * viajam como atributos flash.
 */
@Controller
@RequestMapping("/itens")
public class ItemController {

	static final String FLASH_SUCCESS = "sucesso";
	static final String FLASH_ERROR = "erro";

	private final ItemService itemService;
	private final DefaultShoppingListService defaultShoppingListService;

	public ItemController(ItemService itemService, DefaultShoppingListService defaultShoppingListService) {
		this.itemService = itemService;
		this.defaultShoppingListService = defaultShoppingListService;
	}

	@PostMapping
	public String add(@Valid @ModelAttribute("itemForm") ItemForm form, BindingResult result,
			RedirectAttributes redirect) {
		if (result.hasErrors()) {
			redirect.addFlashAttribute("itemForm", form);
			redirect.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "itemForm", result);
			redirect.addFlashAttribute(FLASH_ERROR, "Não foi possível adicionar o item. Verifique os campos.");
			return "redirect:/";
		}

		ShoppingListContext context = defaultShoppingListService.getOrCreateDefault();
		Item item = itemService.addItem(context.shoppingListId(), context.userId(), form.getName(),
				form.getQuantity(), form.getCategoryId());
		redirect.addFlashAttribute(FLASH_SUCCESS, "\"" + item.getName() + "\" adicionado à lista.");
		return "redirect:/";
	}

	@PostMapping("/{id}/quantidade")
	public String updateQuantity(@PathVariable Long id, @Valid @ModelAttribute("quantityForm") QuantityForm form,
			BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			redirect.addFlashAttribute(FLASH_ERROR, firstErrorMessage(result));
			return "redirect:/";
		}

		ShoppingListContext context = defaultShoppingListService.getOrCreateDefault();
		Item item = itemService.updateQuantity(context.shoppingListId(), id, form.getQuantity());
		redirect.addFlashAttribute(FLASH_SUCCESS,
				"Quantidade de \"" + item.getName() + "\" atualizada para " + item.getQuantity() + ".");
		return "redirect:/";
	}

	@PostMapping("/{id}/remover")
	public String remove(@PathVariable Long id, RedirectAttributes redirect) {
		ShoppingListContext context = defaultShoppingListService.getOrCreateDefault();
		itemService.removeItem(context.shoppingListId(), id);
		redirect.addFlashAttribute(FLASH_SUCCESS, "Item removido da lista.");
		return "redirect:/";
	}

	/** Mensagem amigável; erros de conversão (ex.: "abc" na quantidade) não têm mensagem própria. */
	private static String firstErrorMessage(BindingResult result) {
		FieldError error = result.getFieldError();
		if (error == null || error.isBindingFailure()) {
			return "Informe uma quantidade válida (número inteiro maior ou igual a 1).";
		}
		return error.getDefaultMessage();
	}

}


package br.ufrn.poenalista.web;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.ufrn.poenalista.item.CategoryService;
import br.ufrn.poenalista.item.ItemService;
import br.ufrn.poenalista.shoppinglist.DefaultShoppingListService;
import br.ufrn.poenalista.shoppinglist.ShoppingListContext;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DefaultShoppingListService defaultShoppingListService;

	@MockitoBean
	private ItemService itemService;

	@MockitoBean
	private CategoryService categoryService;

	@Test
	void paginaInicialRenderizaIndex() throws Exception {
		given(defaultShoppingListService.getOrCreateDefault())
				.willReturn(new ShoppingListContext(1L, "Lista de Compras", "Minha Casa", 1L));
		given(itemService.listItems(anyLong())).willReturn(List.of());
		given(categoryService.listAll()).willReturn(List.of());

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attributeExists("lista", "itens", "categorias", "itemForm"));
	}

}

package br.ufrn.poenalista.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import br.ufrn.poenalista.item.Category;
import br.ufrn.poenalista.item.CategoryRepository;
import br.ufrn.poenalista.item.Item;
import br.ufrn.poenalista.item.ItemRepository;
import br.ufrn.poenalista.item.ItemService;
import br.ufrn.poenalista.item.ItemStatus;
import br.ufrn.poenalista.shoppinglist.DefaultShoppingListService;
import br.ufrn.poenalista.shoppinglist.ShoppingListContext;
import jakarta.persistence.EntityManager;

/**
 * Testes de aceitação da história #3 — "incluir e remover itens da lista de compras":
 * <ol>
 * <li>adicionar itens (nome, quantidade e categoria);</li>
 * <li>modificar a quantidade de um item existente (quantidade &gt;= 1);</li>
 * <li>remover itens.</li>
 * </ol>
 * Sobe a aplicação inteira com H2 + Flyway. Cada teste roda em uma transação desfeita ao
 * final, então os itens criados aqui não vazam para outros testes.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GerenciarItensDaListaTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ItemService itemService;

	@Autowired
	private DefaultShoppingListService defaultShoppingListService;

	@Autowired
	private EntityManager em;

	private ShoppingListContext lista;

	@BeforeEach
	void carregaListaPadrao() {
		lista = defaultShoppingListService.getOrCreateDefault();
	}

	// ---- Lista padrão (sem login) ----

	@Test
	void paginaInicialExibeListaPadraoSemLogin() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attribute("categorias", hasSize(10)))
				.andExpect(model().attribute("itens", hasSize(0)))
				.andExpect(content().string(containsString(DefaultShoppingListService.DEFAULT_LIST_NAME)))
				.andExpect(content().string(containsString("A lista está vazia")));
	}

	@Test
	void listaPadraoEhCriadaUmaUnicaVez() {
		ShoppingListContext deNovo = defaultShoppingListService.getOrCreateDefault();

		assertThat(deNovo).isEqualTo(lista);
	}

	// ---- Critério 1: adicionar item ----

	@Test
	void adicionaItemComNomeQuantidadeECategoria() throws Exception {
		Category mercearia = categoria("Mercearia");

		mockMvc.perform(post("/itens")
				.param("name", "  Arroz  ")
				.param("quantity", "2")
				.param("categoryId", mercearia.getId().toString()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andExpect(flash().attributeExists("sucesso"));

		em.flush();
		em.clear();
		List<Item> itens = itemRepository.findAllWithCategoryByShoppingListId(lista.shoppingListId());
		assertThat(itens).hasSize(1);
		Item arroz = itens.get(0);
		assertThat(arroz.getName()).isEqualTo("Arroz");
		assertThat(arroz.getQuantity()).isEqualTo(2);
		assertThat(arroz.getCategory().getName()).isEqualTo("Mercearia");
		assertThat(arroz.getStatus()).isEqualTo(ItemStatus.PENDING);
		assertThat(arroz.getAddedBy().getEmail()).isEqualTo(DefaultShoppingListService.DEFAULT_USER_EMAIL);
	}

	@Test
	void itemAdicionadoApareceNaPaginaInicial() throws Exception {
		itemService.addItem(lista.shoppingListId(), lista.userId(), "Detergente", 3, categoria("Limpeza").getId());

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("itens", hasSize(1)))
				.andExpect(content().string(containsString("Detergente")))
				.andExpect(content().string(containsString("Limpeza")));
	}

	@Test
	void naoAdicionaItemComCamposInvalidos() throws Exception {
		MvcResult result = mockMvc.perform(post("/itens")
				.param("name", "   ")
				.param("quantity", "0")
				.param("categoryId", ""))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andExpect(flash().attributeExists("erro", "itemForm"))
				.andReturn();

		BindingResult erros = (BindingResult) result.getFlashMap()
				.get(BindingResult.MODEL_KEY_PREFIX + "itemForm");
		assertThat(erros.hasFieldErrors("name")).isTrue();
		assertThat(erros.hasFieldErrors("quantity")).isTrue();
		assertThat(erros.hasFieldErrors("categoryId")).isTrue();
		assertThat(itemRepository.findAllWithCategoryByShoppingListId(lista.shoppingListId())).isEmpty();
	}

	@Test
	void naoAdicionaItemComCategoriaInexistente() throws Exception {
		mockMvc.perform(post("/itens")
				.param("name", "Arroz")
				.param("quantity", "1")
				.param("categoryId", "999999"))
				.andExpect(status().isBadRequest());
	}

	// ---- Critério 2: modificar quantidade ----

	@Test
	void atualizaQuantidadeDeItemExistente() throws Exception {
		Item item = novoItem("Leite", 1);

		mockMvc.perform(post("/itens/{id}/quantidade", item.getId()).param("quantity", "6"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andExpect(flash().attributeExists("sucesso"));

		em.flush();
		em.clear();
		assertThat(itemRepository.findById(item.getId())).get()
				.extracting(Item::getQuantity).isEqualTo(6);
	}

	@ParameterizedTest
	@ValueSource(strings = { "0", "-3", "abc", "" })
	void rejeitaQuantidadeMenorQueUmOuInvalida(String quantidade) throws Exception {
		Item item = novoItem("Leite", 4);

		mockMvc.perform(post("/itens/{id}/quantidade", item.getId()).param("quantity", quantidade))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andExpect(flash().attributeExists("erro"));

		em.flush();
		em.clear();
		assertThat(itemRepository.findById(item.getId())).get()
				.extracting(Item::getQuantity).isEqualTo(4);
	}

	@Test
	void servicoTambemRejeitaQuantidadeMenorQueUm() {
		Item item = novoItem("Leite", 2);

		assertThatThrownBy(() -> itemService.updateQuantity(lista.shoppingListId(), item.getId(), 0))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void atualizarQuantidadeDeItemInexistenteRetorna404() throws Exception {
		mockMvc.perform(post("/itens/{id}/quantidade", 999999).param("quantity", "2"))
				.andExpect(status().isNotFound());
	}

	// ---- Critério 3: remover item ----

	@Test
	void removeItemDaLista() throws Exception {
		Item fica = novoItem("Pão", 1);
		Item sai = novoItem("Queijo", 1);

		mockMvc.perform(post("/itens/{id}/remover", sai.getId()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andExpect(flash().attributeExists("sucesso"));

		em.flush();
		em.clear();
		assertThat(itemRepository.findById(sai.getId())).isEmpty();
		assertThat(itemRepository.findAllWithCategoryByShoppingListId(lista.shoppingListId()))
				.extracting(Item::getId).containsExactly(fica.getId());
	}

	@Test
	void removerItemInexistenteRetorna404() throws Exception {
		mockMvc.perform(post("/itens/{id}/remover", 999999))
				.andExpect(status().isNotFound());
	}

	// ---- auxiliares ----

	private Item novoItem(String nome, int quantidade) {
		Item item = itemService.addItem(lista.shoppingListId(), lista.userId(), nome, quantidade,
				categoria("Outros").getId());
		em.flush();
		return item;
	}

	private Category categoria(String nome) {
		return categoryRepository.findAllByOrderByIdAsc().stream()
				.filter(c -> c.getName().equals(nome))
				.findFirst()
				.orElseThrow();
	}

}


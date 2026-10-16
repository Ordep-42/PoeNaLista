package br.ufrn.poenalista.shoppinglist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.poenalista.house.House;
import br.ufrn.poenalista.house.HouseRepository;
import br.ufrn.poenalista.house.Resident;
import br.ufrn.poenalista.house.ResidentRepository;
import br.ufrn.poenalista.house.ResidentRole;
import br.ufrn.poenalista.user.User;
import br.ufrn.poenalista.user.UserRepository;

/**
 * Fornece a lista de compras padrão usada enquanto login (#1) e criação de casas (#2)
 * não estão prontos. Cria, se ainda não existirem, um usuário, uma casa (com esse usuário
 * como moderador) e uma lista padrão, de modo que a página inicial funcione sem login.
 *
 * <p>Quando #1 e #2 forem integradas, o contexto deve passar a vir do usuário autenticado
 * e da casa/lista escolhida por ele; este serviço pode então ser removido.
 */
@Service
public class DefaultShoppingListService {

	public static final String DEFAULT_USER_NAME = "Morador Padrão";
	public static final String DEFAULT_USER_EMAIL = "morador.padrao@poenalista.local";
	/** Valor que nunca corresponde a um hash válido: o usuário padrão não consegue fazer login. */
	public static final String DEFAULT_USER_PASSWORD_HASH = "!sem-login";
	public static final String DEFAULT_HOUSE_NAME = "Minha Casa";
	public static final String DEFAULT_LIST_NAME = "Lista de Compras";

	private final UserRepository userRepository;
	private final HouseRepository houseRepository;
	private final ResidentRepository residentRepository;
	private final ShoppingListRepository shoppingListRepository;

	public DefaultShoppingListService(UserRepository userRepository, HouseRepository houseRepository,
			ResidentRepository residentRepository, ShoppingListRepository shoppingListRepository) {
		this.userRepository = userRepository;
		this.houseRepository = houseRepository;
		this.residentRepository = residentRepository;
		this.shoppingListRepository = shoppingListRepository;
	}

	/** Retorna o contexto da lista padrão, criando usuário, casa e lista se necessário. */
	@Transactional
	public ShoppingListContext getOrCreateDefault() {
		User user = userRepository.findByEmail(DEFAULT_USER_EMAIL)
				.orElseGet(() -> userRepository.save(
						new User(DEFAULT_USER_NAME, DEFAULT_USER_EMAIL, DEFAULT_USER_PASSWORD_HASH)));

		House house = houseRepository.findFirstByCreatedByIdAndNameOrderByIdAsc(user.getId(), DEFAULT_HOUSE_NAME)
				.orElseGet(() -> houseRepository.save(new House(DEFAULT_HOUSE_NAME, user)));

		if (!residentRepository.existsByHouseIdAndUserId(house.getId(), user.getId())) {
			residentRepository.save(new Resident(house, user, ResidentRole.MODERATOR));
		}

		ShoppingList list = shoppingListRepository.findByHouseIdAndName(house.getId(), DEFAULT_LIST_NAME)
				.orElseGet(() -> shoppingListRepository.save(new ShoppingList(house, DEFAULT_LIST_NAME)));

		return new ShoppingListContext(list.getId(), list.getName(), house.getName(), user.getId());
	}

}


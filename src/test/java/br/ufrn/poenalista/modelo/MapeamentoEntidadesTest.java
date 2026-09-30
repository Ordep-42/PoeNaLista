package br.ufrn.poenalista.modelo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import br.ufrn.poenalista.house.House;
import br.ufrn.poenalista.house.Invite;
import br.ufrn.poenalista.house.Resident;
import br.ufrn.poenalista.house.ResidentRole;
import br.ufrn.poenalista.item.Category;
import br.ufrn.poenalista.item.Item;
import br.ufrn.poenalista.item.ItemStatus;
import br.ufrn.poenalista.shoppinglist.ShoppingList;
import br.ufrn.poenalista.user.User;
import jakarta.persistence.PersistenceException;

/**
 * Garante que as entidades JPA batem com as tabelas criadas pelas migrations Flyway.
 * Usa o mesmo banco da aplicação (H2 em modo PostgreSQL) com {@code ddl-auto=validate}:
 * se entidade e migration divergirem, o contexto nem sobe.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MapeamentoEntidadesTest {

	@Autowired
	private TestEntityManager em;

	private User ana;
	private House house;

	@BeforeEach
	void criaUsuarioECasa() {
		ana = em.persist(new User("Ana", "ana@example.com", "hash"));
		house = em.persist(new House("República Central", ana));
	}

	@Test
	void persisteCasaComMoradorListaEItem() {
		Resident resident = em.persist(new Resident(house, ana, ResidentRole.MODERATOR));
		ShoppingList list = em.persist(new ShoppingList(house, "Mercado"));
		Category limpeza = category("Limpeza");
		Item item = em.persist(new Item(list, "Detergente", 2, limpeza, ana));
		em.flush();
		em.clear();

		Item lido = em.find(Item.class, item.getId());
		assertThat(lido.getName()).isEqualTo("Detergente");
		assertThat(lido.getQuantity()).isEqualTo(2);
		assertThat(lido.getStatus()).isEqualTo(ItemStatus.PENDING);
		assertThat(lido.getCategory().getName()).isEqualTo("Limpeza");
		assertThat(lido.getShoppingList().getHouse().getName()).isEqualTo("República Central");
		assertThat(lido.getAddedBy().getEmail()).isEqualTo("ana@example.com");
		assertThat(lido.getBoughtBy()).isNull();
		assertThat(lido.getCreatedAt()).isNotNull();
		assertThat(lido.getUpdatedAt()).isNotNull();

		Resident residentLido = em.find(Resident.class, resident.getId());
		assertThat(residentLido.getRole()).isEqualTo(ResidentRole.MODERATOR);
		assertThat(residentLido.getJoinedAt()).isNotNull();
	}

	@Test
	void casaPodeTerVariasListas() {
		em.persist(new ShoppingList(house, "Mercado"));
		em.persist(new ShoppingList(house, "Feira"));
		em.flush();

		Long total = em.getEntityManager()
				.createQuery("select count(l) from ShoppingList l where l.house = :house", Long.class)
				.setParameter("house", house)
				.getSingleResult();
		assertThat(total).isEqualTo(2);
	}

	@Test
	void migrationCarregaCategoriasIniciais() {
		List<String> nomes = em.getEntityManager()
				.createQuery("select c.name from Category c", String.class)
				.getResultList();

		assertThat(nomes).contains("Limpeza", "Frios", "Laticínios").hasSize(10);
	}

	@Test
	void emailDeUsuarioEhUnico() {
		assertThrows(PersistenceException.class,
				() -> em.persistAndFlush(new User("Outra Ana", "ana@example.com", "hash")));
	}

	@Test
	void usuarioNaoEntraDuasVezesNaMesmaCasa() {
		em.persistAndFlush(new Resident(house, ana, ResidentRole.MODERATOR));

		assertThrows(PersistenceException.class,
				() -> em.persistAndFlush(new Resident(house, ana, ResidentRole.MEMBER)));
	}

	@Test
	void codigoDeConviteEhUnico() {
		Instant expira = Instant.now().plus(7, ChronoUnit.DAYS);
		em.persistAndFlush(new Invite(house, "ABC123", ana, expira));

		assertThrows(PersistenceException.class,
				() -> em.persistAndFlush(new Invite(house, "ABC123", ana, expira)));
	}

	@Test
	void apagarCasaApagaListasEItensEmCascata() {
		ShoppingList list = em.persist(new ShoppingList(house, "Mercado"));
		em.persist(new Item(list, "Pão", 1, null, ana));
		em.flush();
		em.clear();

		em.getEntityManager().createNativeQuery("delete from houses where id = :id")
				.setParameter("id", house.getId())
				.executeUpdate();

		Number itens = (Number) em.getEntityManager()
				.createNativeQuery("select count(*) from items").getSingleResult();
		assertThat(itens.longValue()).isZero();
	}

	private Category category(String name) {
		return em.getEntityManager()
				.createQuery("select c from Category c where c.name = :name", Category.class)
				.setParameter("name", name)
				.getSingleResult();
	}

}

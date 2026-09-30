package br.ufrn.poenalista.item;

import java.time.Instant;

import br.ufrn.poenalista.shoppinglist.ShoppingList;
import br.ufrn.poenalista.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "shopping_list_id", nullable = false)
	private ShoppingList shoppingList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false)
	private int quantity;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ItemStatus status = ItemStatus.PENDING;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "added_by", nullable = false)
	private User addedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bought_by")
	private User boughtBy;

	@Column(name = "bought_at")
	private Instant boughtAt;

	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	protected Item() {
	}

	/** {@code category} é opcional. */
	public Item(ShoppingList shoppingList, String name, int quantity, Category category, User addedBy) {
		this.shoppingList = shoppingList;
		this.name = name;
		this.quantity = quantity;
		this.category = category;
		this.addedBy = addedBy;
	}

	@PrePersist
	void onCreate() {
		createdAt = Instant.now();
		updatedAt = createdAt;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public ShoppingList getShoppingList() {
		return shoppingList;
	}

	public Category getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	public ItemStatus getStatus() {
		return status;
	}

	public User getAddedBy() {
		return addedBy;
	}

	public User getBoughtBy() {
		return boughtBy;
	}

	public Instant getBoughtAt() {
		return boughtAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

}

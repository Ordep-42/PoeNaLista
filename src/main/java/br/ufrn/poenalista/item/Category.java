package br.ufrn.poenalista.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Categoria de produto. Os registros vêm das migrations (V2__seed_categories.sql). */
@Entity
@Table(name = "categories")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String name;

	protected Category() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}

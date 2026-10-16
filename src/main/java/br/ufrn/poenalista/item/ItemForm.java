package br.ufrn.poenalista.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Dados do formulário "Adicionar item". */
public class ItemForm {

	public static final int MAX_QUANTITY = 9999;

	@NotBlank(message = "Informe o nome do item.")
	@Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
	private String name;

	@NotNull(message = "Informe a quantidade.")
	@Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
	@Max(value = MAX_QUANTITY, message = "A quantidade deve ser no máximo " + MAX_QUANTITY + ".")
	private Integer quantity = 1;

	@NotNull(message = "Selecione uma categoria.")
	private Long categoryId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}


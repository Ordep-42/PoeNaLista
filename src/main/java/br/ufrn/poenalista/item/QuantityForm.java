package br.ufrn.poenalista.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Dados do formulário de alteração de quantidade de um item. */
public class QuantityForm {

	@NotNull(message = "Informe a quantidade.")
	@Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
	@Max(value = ItemForm.MAX_QUANTITY, message = "A quantidade deve ser no máximo " + ItemForm.MAX_QUANTITY + ".")
	private Integer quantity;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}


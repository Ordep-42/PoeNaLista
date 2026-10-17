package br.ufrn.poenalista.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Item inexistente ou que não pertence à lista em uso. */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {

	public ItemNotFoundException(Long itemId) {
		super("Item não encontrado: " + itemId);
	}

}


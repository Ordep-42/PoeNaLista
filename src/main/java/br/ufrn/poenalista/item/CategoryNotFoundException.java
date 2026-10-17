package br.ufrn.poenalista.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Categoria informada não existe (ex.: id adulterado no formulário). */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryNotFoundException extends RuntimeException {

	public CategoryNotFoundException(Long categoryId) {
		super("Categoria não encontrada: " + categoryId);
	}

}


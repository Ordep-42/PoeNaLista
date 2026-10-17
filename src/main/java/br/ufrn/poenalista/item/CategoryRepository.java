package br.ufrn.poenalista.item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	/** Mantém a ordem do seed (V2__seed_categories.sql), deixando "Outros" por último. */
	List<Category> findAllByOrderByIdAsc();

}


package br.ufrn.poenalista.house;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {

	Optional<House> findFirstByCreatedByIdAndNameOrderByIdAsc(Long createdById, String name);

}


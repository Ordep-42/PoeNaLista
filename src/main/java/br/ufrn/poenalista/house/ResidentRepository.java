package br.ufrn.poenalista.house;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentRepository extends JpaRepository<Resident, Long> {

	boolean existsByHouseIdAndUserId(Long houseId, Long userId);

}


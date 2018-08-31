package com.poolix.beihilfe.repository;

import com.poolix.beihilfe.domain.Rechnung;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Rechnung entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RechnungRepository extends JpaRepository<Rechnung, Long> {

    @Query("select rechnung from Rechnung rechnung where rechnung.user.login = ?#{principal.username}")
    List<Rechnung> findByUserIsCurrentUser();

}

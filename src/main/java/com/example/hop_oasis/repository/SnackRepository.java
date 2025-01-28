package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Snack;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SnackRepository extends JpaRepository<Snack, Long>, JpaSpecificationExecutor<Snack> {

    @Query("select distinct s.snackName from Snack s where s.id in (:ids)")
    Set<String> findNamesByIds(Collection<Long> ids);

}

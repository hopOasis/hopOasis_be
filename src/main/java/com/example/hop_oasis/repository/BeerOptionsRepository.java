package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.BeerOptions;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerOptionsRepository extends JpaRepository<BeerOptions, Long> {
    BeerOptions findByBeerIdAndVolume(Long beerId, double volume);
    @Transactional
    @Modifying
    @Query("UPDATE BeerOptions bo SET bo.quantity = bo.quantity -:quantity WHERE bo.beer.id = :beerId AND bo.volume = :volume")
    void decreaseQuantity(@Param("beerId") Long beerId, @Param("volume") double volume, @Param("quantity") int quantity);
    @Transactional
    @Modifying
    @Query("UPDATE BeerOptions bo SET bo.quantity = bo.quantity + :quantity WHERE bo.beer.id = :beerId AND bo.volume = :volume")
    void increaseQuantity(@Param("beerId") Long beerId, @Param("volume") double volume, @Param("quantity") int quantity);

}

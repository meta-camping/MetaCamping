package com.example.firstproject.repository;

import com.example.firstproject.entity.Camping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface CampingRepository extends JpaRepository<Camping,Long> {
    @Override
    ArrayList<Camping> findAll();

    @Query(value = "select * from camping where city_name = :city_name ORDER BY address", nativeQuery = true)
    ArrayList<Camping> findAllBySidoName(@Param("city_name") String city_name);

}


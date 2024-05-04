package com.devhp.SpringRestDemoWithGradle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devhp.SpringRestDemoWithGradle.model.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long>{
        
}

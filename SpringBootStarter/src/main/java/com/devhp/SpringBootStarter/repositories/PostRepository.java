package com.devhp.SpringBootStarter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devhp.SpringBootStarter.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    

}

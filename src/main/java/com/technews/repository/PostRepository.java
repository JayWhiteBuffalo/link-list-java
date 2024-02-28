package com.technews.repository;

import com.technews.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAllPostsByUserId(Integer id) throws Exception;
    List<Post> findAllByOrderByPostedAtDesc();
    List<Post> findAllByOrderByPostedAtAsc();

    @Query(value = "SELECT p.*, COUNT(v.id) AS vote_count " +
            "FROM post p " +
            "LEFT JOIN vote v ON p.id = v.post_id " +
            "GROUP BY p.id " +
            "ORDER BY vote_count DESC", nativeQuery = true)
    List<Post> findAllOrderByVoteCountDesc();

}
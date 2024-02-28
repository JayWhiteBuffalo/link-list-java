package com.technews.repository;

import com.technews.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Query("SELECT count(*) FROM Vote v where v.postId = :id")
    int countVotesByPostId(@Param("id") Integer id);

    @Query("SELECT vote FROM Vote vote WHERE vote.userId = :userId AND vote.postId = :postId")
    Vote findByUserIdAndPostId(@Param("userId") Integer userId, @Param("postId") Integer postId);

    @Query("SELECT v FROM Vote v WHERE v.postId = :id")
    List<Vote> findAllVotesForSinglePost(@Param("id") Integer id);
}
package com.technews.repository;

import com.technews.model.User;
import com.technews.model.UserAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttributeRepository extends JpaRepository<UserAttributes, Integer> {
    UserAttributes findByUserId(Integer id) throws Exception;
}

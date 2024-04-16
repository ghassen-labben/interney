package com.example.recruitment.repositories;

import com.example.recruitment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByStatus(Boolean status);


    @Query("SELECT DISTINCT user FROM User user " +
            "INNER JOIN FETCH user.authorities AS authorities " +
            "WHERE user.username = :username")
    User findByUsername(@Param("username") String username);

    User findUserByEmail(String email);
    @Query("select user.username FROM User user")
    public String[] findAllUsername();
    @Query("select user.email FROM User user")
    public String[] findAllEmail();


    boolean existsByUsername(String username);
}
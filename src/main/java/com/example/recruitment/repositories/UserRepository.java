package com.example.recruitment.repositories;

import com.example.recruitment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


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

    @Query("SELECT u FROM User u JOIN u.authorities au WHERE au.authority = :authorityName")
    List<User> findByAuthorityName(@Param("authorityName") String authorityName);


    @Query("SELECT u FROM User u JOIN InternshipApplication ia ON ia.encadrant = u AND ia.candidate = :candidate")
    Set<User> findEncadrantByCandidate(@Param("candidate") User candidate);

    @Query("SELECT ia.candidate FROM InternshipApplication ia WHERE ia.encadrant = :encadrant")
    List<User> findCandidatesByEncadrant(@Param("encadrant") User encadrant);
}
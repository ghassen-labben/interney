package com.example.recruitment.repositories;

import com.example.recruitment.models.Profile;
import com.example.recruitment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    public Profile findProfileByUser(User user);


}

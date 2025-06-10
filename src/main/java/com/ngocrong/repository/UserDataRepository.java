package com.ngocrong.repository;

import com.ngocrong.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface UserDataRepository extends JpaRepository<UserData, Integer> {

    List<UserData> findByUsernameAndPassword(String username, String password);
}

package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);

//    @Query(name = "Member.findByUserName")
    List<Member> findByUserName(@Param("userName") String userName);
}

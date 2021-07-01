package com.example.springdatajpa.repository;

import com.example.springdatajpa.dto.MemberDto;
import com.example.springdatajpa.entity.Member;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);

    //    @Query(name = "Member.findByUserName")
    List<Member> findByUserName(@Param("userName") String userName);

    @Query("select m from Member m where m.userName = :userName and m.age = :age")
    List<Member> findUser(@Param("userName") String userName, @Param("age") int age);

    @Query("select m.userName from Member m")
    List<String> findUserNameList();

    @Query("select new com.example.springdatajpa.dto.MemberDto(m.id, m.userName, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.userName in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUserName(String userName); // 컬렉션

    Member findMemberByUserName(String userName); // 단건

    Optional<Member> findOptionalByUserName(String userName); // 단건 Optional

    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageRequest);

    List<Member> findListByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",
        countQuery = "select count (m.userName) from Member m")
    Page<Member> findByAge2(int age, Pageable pageable); // count 쿼리를 분리할 수 있다! (굳이 join 한 녀석을 count 할 필요가 없다!)
}



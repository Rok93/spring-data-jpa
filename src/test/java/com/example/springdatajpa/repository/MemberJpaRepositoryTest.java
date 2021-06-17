package com.example.springdatajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springdatajpa.entity.Member;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void testMember() {
        //given
        Member member = new Member("memberA");

        //when
        Member savedMember = memberJpaRepository.save(member);

        //then
        Member findMember = memberJpaRepository.find(member.getId());
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUserName()).isEqualTo(savedMember.getUserName());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void basicCRUD() {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //when
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        //then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUserName("member!!!!!!!");

//        //리스트 조회 검증
//        List<Member> all = memberJpaRepository.findAll();
//        assertThat(all).hasSize(2);
//
//        //카운트 검증
//        long count = memberJpaRepository.count();
//        assertThat(count).isEqualTo(2);
//
//        //삭제 검증
//        memberJpaRepository.delete(member1);
//        memberJpaRepository.delete(member2);
//
//        long deletedCount = memberJpaRepository.count();
//        assertThat(deletedCount).isEqualTo(0);
    }
}

package com.example.springdatajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.springdatajpa.dto.MemberDto;
import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.entity.Team;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    private EntityManager em; // 참고: 같은 트랜잭션 내에서는 같은 EntityManager를 가진다!

    @Autowired
    private MemberQueryRepository memberQueryRepository;

    @Test
    void testMember() {
        System.out.println("memberRepository = " + memberRepository.getClass());
        //given
        Member member = new Member("memberA");

        //when
        Member savedMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUserName()).isEqualTo(savedMember.getUserName());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void basicCRUD() {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        //then
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUserName("member!!!!!!!");

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all).hasSize(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndGreaterThen() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUserNameAndAgeGreaterThan("AAA", 10);

        //then
        assertThat(result.get(0)).isEqualTo(m2);
        assertThat(result).hasSize(1);
    }

    @Test
    void testNamedQuery() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUserName("AAA");

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(m1);
        assertThat(result.get(1)).isEqualTo(m2);
    }

    @Test
    void findByUserQuery() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findUser("AAA", 10);

        //then
        assertThat(result.get(0)).isEqualTo(m1);
        assertThat(result).hasSize(1);
    }

    @Test
    void findUserNameList() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<String> result = memberRepository.findUserNameList();

        //then
        assertThat(result.get(0)).isEqualTo("AAA");
        assertThat(result.get(1)).isEqualTo("AAA");
        assertThat(result).hasSize(2);
    }

    @Test
    void testFindMemberDto() {
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10, team);
        memberRepository.save(m1);

        //when
        List<MemberDto> userNameList = memberRepository.findMemberDto();

        //then
        assertThat(userNameList).hasSize(1);
        assertThat(userNameList.get(0).getUserName()).isEqualTo("AAA");
        assertThat(userNameList.get(0).getTeamName()).isEqualTo("teamA");
    }

    @Test
    void findUserNames() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        //then
        assertThat(result.get(0)).isEqualTo(m1);
        assertThat(result.get(1)).isEqualTo(m2);
        assertThat(result).hasSize(2);
    }

    @Test
    void returnListType() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findListByUserName("AAA");

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    void returnType() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        Member result = memberRepository.findMemberByUserName("AAA");

        //then
        assertThat(result).isEqualTo(m1);
    }

    @Test
    void returnTypeIfNotExist() {
        //given
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);

        //when
        Member result = memberRepository.findMemberByUserName("BBB");

        //then
        assertThat(result).isNull();
        //JPA는 결과가 없으면 NotResultException이 발생한다! (getSingleResult()의 결과가 없으면 발생)
        // 자바 8 이전에는 많은 논쟁(예외를 던진다 or null로 넘긴다)이 있었으나 이제는 Optional 처리하면 된다!
    }

    @Test
    void returnTypeIfExistMultipleResults() {
        //given
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);
        memberRepository.save(new Member("AAA", 20));
        memberRepository.save(new Member("AAA", 30));

        //when //then
        assertThatThrownBy(() -> memberRepository.findMemberByUserName("AAA"))
            .isExactlyInstanceOf(IncorrectResultSizeDataAccessException.class);
        // 단건 조회시에 복수개의 데이터가 나오는 경우!
        // IncorrectResultSizeDataAccessException 예외가 발생한다!
        // 원래 발생하는 예외는 NonUniqueResultException 지만, SpringData JPA가 Spring Freamework 예외로 변경한다.
        // 왜냐하면 Repository 기술은 JPA기술이 될 수도 있고, 혹은 Mongo DB가 될 수도 있고 등등 다른 기술이 될 수 있다.
        // 하지만 그것을 사용하는 Service 계층의 클라이언트 코드들은 JPA나 이런데 의존하는게 아니라
        // 그냥 Spring이 추상화한 예에 의존하면, 다른 기술을 사용하더라도 Spring은 동일한 예외를 발생시킨다!
        // 결국은 클라이언트 코드를 바꿀 필요가 없다.
    }

    @Test
    void returnOptionalType() {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        Member result = memberRepository.findOptionalByUserName("AAA").get();

        //then
        assertThat(result).isEqualTo(m1);
    }

    @Test
    void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        PageRequest pageRequest = PageRequest
            .of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));// page를 0부터 시작한다!!

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //then
        List<Member> contents = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(contents).hasSize(3);
        assertThat(totalElements).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void pagingBySlice() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        PageRequest pageRequest = PageRequest
            .of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));// page를 0부터 시작한다!!

        //when
        Slice<Member> page = memberRepository
            .findSliceByAge(age, pageRequest); // Slice는 limit에 1개 더 추가해서 결과를 내놓는다!

        //then
        List<Member> contents = page.getContent();

        assertThat(contents).hasSize(3);
//        assertThat(totalElements).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void pagingByList() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        PageRequest pageRequest = PageRequest
            .of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));// page를 0부터 시작한다!!

        //when
        List<Member> page = memberRepository
            .findListByAge(age, pageRequest); // Slice는 limit에 1개 더 추가해서 결과를 내놓는다!

        //then
        assertThat(page).hasSize(3);

//        assertThat(contents).hasSize(3);
//        assertThat(totalElements).isEqualTo(5);
//        assertThat(page.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
//        assertThat(page.isFirst()).isTrue();
//        assertThat(page.hasNext()).isTrue();
    }

    @DisplayName("count 쿼리를 별도로 분리한 페이징 쿼리")
    @Test
    void paging2() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        PageRequest pageRequest = PageRequest
            .of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));// page를 0부터 시작한다!!

        //when
        Page<Member> page = memberRepository.findByAge2(age, pageRequest);

        //then
        List<Member> contents = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(contents).hasSize(3);
        assertThat(totalElements).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20); // 바로 DB에 업데이트 쿼리를 날려버린다!
//        em.flush(); // 남아있는 변경되지 않은 내용들을 DB에 반영 (사실 JPQL 실행전에 flush가 동작하기 때문에 불필요하다)
//        em.clear(); // 영속성 컨텍스트를 모두 날려버린다. (clearAutomatically 옵션 설정으로 대신한다!)

        Member member5 = memberRepository.findByUserName("member5").get(0);
        //member5의 나이는 몇살일까? 아직 쿼리가 안날아갔기 때문에.... 영속성 컨텍스트의 결과를 가져온다 '40살'일 것이다!
        System.out.println(member5.getAge());

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    void findMemberLazy() {
        //given

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findAll();

        //then
        for (Member member : members) {
            System.out.println("member = " + member.getUserName());
            System.out.println("member.teamClass = " + member.getTeam()
                .getClass()); // proxy 객체! (정확히는 Team$HibernateProxy)
            System.out.println("member.team = " + member.getTeam().getName()); // N + 1 문제가 발생한다!
        }
    }

    @Test
    void findMemberFetchJoin() {
        //given

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        List<Member> members = memberRepository.findMemberFetchJoin();

        //then
        for (Member member : members) {
            System.out.println("member = " + member.getUserName());
            System.out.println("member.teamClass = " + member.getTeam()
                .getClass()); // proxy 객체! (정확히는 Team$HibernateProxy)
            System.out.println("member.team = " + member.getTeam().getName()); // N + 1 문제가 발생한다!
        }
    }

    @Test
    void queryHint() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUserName("member1");
        findMember.setUserName("member2"); // DirtyChecking(변경감지) 의해 업데이트 쿼리가 나갈거라 생각했는데,

        //then
    }

    @Test
    void lock() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUserName("member1");

        //then
    }

    @Test
    void specBasic() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);
        em.flush();
        em.clear();

        //when
        Specification<Member> spec = MemberSpec.userName("m1").and(MemberSpec.teamName("teamA"));
        List<Member> findMembers = memberRepository.findAll(spec);

        //then
        assertThat(findMembers).hasSize(1);
    }

    @Test
    void queryByExample() {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        //when
        //Probe
        Member member = new Member("m1"); //entity 자체가 검색조건이 된다.
        Team team = new Team("teamA");
        member.setTeam(team);

        ExampleMatcher matcher = ExampleMatcher.matching() //age primitive type이기 때문에 age = 0 조건이 WHERE절에 추가된다!
            .withIgnorePaths("age");

        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("m1");
    }
}

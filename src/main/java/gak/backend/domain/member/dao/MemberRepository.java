package gak.backend.domain.member.dao;

import gak.backend.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //비밀번호 찾을 때, 이메일로 찾아야함.
    Optional<Member> findById(Long id);
    //Optional<Member> findByIdOrEmail(Long id, String email);

    Optional<Member> findByEmail(String email);
    boolean existsById(Long id);
    //탈퇴했던 계정인지 확인하기 위해 이메일이 존재하는지 확인.
    boolean existsByEmail(String email);
    //탈퇴후, 재가입이라면 멤버가 여러명일 수 있음.
    //생성할 때, 먼저 확인해야되나? -> existById를 통해서 먼저 확인하고 들어오면 될듯.
    List<Member> findMembersByEmail(String email);
//    List<Member> findByEmail(String email);
}
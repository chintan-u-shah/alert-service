package com.wheelsup.alertservice.repository;

import com.wheelsup.alertservice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByMemberId(Long memberId);
}

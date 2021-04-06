package com.wheelsup.alertservice.repository;

import com.wheelsup.alertservice.domain.PhoneContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneContactRepository extends JpaRepository<PhoneContact, Long> {

    List<PhoneContact> findByMemberId(Long memberId);
}

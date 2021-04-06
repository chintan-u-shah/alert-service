package com.wheelsup.alertservice.repository;

import com.wheelsup.alertservice.domain.AppAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppAlertRepository extends JpaRepository<AppAlert, Long> {
    List<AppAlert> findByClientUserIdAndAlertType(Long clientUserId, String alertType);
}

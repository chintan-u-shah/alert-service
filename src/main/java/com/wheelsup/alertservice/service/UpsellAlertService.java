package com.wheelsup.alertservice.service;

import com.wheelsup.alertservice.domain.AppAlert;
import com.wheelsup.alertservice.domain.Member;
import com.wheelsup.alertservice.external.api.EmailServiceClient;
import com.wheelsup.alertservice.external.api.FileGeneratorClient;
import com.wheelsup.alertservice.external.dto.*;
import com.wheelsup.alertservice.domain.PhoneContact;
import com.wheelsup.alertservice.repository.AppAlertRepository;
import com.wheelsup.alertservice.repository.MemberRepository;
import com.wheelsup.alertservice.repository.PhoneContactRepository;
import com.wheelsup.common.util.AuthUtil;
import com.wheelsup.common.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UpsellAlertService {

    private final MemberRepository memberRepository;
    private final PhoneContactRepository phoneContactRepository;
    private final FileGeneratorClient fileGeneratorClient;
    private final EmailServiceClient emailServiceClient;
    private final AppAlertRepository appAlertRepository;

    @Value("${alert.membershipUpsell.email.recipient}")
    private String membershipUpsellRecipient;

    @Value("${alert.membershipUpsell.email.from:no-reply@wheelsup.com}")
    private String membershipUpsellFrom;


    public GenericResponse sendAlert(AlertRequest alertRequest) {

        HtmlFileGeneratorRequest fileGeneratorRequest = extractFileGeneratorRequest(alertRequest);
        List<AppAlert> appAlerts = appAlertRepository.findByClientUserIdAndAlertType(((Long) ((UserData)fileGeneratorRequest.getData().get("data")).getUserId()).longValue(), alertRequest.getAlertType());
        List<AppAlert> notifiedAppAlerts = appAlerts.stream().filter(AppAlert::getIsAlertNotified).collect(toList());
        if(alertRequest.getIsAlertDismissed()) {
            // Since the alert was dismissed, no need to send email but update audit fields of
            // the AppAlert Entity
            updateAppAlert(alertRequest, fileGeneratorRequest, appAlerts,Boolean.FALSE, alertRequest.getIsAlertDismissed());
            return createAlertResponse(Boolean.FALSE);
        }

        if(notifiedAppAlerts.size() == 1) {
            // Since the Notification group was already notified an alert, no need to send an email
            // again but update AppAlert Entity audit fields
            updateAppAlert(alertRequest, fileGeneratorRequest, appAlerts,Boolean.FALSE, alertRequest.getIsAlertDismissed());
            return createAlertResponse(Boolean.FALSE);
        }
        // appAlerts == null || countAlertNotified == 0)
        // send an email to alert the Notification group
        GenericResponse<String> fileGeneratorResponse = fileGeneratorClient.generateFileContent(fileGeneratorRequest);
        EmailRequest emailRequest = populateEmailRequest(fileGeneratorResponse, (String) ((UserData) fileGeneratorRequest.getData().get("data")).getSubject());
        emailServiceClient.fireEmailNotification(emailRequest);
        updateAppAlert(alertRequest, fileGeneratorRequest, appAlerts,true, alertRequest.getIsAlertDismissed());
        return createAlertResponse(Boolean.TRUE);

    }

    @Transactional
    public void updateAppAlert(AlertRequest alertRequest, HtmlFileGeneratorRequest fileGeneratorRequest, List<AppAlert> appAlerts,Boolean isAlertNotified, Boolean isAlertDismissed) {
        AppAlert appAlert;
        if(appAlerts == null || appAlerts.isEmpty()) {
             appAlert = new AppAlert();
        } else {
            appAlert = appAlerts.get(0);
            isAlertNotified = isAlertNotified || appAlert.getIsAlertNotified();
        }
        appAlert.setIsAlertDismissed(isAlertDismissed);
        appAlert.setIsAlertNotified(isAlertNotified);
        appAlert.setClientUserId(((Long) ((UserData) fileGeneratorRequest.getData().get("data")).getUserId()));
        appAlert.setAlertType(alertRequest.getAlertType());
        appAlert.setNotifiedGroup(membershipUpsellRecipient);
        appAlertRepository.save(appAlert);
    }

    @NotNull
    private GenericResponse<AlertResponse> createAlertResponse(Boolean alertSent) {
        AlertResponse alertResponse = new AlertResponse();
        alertResponse.setIsAlertNotified(alertSent);
        GenericResponse<AlertResponse> response = new GenericResponse<>(alertResponse);
        return response;
    }

    public EmailRequest populateEmailRequest(GenericResponse<String> fileGeneratorResponse, String subject) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setFrom(membershipUpsellFrom);
        emailRequest.setTo(Arrays.asList(membershipUpsellRecipient));
        emailRequest.setHtml(fileGeneratorResponse.getData());
        emailRequest.setSubject(subject);
        return emailRequest;
    }

    /*
    Extracts various fields from the token or db
     */
    private HtmlFileGeneratorRequest extractFileGeneratorRequest(AlertRequest alertRequest) {
        Optional<Authentication> authentication = AuthUtil.getAuthentication();
        if(authentication.isPresent()) {
            UserData data = new UserData();
            data.setUserId (((Number)AuthUtil.getAuthenticationClaim(authentication.get(), "userId")).longValue());
            data.setUserName((String)AuthUtil.getAuthenticationClaim(authentication.get(), "userName"));
            Long memberId = ((Number) AuthUtil.getAuthenticationClaim(authentication.get(), "memberId")).longValue();
            data.setMemberId(memberId);
            data.setMemberType("Fly");
            List<PhoneContact> phoneContacts = phoneContactRepository.findByMemberId(memberId);
            if(phoneContacts != null && !phoneContacts.isEmpty()) {
                String phoneNumber = phoneContacts.stream().findFirst().get().getPhoneNumber();
                data.setPhoneNumber(phoneNumber);
            }
            List<Member> members = memberRepository.findByMemberId(memberId);
            if(members != null && !members.isEmpty()) {
                data.setLegalName(members.stream().findFirst().get().getLegalName());
                data.setSubject("Membership Inquiry via App –  " + data.getLegalName());
            }
            // TODO - CS - populate user location from AlertRequest.AlertDetails.Userlocation upon finalization
            data.setUserLocation("New York, New York");
            data.setInquiryType("Interested in Membership");
            data.setCopyrightYear(Integer.toString(LocalDate.now().getYear()));
            HtmlFileGeneratorRequest fileGeneratorRequest = populateFileMetadata();
            Map<String, Object> map = new HashMap<>();
            map.put("data", data);
            fileGeneratorRequest.setData(map);
            return fileGeneratorRequest;
        }
        return null;
    }

    @NotNull
    private HtmlFileGeneratorRequest populateFileMetadata() {
        HtmlFileGeneratorRequest fileGeneratorRequest = new HtmlFileGeneratorRequest();
        HtmlFileGeneratorRequest.Metadata metadata = new HtmlFileGeneratorRequest.Metadata();
        metadata.setTemplate("alertEmail");
        fileGeneratorRequest.setMetadata(metadata);
        return fileGeneratorRequest;
    }
}

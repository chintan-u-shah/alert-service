package com.wheelsup.alertservice.service;

import com.wheelsup.alertservice.domain.Member;
import com.wheelsup.alertservice.external.api.EmailServiceClient;
import com.wheelsup.alertservice.external.api.FileGeneratorClient;
import com.wheelsup.alertservice.external.dto.EmailRequest;
import com.wheelsup.alertservice.external.dto.HtmlFileGeneratorRequest;
import com.wheelsup.alertservice.domain.PhoneContact;
import com.wheelsup.alertservice.external.dto.UserData;
import com.wheelsup.alertservice.repository.MemberRepository;
import com.wheelsup.alertservice.repository.PhoneContactRepository;
import com.wheelsup.common.util.AuthUtil;
import com.wheelsup.common.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UpsellAlertService {

    private final MemberRepository memberRepository;
    private final PhoneContactRepository phoneContactRepository;
    private final FileGeneratorClient fileGeneratorClient;
    private final EmailServiceClient emailServiceClient;

    public GenericResponse sendAlert() {

        HtmlFileGeneratorRequest fileGeneratorRequest = extractFileGeneratorRequest();
        GenericResponse<String> fileGeneratorResponse = fileGeneratorClient.generateFileContent(fileGeneratorRequest);
        EmailRequest emailRequest = populateEmailRequest(fileGeneratorResponse, (String) ((UserData)fileGeneratorRequest.getData().get("data")).getSubject());
        return emailServiceClient.fireEmailNotification(emailRequest);
    }

    public EmailRequest populateEmailRequest(GenericResponse<String> fileGeneratorResponse, String subject) {
        EmailRequest emailRequest = new EmailRequest();
        // TODO - CS - Update this code
        emailRequest.setFrom("chintan.shah@wheelsup.com");
        // emailRequest.setFrom(((UserData)fileGeneratorRequest.getData()).getUserName());
        // emailRequest.setTo(Arrays.asList("MarketplaceInquiries@wheelsup.com"));
        emailRequest.setTo(Arrays.asList("chintan.shah@wheelsup.com"));
        emailRequest.setHtml(fileGeneratorResponse.getData());
        emailRequest.setSubject(subject);
        return emailRequest;
    }

    private HtmlFileGeneratorRequest extractFileGeneratorRequest() {
        Optional<Authentication> authentication = AuthUtil.getAuthentication();
        if(authentication.isPresent()) {
            UserData data = new UserData();
            data.setUserId ((Integer)AuthUtil.getAuthenticationClaim(authentication.get(), "userId"));
            data.setUserName((String)AuthUtil.getAuthenticationClaim(authentication.get(), "userName"));
            Integer memberId = (Integer) AuthUtil.getAuthenticationClaim(authentication.get(), "memberId");
            data.setMemberId(memberId);
            Map memberType = (Map)AuthUtil.getAuthenticationClaim(authentication.get(), "memberType");
            if(memberType != null) {
                data.setMemberType((String)memberType.get("name"));
            }
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

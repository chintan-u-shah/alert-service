package com.wheelsup.alertservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wheelsup.alertservice.domain.Member;
import com.wheelsup.alertservice.external.api.EmailServiceClient;
import com.wheelsup.alertservice.external.api.FileGeneratorClient;
import com.wheelsup.alertservice.external.dto.EmailRequest;
import com.wheelsup.alertservice.external.dto.FileGeneratorRequest;
import com.wheelsup.alertservice.external.dto.UserData;
import com.wheelsup.alertservice.domain.PhoneContact;
import com.wheelsup.alertservice.repository.MemberRepository;
import com.wheelsup.alertservice.repository.PhoneContactRepository;
import com.wheelsup.common.util.AuthUtil;
import com.wheelsup.common.util.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpsellAlertService {

    private final MemberRepository memberRepository;
    private final PhoneContactRepository phoneContactRepository;
    private final FileGeneratorClient fileGeneratorClient;
    private final EmailServiceClient emailServiceClient;

    public GenericResponse sendAlert() {

        FileGeneratorRequest fileGeneratorRequest = extractFileGeneratorRequest();
        ResponseEntity<byte[]> fileGeneratorResponse = fileGeneratorClient.generateFileContent(fileGeneratorRequest);
        return sendEmail(fileGeneratorResponse, fileGeneratorRequest);
    }

    public GenericResponse sendEmail(ResponseEntity<byte[]> fileGeneratorResponse, FileGeneratorRequest fileGeneratorRequest) {
        EmailRequest emailRequest = new EmailRequest();
        // TODO - CS - Update this code
        emailRequest.setFrom("chintan.shah@wheelsup.com");
        // emailRequest.setFrom(((UserData)fileGeneratorRequest.getData()).getUserName());
        // emailRequest.setTo(Arrays.asList("MarketplaceInquiries@wheelsup.com"));
        emailRequest.setTo(Arrays.asList("chintan.shah@wheelsup.com"));
        emailRequest.setHtml(new String(fileGeneratorResponse.getBody()));
        emailRequest.setSubject(((UserData)fileGeneratorRequest.getData()).getSubject());
        return emailServiceClient.fireEmailNotification(emailRequest);
    }

    private FileGeneratorRequest extractFileGeneratorRequest() {
        Optional<Authentication> authentication = AuthUtil.getAuthentication();
        if(authentication.isPresent()) {
            UserData userData = new UserData();
            userData.setUserId((Integer)AuthUtil.getAuthenticationClaim(authentication.get(), "userId"));
            userData.setUserName((String)AuthUtil.getAuthenticationClaim(authentication.get(), "userName"));
            Integer memberId = (Integer) AuthUtil.getAuthenticationClaim(authentication.get(), "memberId");
            userData.setMemberId(memberId);
            Map memberType = (Map)AuthUtil.getAuthenticationClaim(authentication.get(), "memberType");
            if(memberType != null) {
                userData.setMemberType((String)memberType.get("name"));
            }
            List<PhoneContact> phoneContacts = phoneContactRepository.findByMemberId(memberId);
            if(phoneContacts != null && !phoneContacts.isEmpty()) {
                String phoneNumber = phoneContacts.stream().findFirst().get().getPhoneNumber();
                userData.setPhoneNumber(phoneNumber);
            }
            List<Member> members = memberRepository.findByMemberId(memberId);
            if(members != null && !members.isEmpty()) {
                userData.setSubject("Membership Inquiry via App –  " + members.stream().findFirst().get().getLegalName());
            }
            FileGeneratorRequest fileGeneratorRequest = populateFileMetadata();
            fileGeneratorRequest.setData(userData);
            return fileGeneratorRequest;
        }
        return null;
    }

    @NotNull
    private FileGeneratorRequest populateFileMetadata() {
        FileGeneratorRequest fileGeneratorRequest = new FileGeneratorRequest();
        FileGeneratorRequest.Metadata metadata = new FileGeneratorRequest.Metadata();
        metadata.setFileName("alertEmail");
        metadata.setReportType("normal");
        metadata.setTemplate("wuAlertEmail");
        metadata.setFileType("HTML");
        fileGeneratorRequest.setMetadata(metadata);
        return fileGeneratorRequest;
    }
}

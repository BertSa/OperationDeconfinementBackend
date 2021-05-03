package ca.bertsa.cal.h21_420_445.operation_deconfinement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentServer {

    @Value("${bertsa.ministere.url}")
    public String ministereUrl;

    @Value("${bertsa.messages.error.email}")
    public String messageErrorEmail;
    @Value("${bertsa.messages.error.other}")
    public String messageErrorOther;
    @Value("${bertsa.messages.error.address}")
    public String messageErrorAddress;
    @Value("${bertsa.messages.error.phone}")
    public String messageErrorPhone;
    @Value("${bertsa.messages.error.already_completed}")
    public String messageErrorAlreadyCompleted;
    @Value("${bertsa.messages.error.tutor}")
    public String messageErrorTutor;
    @Value("${bertsa.messages.error.not_eligible_for_license}")
    public String messageErrorNotEligibleForLicense;

    @Value("bertsa.error.nassm.noexist")
    public String messageErrorNassmDoesntExist;
    @Value("bertsa.error.nassm.registered")
    public String messageErrorNassmRegistered;
    @Value("bertsa.error.nassm.invalid")
    public String messageErrorNassmInvalid;
    @Value("bertsa.error.nassm.isnull")
    public String messageErrorNassmIsNull;



}
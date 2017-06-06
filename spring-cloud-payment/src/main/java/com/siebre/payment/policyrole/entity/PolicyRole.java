package com.siebre.payment.policyrole.entity;

import com.siebre.basic.model.BaseObject;
import com.siebre.payment.entity.enums.Beneficiary;
import com.siebre.payment.entity.enums.CertificateType;
import com.siebre.payment.entity.enums.Gender;
import com.siebre.payment.entity.enums.PolicyRoleType;

import java.util.Date;

public class PolicyRole extends BaseObject {

	private static final long serialVersionUID = 2709642927506475097L;

	private String name;

    private Gender gender;

    private String phoneNumber;

    private String email;

    private Date birthday;

    private CertificateType certificateType;

    private String certificateNumber;

    private PolicyRoleType policyRoleType;

    private Beneficiary beneficiary;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber == null ? null : certificateNumber.trim();
    }

    public PolicyRoleType getPolicyRoleType() {
        return policyRoleType;
    }

    public void setPolicyRoleType(PolicyRoleType policyRoleType) {
        this.policyRoleType = policyRoleType;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }
}
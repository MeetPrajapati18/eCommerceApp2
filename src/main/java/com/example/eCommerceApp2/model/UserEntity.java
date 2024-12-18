package com.example.eCommerceApp2.model;


import org.springframework.data.annotation.Id;

import java.util.Date;

public class UserEntity {

    @Id
    private String id;

    private String name, mobileNumber, email, address, city, state, pinCode, password, image, role, resetToken;

    private Boolean isEnable, accountNonLocked;

    private Integer failedAttempt;

    private Date lockTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPinCode() { return pinCode; }
    public void setPinCode(String pinCode) { this.pinCode = pinCode; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getEnable() { return isEnable; }
    public void setEnable(Boolean enable) { isEnable = enable; }

    public Boolean getAccountNonLocked() { return accountNonLocked;}
    public void setAccountNonLocked(Boolean accountLocked) { this.accountNonLocked = accountLocked; }

    public Integer getFailedAttempt() { return failedAttempt; }
    public void setFailedAttempt(Integer failedAttempt) { this.failedAttempt = failedAttempt; }

    public Date getLockTime() { return lockTime; }
    public void setLockTime(Date lockTime) { this.lockTime = lockTime; }

    public String getResetToken() { return resetToken;}
    public void setResetToken(String resetToken) {this.resetToken = resetToken;}
}
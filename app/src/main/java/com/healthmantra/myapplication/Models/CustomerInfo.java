package com.healthmantra.myapplication.Models;

public class CustomerInfo {
    public String userName;
    public String userRegion;
    public String userLocation;
    public String userImage;
    public String userPhone;
    public String userWalletMoney;
    public String userAddress;
    public String userSuscription;
    public String userOrders;
    public String userEmail;
    public String userGender;
    public String userFirstTimeStatus;
    public String userPaymentStatus;

    public String getUserPaymentStatus() {
        return userPaymentStatus;
    }

    public void setUserPaymentStatus(String userPaymentStatus) {
        this.userPaymentStatus = userPaymentStatus;
    }

    public String getUserFirstTimeStatus() {
        return userFirstTimeStatus;
    }

    public void setUserFirstTimeStatus(String userFirstTimeStatus) {
        this.userFirstTimeStatus = userFirstTimeStatus;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public CustomerInfo() {
    }

    public CustomerInfo(String userPaymentStatus, String userFirstTimeStatus, String userEmail, String userGender, String userName, String userRegion, String userLocation, String userImage, String userPhone, String userWalletMoney, String userAddress, String userSuscription, String userOrders) {
        this.userName = userName;
        this.userRegion = userRegion;
        this.userLocation = userLocation;
        this.userImage = userImage;
        this.userPhone = userPhone;
        this.userWalletMoney = userWalletMoney;
        this.userAddress = userAddress;
        this.userSuscription = userSuscription;
        this.userOrders = userOrders;
        this.userGender = userGender;
        this.userEmail = userEmail;
        this.userFirstTimeStatus = userFirstTimeStatus;
        this.userPaymentStatus = userPaymentStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRegion() {
        return userRegion;
    }

    public void setUserRegion(String userRegion) {
        this.userRegion = userRegion;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserWalletMoney() {
        return userWalletMoney;
    }

    public void setUserWalletMoney(String userWalletMoney) {
        this.userWalletMoney = userWalletMoney;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserSuscription() {
        return userSuscription;
    }

    public void setUserSuscription(String userSuscription) {
        this.userSuscription = userSuscription;
    }

    public String getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(String userOrders) {
        this.userOrders = userOrders;
    }
}

package com.kulun.energynet.model.invite;

import java.io.Serializable;

/**
 * created by xuedi on 2019/4/19
 */
public class InviteContent implements Serializable {
    private AccountInfo accountInfo;
    private boolean alreadyExistsParentId;

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public boolean isAlreadyExistsParentId() {
        return alreadyExistsParentId;
    }

    public void setAlreadyExistsParentId(boolean alreadyExistsParentId) {
        this.alreadyExistsParentId = alreadyExistsParentId;
    }

    /**
     * {
     {
     "accountInfo":{
     },
     "alreadyExistsParentId":false
     },
     */
}

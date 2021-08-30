package com.kulun.energynet.model;

/**
 * Created by Administrator on 2017/12/26.
 */

public class RewardInfo {

    private String name;
    private Double reward;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getReward() { return reward; }

    public void setReward(Double reward) { this.reward = reward; }

    @Override
    public String toString() {
        return "name: " + name + "; reward: " + reward;
    }
}

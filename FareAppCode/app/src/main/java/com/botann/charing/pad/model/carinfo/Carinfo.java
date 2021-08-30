package com.botann.charing.pad.model.carinfo;

import java.io.Serializable;
import java.util.List;

public class Carinfo implements Serializable {
    private Carmodule car;
    private String account;
    private String carId;
    private List<String> accountPlateNumber;

    public List<String> getAccountPlateNumber() {
        return accountPlateNumber;
    }

    public void setAccountPlateNumber(List<String> accountPlateNumber) {
        this.accountPlateNumber = accountPlateNumber;
    }

    public Carmodule getCar() {
        return car;
    }

    public void setCar(Carmodule car) {
        this.car = car;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    /*
    * "car": {
			"username": "测试司机f1",
			"carplate": "苏E05EV8",
			"totalMiles": "13439",
			"oldTotalMiles": "100",
			"lsBatteryInfoPack": [{
				"batteryNumber": "S75H035X00683",
				"highestSingletonVolt": "3.6870000000000003",
				"lowestSingletonVolt": "3.678"
			}, {
				"batteryNumber": "S75H035T00522",
				"highestSingletonVolt": "3.6870000000000003",
				"lowestSingletonVolt": "3.678"
			}, {
				"batteryNumber": "S75D036L01756",
				"highestSingletonVolt": "3.6870000000000003",
				"lowestSingletonVolt": "3.678"
			}, {
				"batteryNumber": "S75D036S02421",
				"highestSingletonVolt": "3.6870000000000003",
				"lowestSingletonVolt": "3.678"
			}],
			"soc": "44.0"
		},
		"account": "2018091300000016"
    * */
}

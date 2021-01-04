package com.ka8eem.digissquaredtest.models;

import com.google.gson.annotations.SerializedName;

public class DataModel{

	@SerializedName("RSRQ")
	private double rSRQ;

	@SerializedName("RSRP")
	private double rSRP;

	@SerializedName("SINR")
	private double sINR;

	public double getRSRQ(){
		return rSRQ;
	}

	public double getRSRP(){
		return rSRP;
	}

	public double getSINR(){
		return sINR;
	}
}
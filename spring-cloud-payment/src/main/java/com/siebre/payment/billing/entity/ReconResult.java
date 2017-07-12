package com.siebre.payment.billing.entity;

public class ReconResult {

	private int txnCount;

	private int matchedCount;

	public int getTxnCount() {
		return txnCount;
	}

	public void setTxnCount(int txnCount) {
		this.txnCount = txnCount;
	}

	public int getMatchedCount() {
		return matchedCount;
	}

	public void setMatchedCount(int matchedCount) {
		this.matchedCount = matchedCount;
	}

}
package com.fiserv.payment.connector.model.arcsi.request;

public class Original {

	
	private String txn_type;
	//private String client_txn_id;
	private Host host;
	
//	public String getClient_txn_id() {
//		return client_txn_id;
//	}
//
//	public void setClient_txn_id(String client_txn_id) {
//		this.client_txn_id = client_txn_id;
//	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public String getTxn_type() {
		return txn_type;
	}

	public void setTxn_type(String txn_type) {
		this.txn_type = txn_type;
	}
	
}

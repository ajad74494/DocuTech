package io.naztech.abbyy;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name= "_festool_extraction_v3")
public class Festool {
	
	private String _lCurrency,_lRes,_sFestool,_AssociationMembershipNo,_Order;
	
	@XmlElement (name= "_lCurrency")
	public String get_lCurrency() {
		return _lCurrency;
	}

	public void set_lCurrency(String _lCurrency){
		this._lCurrency = _lCurrency;
	}
	
	@XmlElement(name= "_lRes")
	public String get_lRes() {
		return _lRes;
	}

	public void set_lRes(String _lRes) {
		this._lRes = _lRes;
	}
	
	@XmlElement (name= "_lRes")
	public String get_sFestool() {
		return _sFestool;
	}

	public void set_sFestool(String _sFestool) {
		this._sFestool = _sFestool;
	}
	
	@XmlElement (name = "_AssociationMembershipNo")
	public String get_AssociationMembershipNo() {
		return _AssociationMembershipNo;
	}

	public void set_AssociationMembershipNo(String _AssociationMembershipNo) {
		this._AssociationMembershipNo = _AssociationMembershipNo;
	}
	
	@XmlElement (name = "_Order")
	public String get_Order() {
		return _Order;
	}

	public void set_Order(String _Order) {
		this._Order = _Order;
	}

	public Festool(String _lCurrency, String _lRes, String _sFestool, String _AssociationMembershipNo, String _Order) {
		super();
		this._lCurrency = _lCurrency;
		this._lRes = _lRes;
		this._sFestool = _sFestool;
		this._AssociationMembershipNo = _AssociationMembershipNo;
		this._Order = _Order;
	}

	public Festool() {
		super();
	}
	
	

}

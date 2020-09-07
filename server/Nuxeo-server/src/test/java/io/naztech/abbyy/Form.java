package io.naztech.abbyy;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "Documents")
public class Form {
	
	private Festool fest;

	@XmlElement
	public Festool getFest() {
		return fest;
	}

	public void setFest(Festool fest) {
		this.fest = fest;
	}

	public Form() {
		super();
	}

	public Form(Festool fest) {
		super();
		this.fest = fest;
	}
	

}

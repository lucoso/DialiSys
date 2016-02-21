package br.com.DialiSys.Model;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ExameUreia {

	@Column(name="Exame_UreiaPre", scale = 2, nullable=true)
	private BigDecimal ureiaPre;
	@Column(name="Exame_UreiaPos", scale = 2, nullable=true)
	private BigDecimal ureiaPos;

	public BigDecimal getUreiaPre() {
		return ureiaPre;
	}
	public void setUreiaPre(BigDecimal ureiaPre) {
		this.ureiaPre = ureiaPre;
	}
	public BigDecimal getUreiaPos() {
		return ureiaPos;
	}
	public void setUreiaPos(BigDecimal ureiaPos) {
		this.ureiaPos = ureiaPos;
	}


}

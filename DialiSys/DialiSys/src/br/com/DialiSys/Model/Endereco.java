package br.com.DialiSys.Model;


import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class Endereco {

	@Column(name="End_Rua", length=100, nullable=false)
	private String rua;
	@Column(name="End_Numero", length=10, nullable=true)
	private String numero;
	@Column(name="End_Complemento", length=50, nullable=true)
	private String complemento;
	@Column(name="End_Bairro", length=50, nullable=false)
	private String bairro;
	@Column(name="End_Cidade", length=100, nullable=false)
	private String cidade;
	@Column(name="End_UF", length=2, nullable=false)
	private String uf;
	@Column(name="End_País", length=100, nullable=false)
	private String pais;
	@Column(name="End_CEP", length=9, nullable=false)
	private String cep;

	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}


}

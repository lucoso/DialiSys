package br.com.DialiSys.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@NamedQueries({
	@NamedQuery(name = PessoaFisica.BuscarPorID, query= "select pf from PessoaFisica pf where pf.id = :id"),
	@NamedQuery(name = PessoaFisica.BuscarPorCPF, query = "select pf from PessoaFisica pf where pf.cpf.numero = :numero_cpf")
})
public class PessoaFisica extends Pessoa {

	public static final String BuscarPorID = "PessoaFisica.BuscarPorID";
	public static final String BuscarPorCPF = "PessoaFisica.BuscarPorCPF";

	
	@Embedded
	private CPF cpf;
	
	@Column(name="Pessoa_Sexo", length=9, nullable=true)
	private boolean sexo;
	
	@Column(name="Pessoa_DataNascimento", nullable=true)
	@Temporal(value=TemporalType.DATE)
	private Date datanasc;

	/**
	 * Construtor Sem Argumentos
	 */
	public PessoaFisica(){
		
	}
	
	
	public PessoaFisica(long id, String nome, String tel, String email,
			Endereco endereco, CPF cpf, boolean sexo, Date datanasc) {
		super(id, nome, tel, email, endereco);
		this.cpf = cpf;
		this.sexo = sexo;
		this.datanasc = datanasc;
	}

	public CPF getCpf() {
		return cpf;
	}

	public void setCpf(CPF cpf) {
		this.cpf = cpf;
	}

	public boolean isSexo() {
		return sexo;
	}

	public void setSexo(boolean sexo) {
		this.sexo = sexo;
	}

	public Date getDatanasc() {
		return datanasc;
	}

	public void setDatanasc(Date datanasc) {
		this.datanasc = datanasc;
	}



}

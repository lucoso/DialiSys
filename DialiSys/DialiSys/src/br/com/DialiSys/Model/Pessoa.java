package br.com.DialiSys.Model;


import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Pessoa extends ObjetoDominio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Pessoa_ID")
	private long id;
	
	@Column(name="Pessoa_Nome", length=100, nullable=false)
	private String nome;
	
	@Column(name="Pessoa_Tel", length=20, nullable=false)
	private String tel;
	
	@Column(name="Pessoa_Email", length=50, nullable=true)
	private String email;
	
	@Embedded
	private Endereco endereco;


	/**
	 * Construtor Sem Argumentos
	 */
	public Pessoa(){
		
	}
	
	public Pessoa(long id, String nome, String tel, String email,
			Endereco endereco) {
		super();
		this.id = id;
		this.nome = nome;
		this.tel = tel;
		this.email = email;
		this.endereco = endereco;
	}
	
	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Endereco getEndereco() {
		return endereco;
	}


	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
}

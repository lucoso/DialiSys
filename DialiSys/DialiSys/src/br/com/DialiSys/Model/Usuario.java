package br.com.DialiSys.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@NamedQueries({
	@NamedQuery(name = Usuario.BuscarPorID, query= "select u from Usuario u where u.pessoaf.id = :id"),
	@NamedQuery(name = Usuario.BuscarPorLogin, query = "select u from Usuario u where u.login = :login_usuario"),
	@NamedQuery(name = Usuario.ValidarUsuario, query = "select u from Usuario u where u.login = :usuario and u.senha = :senha")
})
public class Usuario extends ObjetoDominio{
	
	
	public static final String BuscarPorID = "Usuario.BuscarPorID";
	public static final String BuscarPorLogin = "Usuario.BuscarPorLogin";
	public static final String ValidarUsuario = "Usuario.ValidarUsuario";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Usuario_ID")
	private long id;
	
	@Column(name="Usuario_Login", length=100, nullable=false, unique=true)
	private String login;
	
	@Column(name="Usuario_Senha", length=50, nullable=false)
	private String senha;
	
	@Enumerated(EnumType.STRING)
	private Ator ator;
	
	@OneToOne
	@JoinColumn(name="PessoaFisica_ID")
	private PessoaFisica pessoaf;
	
	/**
	 * Construtor Sem Argumentos
	 */
	public Usuario(){
		
	}

	public Usuario(long id, String login, String senha, Ator ator,
			PessoaFisica pessoaf) {
		super();
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.ator = ator;
		this.pessoaf = pessoaf;
	}
	
	public long getId(){
		return id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaf;
	}

	public void setPessoaFisica(PessoaFisica pessoaf) {
		this.pessoaf = pessoaf;
	}
	
	public Ator getAtor() {
		return ator;
	}

	public void setAtor(Ator ator) {
		this.ator = ator;
	}
}

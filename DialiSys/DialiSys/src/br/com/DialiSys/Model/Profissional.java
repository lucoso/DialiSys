package br.com.DialiSys.Model;


import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@NamedQuery(name = Profissional.BuscarPorID, query= "select p from Profissional p where p.id = :id")
public abstract class Profissional extends Papel{

	public static final String BuscarPorID = "Profissional.BuscarPorID";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Profissional_ID")
	private long id;

	@ManyToMany()
	@JoinTable(name = "Profissionais_Clinicas")
	private Set<Clinica> clinicas;

	/**
	 * Construtor Sem Argumentos
	 */
	public Profissional(){

	}


	public Profissional(PessoaFisica pessoaf, long id, Set<Clinica> clinicas) {
		super(pessoaf);
		this.id = id;
		this.clinicas = clinicas;
	}

	public long getId(){
		return id;
	}


	public Set<Clinica> getClinicas() {
		return clinicas;
	}


	public void setClinicas(Set<Clinica> clinicas) {
		this.clinicas = clinicas;
	}

	
}

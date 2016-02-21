package br.com.DialiSys.Model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.DialiSys.Manager.SessaoManager;
import br.com.DialiSys.Util.JavaUtil;

@XmlRootElement
@Entity
@NamedQueries({
	@NamedQuery(name = Paciente.BuscarPorID, query= "select p from Paciente p where p.id = :id"),
	@NamedQuery(name = Paciente.BuscarPorProntuario, query= "select p from Paciente p where p.prontuario = :prontuario"),
	@NamedQuery(name = Paciente.BuscarPorMedico, query = "select p.pessoaf.nome from Paciente p where p.medicoResponsavel.pessoaf.nome = :nome_medico"),
	@NamedQuery(name = Paciente.BuscarPorEnfermeira, query = "select p.pessoaf.nome from Paciente p where p.enfermeiraResponsavel.pessoaf.nome = :nome_enfermeira")
})
public class Paciente extends Papel implements Serializable {

	/**
	 * Eclipse Pediu o serial por causa do Serializable
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String BuscarPorID = "Paciente.BuscarPorID";
	public static final String BuscarPorProntuario = "Paciente.BuscarPorProntuario";
	public static final String BuscarPorMedico = "Paciente.BuscarPorMedico";
	public static final String BuscarPorEnfermeira = "Paciente.BuscarPorEnfermeira";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Paciente_ID")
	private long id;
	
	@Column(name="Paciente_Prontuario", length=100, nullable=false, unique=true)
	private String prontuario;
	
	@Column(name="Paciente_DataInicio_Dialise", nullable=false)
	@Temporal(value=TemporalType.DATE)
	private Date datainicio;
	
	@Column(name="Paciente_PesoSeco", scale=2, nullable=false)
	private BigDecimal pesoseco;
	
	@Embedded
	private AntiCoagulante anticoagulante;
	
	@Embedded
	private AcessoVascular acessovascular;
	
	@ManyToOne()
	@JoinColumn(name="Medico_ID")
	private Medico medicoResponsavel;
	
	@ManyToOne()
	@JoinColumn(name="Enfermeira_ID")
	private Enfermeira enfermeiraResponsavel;

	@OneToMany(mappedBy="paciente", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Sessao> sessoes;

	@ManyToOne()
	@JoinColumn(name="Clinica_ID")
	private Clinica clinica;

	@Transient
	private CarregadorSessao carregadorSessao;
	
	@Transient
	private BigDecimal s;
	
	@Transient
	private BigDecimal d;
	
	@Transient
	private String media;

	/**
	 * Construtor Sem Argumentos
	 */
	public Paciente(){

	}

	public Paciente(PessoaFisica pessoaf, long id, String prontuario, Date datainicio, BigDecimal pesoseco,
			AntiCoagulante anticoagulante, AcessoVascular acessovascular,
			Medico medicoResponsavel, Enfermeira enfermeiraResponsavel,
			List<Sessao> sessoes, Clinica clinica,
			CarregadorSessao carregadorSessao, BigDecimal s, BigDecimal d,
			String media) {
		super(pessoaf);
		this.id = id;
		this.prontuario = prontuario;
		this.datainicio = datainicio;
		this.pesoseco = pesoseco;
		this.anticoagulante = anticoagulante;
		this.acessovascular = acessovascular;
		this.medicoResponsavel = medicoResponsavel;
		this.enfermeiraResponsavel = enfermeiraResponsavel;
		this.sessoes = sessoes;
		this.clinica = clinica;
		this.carregadorSessao = carregadorSessao;
		this.s = s;
		this.d = d;
		this.media = media;
	}
	
	public long getId(){
		return id;
	}

	public String getProntuario() {
		return prontuario;
	}
	public void setProntuario(String prontuario) {
		this.prontuario = prontuario;
	}


	public Date getDatainicio() {
		return datainicio;
	}
	public void setDatainicio(Date datainicio) {
		this.datainicio = datainicio;
	}
	public BigDecimal getPesoseco() {
		return pesoseco;
	}
	public void setPesoseco(BigDecimal pesoseco) {
		this.pesoseco = pesoseco;
	}
	public AntiCoagulante getAnticoagulante() {
		return anticoagulante;
	}
	public void setAnticoagulante(AntiCoagulante anticoagulante) {
		this.anticoagulante = anticoagulante;
	}
	public AcessoVascular getAcessovascular() {
		return acessovascular;
	}
	public void setAcessovascular(AcessoVascular acessovascular) {
		this.acessovascular = acessovascular;
	}

	public Medico getMedicoResponsavel() {
		return medicoResponsavel;
	}

	public void setMedicoResponsavel(Medico medicoResponsavel) {
		this.medicoResponsavel = medicoResponsavel;
	}

	public Enfermeira getEnfermeiraResponsavel() {
		return enfermeiraResponsavel;
	}

	public void setEnfermeiraResponsavel(Enfermeira enfermeiraResponsavel) {
		this.enfermeiraResponsavel = enfermeiraResponsavel;
	}

	public List<Sessao> getSessoes() {
		return sessoes;
	}

	public void setSessoes(List<Sessao> sessoes) {
		this.sessoes = sessoes;
	}

	public Clinica getClinica() {
		return clinica;
	}

	public void setClinica(Clinica clinica) {
		this.clinica = clinica;
	}

	public CarregadorSessao getCarregadorSessao() {
		return carregadorSessao;
	}

	public void setCarregadorSessao(CarregadorSessao carregadorSessao) {
		this.carregadorSessao = carregadorSessao;
	}

	public BigDecimal getS() {
		return s;
	}

	public BigDecimal getD() {
		return d;
	}

	public String getMedia() {
		return media;
	}

	public BigDecimal CalcularMediaUltrafiltracao(long id_paciente, Date dataInicial, Date dataFinal){


		List<Sessao> local = new ArrayList<Sessao>();
		List<BigDecimal> ufs = new ArrayList<BigDecimal>();
		BigDecimal soma = new BigDecimal(0);
		int qntd;
		BigDecimal MediaDaUf;
		carregadorSessao = new SessaoManager();


		local = carregadorSessao.BuscarSessaoCalculoMedia(id_paciente, dataInicial, dataFinal);

		for(Sessao s : local){

			BigDecimal bd = s.getUltraFiltracao();

			ufs.add(bd);
		}

		for(int i=0;i<ufs.size();i++){

			soma = ufs.get(i).add(soma);

		}

		qntd = ufs.size();
		BigDecimal qtd = new BigDecimal(qntd);

		MediaDaUf = soma.divide(qtd);

		return MediaDaUf;	

	}

	public void CalcularMediaPressaoArterialSistolica(long id_paciente, Date dataInicial, Date dataFinal){

		BigDecimal sist; 
		BigDecimal somasist = new BigDecimal(0);
		BigDecimal mediaSist;
		int qntd;
		List<PressaoArterial> listaPA = new ArrayList<PressaoArterial>();
		PressaoArterial pa = new PressaoArterial();
		List<Sessao> listaSlocal = new ArrayList<Sessao>();
		carregadorSessao = new SessaoManager();

		try{
		
			listaSlocal = carregadorSessao.BuscarSessaoCalculoMedia(id_paciente, dataInicial, dataFinal);

			Iterator<Sessao> itr = listaSlocal.iterator();
			for(int i=0; i<listaSlocal.size(); i++){
				while(itr.hasNext()){
					pa = itr.next().getPressoes().get(i);
					listaPA.add(pa);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao buscar as Sessões");
		}
		
		System.out.println(listaPA.isEmpty());
		for(int i=0;i<listaPA.size();i++){
			System.out.println(listaPA.get(i));
		}
		try{
			
			Iterator<PressaoArterial> itr = listaPA.iterator();
			for(int i=0; i<listaPA.size(); i++){
				while(itr.hasNext()){
					String s = itr.next().getPressao();
					String array[] = new String[1];
					array = s.split("/");
					sist = JavaUtil.ConvertStringToBigDecimal(array[0]);
					somasist = sist.add(somasist);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao somar as Sistolicas");
		}
		
		qntd = listaPA.size();
		BigDecimal qtd = new BigDecimal(qntd);

		mediaSist = somasist.divide(qtd);

		s = mediaSist;
	}



	public void CalcularMediaPressaoArterialDiastolica(long id_paciente, Date dataInicial, Date dataFinal){

		BigDecimal diast;
		BigDecimal somadiast = new BigDecimal(0);
		BigDecimal mediaDiast;
		int qntd;
		List<PressaoArterial> listaPA = new ArrayList<PressaoArterial>();
		PressaoArterial pa = new PressaoArterial();
		List<Sessao> listaSlocal = new ArrayList<Sessao>();
		carregadorSessao = new SessaoManager();

		try{
			
			listaSlocal = carregadorSessao.BuscarSessaoCalculoMedia(id_paciente, dataInicial, dataFinal);

			Iterator<Sessao> itr = listaSlocal.iterator();
			for(int i=0; i<listaSlocal.size(); i++){
				while(itr.hasNext()){
					 pa = itr.next().getPressoes().get(i);
					 listaPA.add(pa);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao buscar as Sessões");
		}
		
		try{
			
			Iterator<PressaoArterial> itr = listaPA.iterator();
			for(int i=0; i<listaPA.size(); i++){
				while(itr.hasNext()){
					String s = itr.next().getPressao();
					String array[] = new String[1];
					array = s.split("/");
					diast = JavaUtil.ConvertStringToBigDecimal(array[1]);
					somadiast = diast.add(somadiast);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao somar as Diastolicas");
		}

		qntd = listaPA.size();
		BigDecimal qtd = new BigDecimal(qntd);

		mediaDiast = somadiast.divide(qtd);

		d = mediaDiast;
	}


	public String MediaDaPressaoArterial(){

		media = s + "/" + d;

		return media;
	}
}
package br.com.DialiSys.Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
	@NamedQuery(name = Sessao.BuscarPorID, query= "select s from Sessao s where s.id = :id"),
	@NamedQuery(name = Sessao.BuscarPorPaciente, query = "select s from Sessao s where s.paciente.pessoaf.id = :pessoa_id"),
	@NamedQuery(name = Sessao.BuscarPorDataEPaciente, query = "select s from Sessao s where s.dataSessao = :data and s.paciente.pessoaf.id = :paciente_id")
})
@XmlRootElement
public class Sessao extends ObjetoDominio {

	public static final String BuscarPorID = "Sessao.BuscarPorID";
	public static final String BuscarPorPaciente = "Sessao.BuscarPorPaciente";
	public static final String BuscarPorDataEPaciente = "Sessao.BuscarPorDataEPaciente";
	public static BigDecimal ultimoktv = new BigDecimal(0); 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Sessao_ID")
	private long id;
	
	@Column(name = "Sessão_Paciente_PesoEntrada", precision=4, scale=2)
	private BigDecimal pesoEntrada;
	
	@Column(name = "Sessão_Paciente_PesoSaida", precision=4, scale=2)
	private BigDecimal pesoSaida;
	
	@Column(name = "Sessão_Ultrafiltração", precision=4, scale=2)
	private BigDecimal ultraFiltracao;
	
	@Column(name = "Sessão_TempoDuração", precision=4, scale=2)
	private BigDecimal tempoDuracao;
	
	@Column(name="Sessão_Intercorrencia", length=50, nullable=true)
	private String intercorrencia;
	
	@Column(name="Sessão_FluxoBomba", nullable=false)
	private int fluxo;
	
	@Column(name="Sessão_Emtransito", nullable=false)
	private boolean EmTransito;
	
	@Column(name="Sessão_Data", nullable=false)
	@Temporal(value=TemporalType.DATE)
	private Date dataSessao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="Paciente_ID")
	private Paciente paciente;

	@Embedded
	private ExameUreia exameUreia;


	@ElementCollection
	@CollectionTable(name="Sessao_Pressao_Arterial")
	private List <PressaoArterial> pressoes;

	@Column(name="Sessão_HoraEntrada", nullable=false)
	@Temporal(value=TemporalType.TIME)
	private Date horaEntrada;

	@Column(name="Sessão_HoraSaida", nullable=true)
	@Temporal(value=TemporalType.TIME)
	private Date horaSaida;

	@ManyToOne
	@JoinColumn(name="Clinica_ID")
	private Clinica clinica;
	
	@ManyToOne
	@JoinColumn(name="Profissional_ID")
	private Profissional profissional;
	
	@Column(name="Sessão_Solucao_Intercorrencia", length=200, nullable=true)
	private String solucao;
	
	@Column(name="Sessão_Excepcional", nullable=false)
	private boolean SessaoExcepcional;
	
	/**
	 * lista local so para o metodo registrapressao
	 */
	@Transient
	List<PressaoArterial> pr = new ArrayList<PressaoArterial>();


	public Sessao(long id, BigDecimal pesoEntrada, BigDecimal pesoSaida,
			BigDecimal ultraFiltracao, BigDecimal tempoDuracao,
			String intercorrencia, int fluxo, boolean emTransito,
			Date dataSessao, Paciente paciente, ExameUreia exameUreia,
			List<PressaoArterial> pressoes, Date horaEntrada, Date horaSaida,
			Clinica clinica, Profissional profissional, String solucao) {
		super();
		this.id = id;
		this.pesoEntrada = pesoEntrada;
		this.pesoSaida = pesoSaida;
		this.ultraFiltracao = ultraFiltracao;
		this.tempoDuracao = tempoDuracao;
		this.intercorrencia = intercorrencia;
		this.fluxo = fluxo;
		EmTransito = emTransito;
		this.dataSessao = dataSessao;
		this.paciente = paciente;
		this.exameUreia = exameUreia;
		this.pressoes = pressoes;
		this.horaEntrada = horaEntrada;
		this.horaSaida = horaSaida;
		this.clinica = clinica;
		this.profissional = profissional;
		this.solucao = solucao;
	}

	/**
	 * Construtor Sem Argumentos
	 */
	public Sessao(){
	}
	
	public long getId(){
		return id;
	}

	public BigDecimal getPesoEntrada() {
		return pesoEntrada;
	}

	public void setPesoEntrada(BigDecimal pesoEntrada) {
		this.pesoEntrada = pesoEntrada;
	}

	public BigDecimal getPesoSaida() {
		return pesoSaida;
	}

	public void setPesoSaida(BigDecimal pesoSaida) {
		this.pesoSaida = pesoSaida;
	}

	public BigDecimal getUltraFiltracao() {
		return ultraFiltracao;
	}

	public void setUltraFiltracao(BigDecimal ultraFiltracao) {
		this.ultraFiltracao = ultraFiltracao;
	}

	public BigDecimal getTempoDuracao() {
		return tempoDuracao;
	}

	public void setTempoDuracao(BigDecimal tempoDuracao) {
		this.tempoDuracao = tempoDuracao;
	}

	public String getIntercorrencia() {
		return intercorrencia;
	}

	public void setIntercorrencia(String intercorrencia) {
		this.intercorrencia = intercorrencia;
	}

	public int getFluxo() {
		return fluxo;
	}

	public void setFluxo(int fluxo) {
		this.fluxo = fluxo;
	}

	public boolean isEmTransito() {
		return EmTransito;
	}

	public void setEmTransito(boolean emTransito) {
		EmTransito = emTransito;
	}

	public Date getDataSessao() {
		return dataSessao;
	}

	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public ExameUreia getExameUreia() {
		return exameUreia;
	}

	public void setExameUreia(ExameUreia exameUreia) {
		this.exameUreia = exameUreia;
	}

	public List<PressaoArterial> getPressoes() {
		return pressoes;
	}

	public void setPressoes(List<PressaoArterial> pressoes) {
		this.pressoes = pressoes;
	}


	public Date getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(Date horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public Date getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(Date horaSaida) {
		this.horaSaida = horaSaida;
	}

	public Clinica getClinica() {
		return clinica;
	}

	public void setClinica(Clinica clinica) {
		this.clinica = clinica;
	}
	
	public List<PressaoArterial> getPr() {
		return pr;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public String getSolucao() {
		return solucao;
	}

	public void setSolucao(String solucao) {
		this.solucao = solucao;
	}
	
	public boolean isSessaoExcepcional() {
		return SessaoExcepcional;
	}

	public void setSessaoExcepcional(boolean sessaoExcepcional) {
		SessaoExcepcional = sessaoExcepcional;
	}

	public BigDecimal CalcularUltrafiltracao (){

		BigDecimal soro = new BigDecimal (0.3);

		BigDecimal u;



		if((paciente.getPesoseco() != null) && (paciente.getPesoseco().intValue() > 0)){
			if((this.pesoEntrada != null) && (this.pesoEntrada.intValue() >0 )){
				if(this.pesoEntrada.intValue() > paciente.getPesoseco().intValue()){ 

					u = (this.pesoEntrada.subtract(paciente.getPesoseco()));
					this.ultraFiltracao = u.add(soro);

				}else{

					System.out.println("O Paciente está com o peso de Entrada abaixo do Peso Seco! "
							+ "A ultrafiltração será somente os 300g do Soro!");

					this.ultraFiltracao = soro;


				}

			}else{
				
				System.out.println("O Peso de Entrada do Paciente informado é Inválido! "
						+ "          Por favor informe o valor novamente!");
			}

			
		}else{
			System.out.println("O Peso Seco do Paciente informado é Inválido! "
					+ "          Por favor informe o valor novamente!");
		}

		return ultraFiltracao;

	}
	
	
	public void RegistraPressaoArterial (BigDecimal sistolica, BigDecimal diastolica, Date hora) {
		
		if((sistolica != null) && (diastolica != null) && (hora != null)){
			
			PressaoArterial pa = new PressaoArterial(null, null, null, null);
			pa.setSistolica(sistolica);
			pa.setDiastolica(diastolica);
			pa.setHora(hora);
			pa.setPressao(pa.MontarPressao());
			pr.add(pa);
			
		}
	}
	


	public BigDecimal CalcularKTV (long pessoa_id, Date data){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		BigDecimal ureiaPr = new BigDecimal(0);
		BigDecimal ureiaPo = new BigDecimal(0);
		BigDecimal t = new BigDecimal(0);
		BigDecimal ult = new BigDecimal(0);
		BigDecimal pesoPos = new BigDecimal(0);
		 
		Sessao s = null;
		
		/*String consulta = "select s from Sessao s where s.paciente in("
				+ "select p from Paciente p join s.paciente where s.paciente.pessoaf.id = :pac_id and "
				+ "s.dataSessao = :dataS)";*/
		try{
		String consulta = "select s from Sessao s where s.paciente.pessoaf.id = :pac_id "
				+ "and s.dataSessao = :dataS";
		TypedQuery<Sessao> query = em.createQuery(consulta, Sessao.class);
		query.setParameter("pac_id", pessoa_id);
		query.setParameter("dataS", data);
		List<Sessao> resultado = query.getResultList();
		
		if(resultado.isEmpty()){
			
			BigDecimal bd = new BigDecimal(0);
			System.out.println("Sessão com exame uréia NÃO encontrada! Não é possível calcular o KTV");
			return bd;
			
		}else{
			
			Iterator<Sessao> itr = resultado.iterator();
			for(int i=0; i<resultado.size(); i++){
				while(itr.hasNext()){
					s = itr.next();
				}
			}

		//ureiaPr = resultado.getExameUreia().getUreiaPre();
		//ureiaPo = resultado.getExameUreia().getUreiaPos();
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
			
			ureiaPr = s.getExameUreia().getUreiaPre();
			ureiaPo = s.getExameUreia().getUreiaPos();
			t = s.getTempoDuracao();
			ult = s.getUltraFiltracao();
			pesoPos = s.getPesoSaida();
		

		//BigDecimal R = ureiaPo.divide(ureiaPr);
		BigDecimal R = ureiaPo.divide(ureiaPr, 2, RoundingMode.UP);
		double r = R.doubleValue();

		//BigDecimal t = resultado.getTempoDuracao();
		double t2 = t.doubleValue();

		//BigDecimal ult = resultado.getUltraFiltracao();
		double uf = ult.doubleValue();

		//BigDecimal pesoPos = resultado.getPesoSaida();
		double w = pesoPos.doubleValue();
		double ktv;

		ktv = -Math.log(r - (0.008 * t2)) + ((4 - (3.5 * r)) * (uf/w));
		
		BigDecimal KTV = new BigDecimal(ktv).setScale(2, RoundingMode.UP);
		
		ultimoktv = KTV;
		return KTV;
	}
}
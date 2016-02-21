package br.com.DialiSys.Manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.DialiSys.Model.CarregadorSessao;
import br.com.DialiSys.Model.Clinica;
import br.com.DialiSys.Model.Enfermeira;
import br.com.DialiSys.Model.ExameUreia;
import br.com.DialiSys.Model.Medico;
import br.com.DialiSys.Model.Paciente;
import br.com.DialiSys.Model.Profissional;
import br.com.DialiSys.Model.Sessao;
import br.com.DialiSys.Util.JavaUtil;

public class SessaoManager implements CarregadorSessao {
	
	public static long idGerado = 0;
	public static BigDecimal uf;
	public static boolean EMTransito;
	public static boolean excepcional;
	
	public int CadastrarSessao(String peso_entrada, String peso_saida, String tempo_duracao, String intercorrencia, 
			String solucao, String fluxo, String data_sessao, String horaentrada, String horasaida, 
			JSONArray sistolicas, JSONArray diastolicas, JSONArray hora, String ureiapre, String ureiapos, 
			String paciente_id, String profissional_id, String clinica_id) throws ParseException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Sessao s = new Sessao();
		Clinica c = new Clinica();
		Paciente p = new Paciente();
		ExameUreia eu = new ExameUreia();
		Profissional m = new Medico();
		Profissional e = new Enfermeira();
		List<Sessao> s_list = new ArrayList<Sessao>();
		List<Profissional> prof_list = new ArrayList<Profissional>();
		List<Paciente> pac_list = new ArrayList<Paciente>();
		List<Clinica> c_list = new ArrayList<Clinica>();
		List<BigDecimal> sistolicas_list = new ArrayList<BigDecimal>();
		List<BigDecimal> diastolicas_list = new ArrayList<BigDecimal>();
		List<Date> horas_list = new ArrayList<Date>();
		List<String> string_sist_list = new ArrayList<String>();
		List<String> string_diast_list = new ArrayList<String>();
		List<String> string_hora_list = new ArrayList<String>();
		long pac_id = JavaUtil.ConvertStringToLong(paciente_id);
		long prof_id = JavaUtil.ConvertStringToLong(profissional_id);
		long c_id = JavaUtil.ConvertStringToLong(clinica_id);
		long clinica_id_pac = 0;
		Number n2 = 0, n3 = 0;
		int count = 0;
		int cod_except = 0;
		boolean transito = true;
		boolean Notransito = false;
		boolean except = true;
		boolean NOexcept = false;
		
		try{
			
			Date datasessao = JavaUtil.ConvertStringToDate(data_sessao);
			TypedQuery<Sessao> query = em.createNamedQuery(Sessao.BuscarPorDataEPaciente, Sessao.class);
			query.setParameter("data", datasessao);
			query.setParameter("paciente_id", pac_id);
			s_list = query.getResultList();
			
			String consulta2 = "select p from Profissional p where p.pessoaf.id = :id";
			TypedQuery<Profissional> query2 = em.createQuery(consulta2, Profissional.class);
			query2.setParameter("id", prof_id);
			prof_list = query2.getResultList();
			
			String consulta3 = "select count(m) from Medico m where m.pessoaf.id = :id";
			TypedQuery<Number> query3 = em.createQuery(consulta3, Number.class);
			query3.setParameter("id", prof_id);
			n2 = query3.getSingleResult();
			
			String consulta4 = "select count(e) from Enfermeira e where e.pessoaf.id = :id";
			TypedQuery<Number> query4 = em.createQuery(consulta4, Number.class);
			query4.setParameter("id", prof_id);
			n3 = query4.getSingleResult();
			
			String consulta5 = "select p from Paciente p where p.pessoaf.id = :id";
			TypedQuery<Paciente> query5 = em.createQuery(consulta5, Paciente.class);
			query5.setParameter("id", pac_id);
			pac_list = query5.getResultList();
			
			TypedQuery<Clinica> query6 = em.createNamedQuery(Clinica.BuscarPorID, Clinica.class);
			query6.setParameter("id", c_id);
			c_list = query6.getResultList();
			
			if(s_list.isEmpty()){
				
				cod_except = 1;
			
			if(c_list.isEmpty()){
				System.out.println("Clinica n�o encontrada! Cadastre antes de criar uma Sess�o!");
				return 404;
				
			}else{
				
				Iterator<Clinica> itr = c_list.iterator();
				
				for(int i=0;i<c_list.size();i++){
						while(itr.hasNext()){
							c = itr.next();
						}
				}
			}
			
			if(pac_list.isEmpty()){
				
				System.out.println("Paciente N�O encontrado no BD! Cadastre antes de criar uma Sess�o!");
				return 4041;
			
			}else{
				
				Iterator<Paciente> itr = pac_list.iterator();
				
				for(int i=0;i<pac_list.size();i++){
						while(itr.hasNext()){
							p = itr.next();
							clinica_id_pac = p.getClinica().getId();
						}
				}
			}
			
			if(prof_list.isEmpty()){
				
				System.out.println("Profissional N�O encontrado no BD! Cadastre antes de criar uma Sess�o!");
				return 404;
			
			}else{
			
				if(n2.intValue()>=1){
				
				
					Iterator<Profissional> itr = prof_list.iterator();
					
					for(int i=0;i<prof_list.size();i++){
							while(itr.hasNext()){
								m = itr.next();
								count = 1;
								}
					}
				}
				
				if(n3.intValue()>=1){
					

					Iterator<Profissional> itr = prof_list.iterator();
					
					for(int i=0;i<prof_list.size();i++){
							while(itr.hasNext()){
									e = itr.next();
									count = 2;
							}
					}
				}
			}
			}

			/**
			 * confirmando que a lista de sessao N�O esta vazia!!
			 */
			if(!s_list.isEmpty()){
			/**
			 * tive que repetir a condi��o do profissional pois se a Lista de Sessao N�O FOR VAZIA o "count" N�O vai ser
			 * contado pois n�o vai passar pelo "if" acima...vai vir direto pra esse!!!
			 */
			if(prof_list.isEmpty()){
				
				System.out.println("Profissional N�O encontrado no BD! Cadastre antes de criar uma Sess�o!");
				return 404;
			
			}else{
			
				if(n2.intValue()>=1){
				
				
					Iterator<Profissional> itr = prof_list.iterator();
					
					for(int i=0;i<prof_list.size();i++){
							while(itr.hasNext()){
								m = itr.next();
								count = 1;
								}
					}
				}
				
				if(n3.intValue()>=1){
					

					Iterator<Profissional> itr = prof_list.iterator();
					
					for(int i=0;i<prof_list.size();i++){
							while(itr.hasNext()){
									e = itr.next();
									count = 2;
							}
					}
				}
			}
			
				/**
				 * Se count = 1: � um MEDICO que esta criando a Sess�o...ent�o SER� CRIADO A SESS�O EXCEPCIONAL!
				 * SEN�O: � uma Enfermeira que esta criando a Sess�o...ent�o N�O pode ser criado a Sess�o Excepcional..
				 */
				if(count == 1){
					
					if(c_list.isEmpty()){
						System.out.println("Clinica n�o encontrada! Cadastre antes de criar uma Sess�o!");
						return 404;
						
					}else{
						
						Iterator<Clinica> itr = c_list.iterator();
						
						for(int i=0;i<c_list.size();i++){
								while(itr.hasNext()){
									c = itr.next();
								}
						}
					}
					
					if(pac_list.isEmpty()){
						
						System.out.println("Paciente N�O encontrado no BD! Cadastre antes de criar uma Sess�o!");
						return 4041;
					
					}else{
						
						Iterator<Paciente> itr = pac_list.iterator();
						
						for(int i=0;i<pac_list.size();i++){
								while(itr.hasNext()){
									p = itr.next();
									clinica_id_pac = p.getClinica().getId();
								}
						}
					}
					
					excepcional = except;
					cod_except = 2;
				}else{
				
					System.out.println("Ja existe uma Sessao cadastrada para esse Paciente nesta Data!"
							+ "Somente o M�dico pode criar uma Sess�o Excepcional");
					return 409;
				}
			}
	}catch(Exception ex){
		ex.printStackTrace();
		System.out.println("Erro ao fazer busca no BD");
	}
		
		try{
			for(int i = 0; i<sistolicas.length(); i++){
				
				string_sist_list.add(sistolicas.getString(i));
			}
			
			Iterator<String> itr = string_sist_list.iterator();
			for(int i=0; i<string_sist_list.size();i++){
				while(itr.hasNext()){
				
				BigDecimal bd = JavaUtil.ConvertStringToBigDecimal(itr.next());
				sistolicas_list.add(bd);
			}
			}
						
			}catch(JSONException jex){
				
				jex.printStackTrace();
			
			}
		
		try{
			for(int i = 0; i<diastolicas.length(); i++){
				
				string_diast_list.add(diastolicas.getString(i));
			}
			
			Iterator<String> itr = string_diast_list.iterator();
			for(int i=0; i<string_diast_list.size();i++){
				while(itr.hasNext()){
				BigDecimal bd = JavaUtil.ConvertStringToBigDecimal(itr.next());
				diastolicas_list.add(bd);
			}
			}
						
			}catch(JSONException jex){
				
				jex.printStackTrace();
			
			}
		
		try{
			for(int i = 0; i<hora.length(); i++){
				
				string_hora_list.add(hora.getString(i));
			}
			
			Iterator<String> itr = string_hora_list.iterator();
			for(int i=0; i<string_hora_list.size();i++){
				while(itr.hasNext()){
				Date h = JavaUtil.ConvertStringToHora(itr.next());
				horas_list.add(h);
			}
			}
						
			}catch(JSONException jex){
				
				jex.printStackTrace();
			
			}
		
		/**
		 * Registra a Press�o Arterial
		 */
		try{
			
			Iterator<BigDecimal> itr_sist = sistolicas_list.iterator();
			Iterator<BigDecimal> itr_diast = diastolicas_list.iterator();
			Iterator<Date> itr_h = horas_list.iterator();
			
			for(int i=0; i<sistolicas_list.size(); i++){
				for(int j=0; j<diastolicas_list.size(); j++){
					for(int k=0; k<horas_list.size(); k++){
						while(itr_sist.hasNext()){
							while(itr_diast.hasNext()){
								while(itr_h.hasNext()){
									BigDecimal bd1 = itr_sist.next();
									BigDecimal bd2 = itr_diast.next();
									Date h = itr_h.next();
									s.RegistraPressaoArterial(bd1, bd2, h);
								}
							}
						}
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao Registrar as Press�es Arteriais");
		}
		
		try{
			if(count == 1){
			
				BigDecimal ureiaPre = JavaUtil.ConvertStringToBigDecimal(ureiapre);
				BigDecimal ureiaPos = JavaUtil.ConvertStringToBigDecimal(ureiapos);
				BigDecimal pesoEntrada = JavaUtil.ConvertStringToBigDecimal(peso_entrada);
				BigDecimal pesoSaida = JavaUtil.ConvertStringToBigDecimal(peso_saida);
				BigDecimal tempo = JavaUtil.ConvertStringToBigDecimal(tempo_duracao);
			
				Date dataS = JavaUtil.ConvertStringToDate(data_sessao);
			
				Date horaEntrada = JavaUtil.ConvertStringToHora(horaentrada);
				Date horaSaida = JavaUtil.ConvertStringToHora(horasaida);
			
				int fluxoBomba = JavaUtil.ConvertStringToInt(fluxo);
				
				/**
				 * Se o id da Clinica do PACIENTE for = ao id da Clinica passado por parametro...
				 * ent�o o Paciente Esta fazendo dialise na sua Clinica, ou seja, N�O ESTA FAZENDO "EM TRANSITO"
				 * Caso contrario ele esta FORA da sua Clinica, ou seja, ESTA FAZENDO DIALISE "EM TRANSITO"
				 */
				if(clinica_id_pac == c_id){
					
					s.setEmTransito(Notransito);
				}else{
					
					s.setEmTransito(transito);
				}
			
				eu.setUreiaPre(ureiaPre);
				eu.setUreiaPos(ureiaPos);
				
				s.setIntercorrencia(intercorrencia);
				s.setDataSessao(dataS);
				s.setExameUreia(eu);
				s.setFluxo(fluxoBomba);
				s.setHoraEntrada(horaEntrada);
				s.setHoraSaida(horaSaida);
				s.setPressoes(s.getPr());
				s.setSolucao(solucao);
				
				/**
				 * se cod_except = 1 - N�O � uma Sessao Excepcional
				 * se cod_except = 2 - � uma Sessao Excepcional
				 */
				if(cod_except == 1){
					
					s.setSessaoExcepcional(NOexcept);
				}
				
				if(cod_except == 2){
				
					s.setSessaoExcepcional(except);
				}
				
				s.setPaciente(p);
				p.getSessoes().add(s);
				s.setProfissional(m);
				s.setClinica(c);
				s.setTempoDuracao(tempo);
				s.setPesoEntrada(pesoEntrada);
				s.setPesoSaida(pesoSaida);
				s.CalcularUltrafiltracao();
				s.setUltraFiltracao(s.getUltraFiltracao());
			}
			
			if(count == 2){
				
				BigDecimal ureiaPre = JavaUtil.ConvertStringToBigDecimal(ureiapre);
				BigDecimal ureiaPos = JavaUtil.ConvertStringToBigDecimal(ureiapos);
				BigDecimal pesoEntrada = JavaUtil.ConvertStringToBigDecimal(peso_entrada);
				BigDecimal pesoSaida = JavaUtil.ConvertStringToBigDecimal(peso_saida);
				BigDecimal tempo = JavaUtil.ConvertStringToBigDecimal(tempo_duracao);
			
				Date dataS = JavaUtil.ConvertStringToDate(data_sessao);
			
				Date horaEntrada = JavaUtil.ConvertStringToHora(horaentrada);
				Date horaSaida = JavaUtil.ConvertStringToHora(horasaida);
			
				int fluxoBomba = JavaUtil.ConvertStringToInt(fluxo);
			
				/**
				 * Se o id da Clinica do PACIENTE for = ao id da Clinica passado por parametro...
				 * ent�o o Paciente Esta fazendo dialise na sua Clinica, ou seja, N�O ESTA FAZENDO "EM TRANSITO"
				 * Caso contrario ele esta FORA da sua Clinica, ou seja, ESTA FAZENDO DIALISE "EM TRANSITO"
				 */
				if(clinica_id_pac == c_id){
					
					s.setEmTransito(Notransito);
				}else{
					
					s.setEmTransito(transito);
				}
			
				eu.setUreiaPre(ureiaPre);
				eu.setUreiaPos(ureiaPos);
			
				s.setIntercorrencia(intercorrencia);
				s.setDataSessao(dataS);
				s.setExameUreia(eu);
				s.setFluxo(fluxoBomba);
				s.setHoraEntrada(horaEntrada);
				s.setHoraSaida(horaSaida);
				s.setPressoes(s.getPr());
				s.setSolucao(solucao);
				s.setSessaoExcepcional(NOexcept);
				s.setPaciente(p);
				p.getSessoes().add(s);
				s.setProfissional(e);
				s.setClinica(c);
				s.setTempoDuracao(tempo);
				s.setPesoEntrada(pesoEntrada);
				s.setPesoSaida(pesoSaida);
				s.CalcularUltrafiltracao();
				s.setUltraFiltracao(s.getUltraFiltracao());
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao setar os dados");
		}
		
		try{

			em.getTransaction().begin();
			em.persist(s);
			idGerado = s.getId();
			uf = s.getUltraFiltracao();
			EMTransito = s.isEmTransito();
			em.getTransaction().commit();

		}catch (Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Nova Sess�o Criado Com Sucesso!!!");
		return 201;
		}
	
	public JSONObject BuscarSessaoPorID(long id) throws JSONException{
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		JSONObject objRetornado = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		JSONObject obj3 = new JSONObject();
		JSONObject obj4 = new JSONObject();

		String consulta = "select s.id, s.pesoEntrada, s.pesoSaida, s.tempoDuracao, s.intercorrencia, s.solucao, s.fluxo, "
				+ "s.EmTransito, s.SessaoExcepcional, s.dataSessao, s.horaEntrada, s.horaSaida, s.exameUreia, s.ultraFiltracao from Sessao s where s.id = :id";

		Query query = em.createQuery(consulta);
		query.setParameter("id", id);
		Object[] resultado = (Object[]) query.getSingleResult();

		String consulta2 = "select p.pessoaf.id from Sessao s join s.paciente p where s.id = :id";
		
		Query query2 = em.createQuery(consulta2);
		query2.setParameter("id", id);
		Object resultado2 = (Object) query2.getSingleResult();
		
		String consulta3 = "select pr.pessoaf.id from Sessao s join s.profissional pr where s.id = :id";
		
		Query query3 = em.createQuery(consulta3);
		query3.setParameter("id", id);
		Object resultado3 = (Object) query3.getSingleResult();
		
		String consulta4 = "select c.id from Sessao s join s.clinica c where s.id = :id";
		
		Query query4 = em.createQuery(consulta4);
		query4.setParameter("id", id);
		Object resultado4 = (Object) query4.getSingleResult();
		
		String consulta5 = "select p.pressao, p.hora from Sessao s join s.pressoes p where s.id = :id";
		
		Query query5 = em.createQuery(consulta5);
		query5.setParameter("id", id);
		List<Object[]> resultado5 = (List<Object[]>) query5.getResultList();

		for (int i = 0; i < resultado.length; i++) {
			obj1.putOpt("ID", resultado[0]);
			obj1.putOpt("Peso_Entrada", resultado[1]);
			obj1.putOpt("Peso_Saida", resultado[2]);
			obj1.putOpt("Tempo_Dura��o", resultado[3]);
			obj1.putOpt("Intercorr�ncia", resultado[4]);
			obj1.putOpt("Solu��o", resultado[5]);
			obj1.putOpt("Fluxo", resultado[6]);
			obj1.putOpt("Em Transito", resultado[7]);
			obj1.putOpt("Sessao_Excepcional", resultado[8]);
			obj1.putOpt("Data", resultado[9]);
			obj1.putOpt("Hora_Entrada", resultado[10]);
			obj1.putOpt("Hora_Saida", resultado[11]);
			obj1.putOpt("Exame_Ureia", resultado[12]);
			obj1.putOpt("UltraFiltra��o", resultado[13]);
		}

		obj2.put("Paciente_ID", resultado2);
		obj3.put("Profissional_ID", resultado3);
		obj4.put("Clinica_ID", resultado4);

		objRetornado.put("Sess�o", obj1);
		objRetornado.put("Paciente", obj2);
		objRetornado.put("Profissional", obj3);
		objRetornado.put("Clinica", obj4);
		objRetornado.put("Pressoes", resultado5);

		return objRetornado;
	}
	
	public JSONObject BuscarSessoesPorPeriodo(Date datainicial, Date datafinal) throws JSONException{
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		JSONObject obj = new JSONObject();
		JSONObject objRetornado = new JSONObject();
		JSONArray obj2 = new JSONArray();
		
		String consulta = "select s.id, s.pesoEntrada, s.pesoSaida, s.tempoDuracao, s.intercorrencia, s.solucao, s.fluxo, "
				+ "s.EmTransito, s.SessaoExcepcional, s.dataSessao, s.horaEntrada, s.horaSaida, s.exameUreia, s.ultraFiltracao from Sessao s "
				+ "where s.dataSessao between :datainicial and :datafinal";

		Query query = em.createQuery(consulta);
		query.setParameter("datainicial", datainicial);
		query.setParameter("datafinal", datafinal);
		List<Object[]> resultado = (List<Object[]>) query.getResultList();
			
				obj2.put(resultado);
		
		
				for(int j=0;j<obj2.length();j++){
					for(int k=0;k<resultado.size();k++){
				obj.putOpt("ID", resultado.get(k)[0]);
				obj.putOpt("Peso_Entrada", resultado.get(k)[1]);
				obj.putOpt("Peso_Saida", resultado.get(k)[2]);
				obj.putOpt("Tempo_Dura��o", resultado.get(k)[3]);
				obj.putOpt("Intercorr�ncia", resultado.get(k)[4]);
				obj.putOpt("Solu��o", resultado.get(k)[5]);
				obj.putOpt("Fluxo", resultado.get(k)[6]);
				obj.putOpt("Em Transito", resultado.get(k)[7]);
				obj.putOpt("Sessao_Excepcional", resultado.get(k)[8]);
				obj.putOpt("Data", resultado.get(k)[9]);
				obj.putOpt("Hora_Entrada", resultado.get(k)[10]);
				obj.putOpt("Hora_Saida", resultado.get(k)[11]);
				obj.putOpt("Exame_Ureia", resultado.get(k)[12]);
				obj.putOpt("UltraFiltra��o", resultado.get(k)[13]);
				//obj2.put(resultado.size(), obj);
					
			}
			
		}	
				
		objRetornado.putOpt("Sess�es", obj2);
				
		
		return objRetornado;
		
		
	}
	
	public JSONObject BuscarSessaoPorDataEPaciente(String data, long id) throws ParseException, JSONException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		JSONObject objRetornado = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		JSONObject obj3 = new JSONObject();
		JSONObject obj4 = new JSONObject();
		Date dataS = JavaUtil.ConvertStringToDate(data);

		String consulta = "select s.id, s.pesoEntrada, s.pesoSaida, s.tempoDuracao, s.intercorrencia, s.solucao, s.fluxo, "
				+ "s.EmTransito, s.SessaoExcepcional, s.dataSessao, s.horaEntrada, s.horaSaida, s.exameUreia, s.ultraFiltracao from Sessao s "
				+ "where s.paciente.pessoaf.id = :id and s.dataSessao = :data and s.SessaoExcepcional is not true";

		Query query = em.createQuery(consulta);
		query.setParameter("id", id);
		query.setParameter("data", dataS);
		Object[] resultado = (Object[]) query.getSingleResult();

		String consulta2 = "select p.pessoaf.id from Sessao s join s.paciente p where s.paciente.pessoaf.id = :id and "
				+ "s.dataSessao = :data and s.SessaoExcepcional is not true";
		
		Query query2 = em.createQuery(consulta2);
		query2.setParameter("id", id);
		query2.setParameter("data", dataS);
		Object resultado2 = (Object) query2.getSingleResult();
		
		String consulta3 = "select pr.pessoaf.id from Sessao s join s.profissional pr where s.paciente.pessoaf.id = :id and "
				+ "s.dataSessao = :data and s.SessaoExcepcional is not true";
		
		Query query3 = em.createQuery(consulta3);
		query3.setParameter("id", id);
		query3.setParameter("data", dataS);
		Object resultado3 = (Object) query3.getSingleResult();
		
		String consulta4 = "select c.id from Sessao s join s.clinica c where s.paciente.pessoaf.id = :id and "
				+ "s.dataSessao = :data and s.SessaoExcepcional is not true";
		
		Query query4 = em.createQuery(consulta4);
		query4.setParameter("id", id);
		query4.setParameter("data", dataS);
		Object resultado4 = (Object) query4.getSingleResult();
		
		String consulta5 = "select p.pressao, p.hora from Sessao s join s.pressoes p where s.paciente.pessoaf.id = :id and "
				+ "s.dataSessao = :data and s.SessaoExcepcional is not true";
		
		Query query5 = em.createQuery(consulta5);
		query5.setParameter("id", id);
		query5.setParameter("data", dataS);
		List<Object[]> resultado5 = (List<Object[]>) query5.getResultList();

		for (int i = 0; i < resultado.length; i++) {
			obj1.putOpt("ID", resultado[0]);
			obj1.putOpt("Peso_Entrada", resultado[1]);
			obj1.putOpt("Peso_Saida", resultado[2]);
			obj1.putOpt("Tempo_Dura��o", resultado[3]);
			obj1.putOpt("Intercorr�ncia", resultado[4]);
			obj1.putOpt("Solu��o", resultado[5]);
			obj1.putOpt("Fluxo", resultado[6]);
			obj1.putOpt("Em Transito", resultado[7]);
			obj1.putOpt("Sessao_Excepcional", resultado[8]);
			obj1.putOpt("Data", resultado[9]);
			obj1.putOpt("Hora_Entrada", resultado[10]);
			obj1.putOpt("Hora_Saida", resultado[11]);
			obj1.putOpt("Exame_Ureia", resultado[12]);
			obj1.putOpt("UltraFiltra��o", resultado[13]);
		}

		obj2.put("Paciente_ID", resultado2);
		obj3.put("Profissional_ID", resultado3);
		obj4.put("Clinica_ID", resultado4);

		objRetornado.put("Sess�o", obj1);
		objRetornado.put("Paciente", obj2);
		objRetornado.put("Profissional", obj3);
		objRetornado.put("Clinica", obj4);
		objRetornado.put("Pressoes", resultado5);

		return objRetornado;	
	}
	
	public JSONObject BuscarSessaoExcepcional(long id, String data) throws ParseException, JSONException{
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		JSONObject objRetornado = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		JSONObject obj3 = new JSONObject();
		JSONObject obj4 = new JSONObject();
		Date dataS = JavaUtil.ConvertStringToDate(data);

		String consulta = "select s.id, s.pesoEntrada, s.pesoSaida, s.tempoDuracao, s.intercorrencia, s.solucao, s.fluxo, "
				+ "s.EmTransito, s.SessaoExcepcional, s.dataSessao, s.horaEntrada, s.horaSaida, s.exameUreia, s.ultraFiltracao from Sessao s "
				+ "where s.paciente.pessoaf.id = :id and s.dataSessao = :data and s.SessaoExcepcional is true";

		Query query = em.createQuery(consulta);
		query.setParameter("id", id);
		query.setParameter("data", dataS);
		Object[] resultado = (Object[]) query.getSingleResult();

		String consulta2 = "select p.pessoaf.id from Sessao s join s.paciente p where s.paciente.pessoaf.id = :id and "
				+ "s.dataSessao = :data and s.SessaoExcepcional is true";
		
		Query query2 = em.createQuery(consulta2);
		query2.setParameter("id", id);
		query2.setParameter("data", dataS);
		Object resultado2 = (Object) query2.getSingleResult();
		
		String consulta3 = "select pr.pessoaf.id from Sessao s join s.profissional pr where s.paciente.pessoaf.id = :id and "
				+ "s.dataSessao = :data and s.SessaoExcepcional is true";
		
		Query query3 = em.createQuery(consulta3);
		query3.setParameter("id", id);
		query3.setParameter("data", dataS);
		Object resultado3 = (Object) query3.getSingleResult();
		
		String consulta4 = "select c.id from Sessao s join s.clinica c where s.paciente.pessoaf.id = :id and "
				+ "s.dataSessao = :data and s.SessaoExcepcional is true";
		
		Query query4 = em.createQuery(consulta4);
		query4.setParameter("id", id);
		query4.setParameter("data", dataS);
		Object resultado4 = (Object) query4.getSingleResult();
		
		String consulta5 = "select p.pressao, p.hora from Sessao s join s.pressoes p where s.paciente.pessoaf.id = :id and "
				+ "s.dataSessao = :data and s.SessaoExcepcional is true";
		
		Query query5 = em.createQuery(consulta5);
		query5.setParameter("id", id);
		query5.setParameter("data", dataS);
		List<Object[]> resultado5 = (List<Object[]>) query5.getResultList();

		for (int i = 0; i < resultado.length; i++) {
			obj1.putOpt("ID", resultado[0]);
			obj1.putOpt("Peso_Entrada", resultado[1]);
			obj1.putOpt("Peso_Saida", resultado[2]);
			obj1.putOpt("Tempo_Dura��o", resultado[3]);
			obj1.putOpt("Intercorr�ncia", resultado[4]);
			obj1.putOpt("Solu��o", resultado[5]);
			obj1.putOpt("Fluxo", resultado[6]);
			obj1.putOpt("Em Transito", resultado[7]);
			obj1.putOpt("Sessao_Excepcional", resultado[8]);
			obj1.putOpt("Data", resultado[9]);
			obj1.putOpt("Hora_Entrada", resultado[10]);
			obj1.putOpt("Hora_Saida", resultado[11]);
			obj1.putOpt("Exame_Ureia", resultado[12]);
			obj1.putOpt("UltraFiltra��o", resultado[13]);
		}

		obj2.put("Paciente_ID", resultado2);
		obj3.put("Profissional_ID", resultado3);
		obj4.put("Clinica_ID", resultado4);

		objRetornado.put("Sess�o", obj1);
		objRetornado.put("Paciente", obj2);
		objRetornado.put("Profissional", obj3);
		objRetornado.put("Clinica", obj4);
		objRetornado.put("Pressoes", resultado5);

		return objRetornado;	
	}
	
	
	public JSONObject BuscarSessaoDoPaciente(long pessoa_id, long sessao_id) throws JSONException{
		

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		JSONObject objRetornado = new JSONObject();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		JSONObject obj3 = new JSONObject();
		JSONObject obj4 = new JSONObject();

		String consulta = "select s.id, s.pesoEntrada, s.pesoSaida, s.tempoDuracao, s.intercorrencia, s.solucao, s.fluxo, "
				+ "s.EmTransito, s.SessaoExcepcional, s.dataSessao, s.horaEntrada, s.horaSaida, s.exameUreia, s.ultraFiltracao from Sessao s "
				+ "where s.paciente.pessoaf.id = :pessoa_id and s.id = :sessao_id";

		Query query = em.createQuery(consulta);
		query.setParameter("pessoa_id", pessoa_id);
		query.setParameter("sessao_id", sessao_id);
		Object[] resultado = (Object[]) query.getSingleResult();

		String consulta2 = "select p.pessoaf.id from Sessao s join s.paciente p where s.paciente.pessoaf.id = :pessoa_id and "
				+ "s.id = :sessao_id";
		
		Query query2 = em.createQuery(consulta2);
		query2.setParameter("pessoa_id", pessoa_id);
		query2.setParameter("sessao_id", sessao_id);
		Object resultado2 = (Object) query2.getSingleResult();
		
		String consulta3 = "select pr.pessoaf.id from Sessao s join s.profissional pr where s.paciente.pessoaf.id = :pessoa_id "
				+ "and s.id = :sessao_id";
		
		Query query3 = em.createQuery(consulta3);
		query3.setParameter("pessoa_id", pessoa_id);
		query3.setParameter("sessao_id", sessao_id);
		Object resultado3 = (Object) query3.getSingleResult();
		
		String consulta4 = "select c.id from Sessao s join s.clinica c where s.paciente.pessoaf.id = :pessoa_id and s.id = :sessao_id";
		
		Query query4 = em.createQuery(consulta4);
		query4.setParameter("pessoa_id", pessoa_id);
		query4.setParameter("sessao_id", sessao_id);
		Object resultado4 = (Object) query4.getSingleResult();
		
		String consulta5 = "select p.pressao, p.hora from Sessao s join s.pressoes p where s.paciente.pessoaf.id = :pessoa_id "
				+ "and s.id = :sessao_id";
		
		Query query5 = em.createQuery(consulta5);
		query5.setParameter("pessoa_id", pessoa_id);
		query5.setParameter("sessao_id", sessao_id);
		List<Object[]> resultado5 = (List<Object[]>) query5.getResultList();

		for (int i = 0; i < resultado.length; i++) {
			obj1.putOpt("ID", resultado[0]);
			obj1.putOpt("Peso_Entrada", resultado[1]);
			obj1.putOpt("Peso_Saida", resultado[2]);
			obj1.putOpt("Tempo_Dura��o", resultado[3]);
			obj1.putOpt("Intercorr�ncia", resultado[4]);
			obj1.putOpt("Solu��o", resultado[5]);
			obj1.putOpt("Fluxo", resultado[6]);
			obj1.putOpt("Em Transito", resultado[7]);
			obj1.putOpt("Sessao_Excepcional", resultado[8]);
			obj1.putOpt("Data", resultado[9]);
			obj1.putOpt("Hora_Entrada", resultado[10]);
			obj1.putOpt("Hora_Saida", resultado[11]);
			obj1.putOpt("Exame_Ureia", resultado[12]);
			obj1.putOpt("UltraFiltra��o", resultado[13]);
		}

		obj2.put("Paciente_ID", resultado2);
		obj3.put("Profissional_ID", resultado3);
		obj4.put("Clinica_ID", resultado4);

		objRetornado.put("Sess�o", obj1);
		objRetornado.put("Paciente", obj2);
		objRetornado.put("Profissional", obj3);
		objRetornado.put("Clinica", obj4);
		objRetornado.put("Pressoes", resultado5);

		return objRetornado;	
		
	}

	public int AtualizarSessao(String sessao_id, String peso_entrada, String peso_saida, String tempo_duracao, String intercorrencia, 
			String solucao, String fluxo, String emtransito, String Sessao_Excepcional, String data_sessao, String horaentrada, String horasaida, 
			JSONArray sistolicas, JSONArray diastolicas, JSONArray hora, String ureiapre, String ureiapos, 
			String paciente_id, String profissional_id, String clinica_id){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Sessao s = new Sessao();
		Clinica c = new Clinica();
		Paciente p = new Paciente();
		ExameUreia eu = new ExameUreia();
		Profissional m = new Medico();
		Profissional e = new Enfermeira();
		List<Sessao> s_list = new ArrayList<Sessao>();
		List<Profissional> prof_list = new ArrayList<Profissional>();
		List<Paciente> pac_list = new ArrayList<Paciente>();
		List<Clinica> c_list = new ArrayList<Clinica>();
		List<BigDecimal> sistolicas_list = new ArrayList<BigDecimal>();
		List<BigDecimal> diastolicas_list = new ArrayList<BigDecimal>();
		List<Date> horas_list = new ArrayList<Date>();
		List<String> string_sist_list = new ArrayList<String>();
		List<String> string_diast_list = new ArrayList<String>();
		List<String> string_hora_list = new ArrayList<String>();
		long pac_id = JavaUtil.ConvertStringToLong(paciente_id);
		long prof_id = JavaUtil.ConvertStringToLong(profissional_id);
		long c_id = JavaUtil.ConvertStringToLong(clinica_id);
		long s_id = JavaUtil.ConvertStringToLong(sessao_id);
		long clinica_id_pac = 0;
		Number n2 = 0, n3 = 0;
		int count = 0;
		boolean transito = true;
		boolean Notransito = false;
		
		try{
			
			TypedQuery<Sessao> query = em.createNamedQuery(Sessao.BuscarPorID, Sessao.class);
			query.setParameter("id", s_id);
			s_list = query.getResultList();
			
			String consulta2 = "select p from Profissional p where p.pessoaf.id = :id";
			TypedQuery<Profissional> query2 = em.createQuery(consulta2, Profissional.class);
			query2.setParameter("id", prof_id);
			prof_list = query2.getResultList();
			
			String consulta3 = "select count(m) from Medico m where m.pessoaf.id = :id";
			TypedQuery<Number> query3 = em.createQuery(consulta3, Number.class);
			query3.setParameter("id", prof_id);
			n2 = query3.getSingleResult();
			
			String consulta4 = "select count(e) from Enfermeira e where e.pessoaf.id = :id";
			TypedQuery<Number> query4 = em.createQuery(consulta4, Number.class);
			query4.setParameter("id", prof_id);
			n3 = query4.getSingleResult();
			
			String consulta5 = "select p from Paciente p where p.pessoaf.id = :id";
			TypedQuery<Paciente> query5 = em.createQuery(consulta5, Paciente.class);
			query5.setParameter("id", pac_id);
			pac_list = query5.getResultList();
			
			TypedQuery<Clinica> query6 = em.createNamedQuery(Clinica.BuscarPorID, Clinica.class);
			query6.setParameter("id", c_id);
			c_list = query6.getResultList();
			
			if(s_list.isEmpty()){
				
				System.out.println("Sess�o N�O Encontrada! N�o � possivel Atualizar a Sess�o!");
				return 404;
			}else{
				
				Iterator<Sessao> itr = s_list.iterator();
				for(int i=0;i<s_list.size();i++){
					while(itr.hasNext()){
						s = itr.next();
					}
				}
			}
				
			
			if(c_list.isEmpty()){
				System.out.println("Clinica n�o encontrada! Cadastre antes de atualizar uma Sess�o!");
				return 404;
				
			}else{
				
				Iterator<Clinica> itr = c_list.iterator();
				
				for(int i=0;i<c_list.size();i++){
						while(itr.hasNext()){
							c = itr.next();
						}
				}
			}
			
			if(pac_list.isEmpty()){
				
				System.out.println("Paciente N�O encontrado no BD! Cadastre antes de atualizar uma Sess�o!");
				return 4041;
			
			}else{
				
				Iterator<Paciente> itr = pac_list.iterator();
				
				for(int i=0;i<pac_list.size();i++){
						while(itr.hasNext()){
							p = itr.next();
							clinica_id_pac = p.getClinica().getId();
						}
				}
			}
			
			if(prof_list.isEmpty()){
				
				System.out.println("Profissional N�O encontrado no BD! Cadastre antes de atualizar uma Sess�o!");
				return 404;
			
			}else{
			
				if(n2.intValue()>=1){
				
				
					Iterator<Profissional> itr = prof_list.iterator();
					
					for(int i=0;i<prof_list.size();i++){
							while(itr.hasNext()){
								m = itr.next();
								count = 1;
								}
					}
				}
				
				if(n3.intValue()>=1){
					

					Iterator<Profissional> itr = prof_list.iterator();
					
					for(int i=0;i<prof_list.size();i++){
							while(itr.hasNext()){
									e = itr.next();
									count = 2;
							}
					}
				}
			}
				
	}catch(Exception ex){
		ex.printStackTrace();
		System.out.println("Erro ao fazer busca no BD");
	}
		
		try{
			for(int i = 0; i<sistolicas.length(); i++){
				
				string_sist_list.add(sistolicas.getString(i));
			}
			
			Iterator<String> itr = string_sist_list.iterator();
			for(int i=0; i<string_sist_list.size();i++){
				while(itr.hasNext()){
				
				BigDecimal bd = JavaUtil.ConvertStringToBigDecimal(itr.next());
				sistolicas_list.add(bd);
			}
			}
						
			}catch(JSONException jex){
				
				jex.printStackTrace();
			
			}
		
		try{
			for(int i = 0; i<diastolicas.length(); i++){
				
				string_diast_list.add(diastolicas.getString(i));
			}
			
			Iterator<String> itr = string_diast_list.iterator();
			for(int i=0; i<string_diast_list.size();i++){
				while(itr.hasNext()){
				BigDecimal bd = JavaUtil.ConvertStringToBigDecimal(itr.next());
				diastolicas_list.add(bd);
			}
			}
						
			}catch(JSONException jex){
				
				jex.printStackTrace();
			
			}
		
		try{
			for(int i = 0; i<hora.length(); i++){
				
				string_hora_list.add(hora.getString(i));
			}
			
			Iterator<String> itr = string_hora_list.iterator();
			for(int i=0; i<string_hora_list.size();i++){
				while(itr.hasNext()){
				Date h = JavaUtil.ConvertStringToHora(itr.next());
				horas_list.add(h);
			}
			}
						
			}catch(JSONException jex){
				
				jex.printStackTrace();
			
			}
		
		/**
		 * Registra a Press�o Arterial
		 */
		try{
			
			Iterator<BigDecimal> itr_sist = sistolicas_list.iterator();
			Iterator<BigDecimal> itr_diast = diastolicas_list.iterator();
			Iterator<Date> itr_h = horas_list.iterator();
			
			for(int i=0; i<sistolicas_list.size(); i++){
				for(int j=0; j<diastolicas_list.size(); j++){
					for(int k=0; k<horas_list.size(); k++){
						while(itr_sist.hasNext()){
							while(itr_diast.hasNext()){
								while(itr_h.hasNext()){
									BigDecimal bd1 = itr_sist.next();
									BigDecimal bd2 = itr_diast.next();
									Date h = itr_h.next();
									s.RegistraPressaoArterial(bd1, bd2, h);
								}
							}
						}
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao Registrar as Press�es Arteriais");
		}
		
		try{
			if(count == 1){
			
				BigDecimal ureiaPre = JavaUtil.ConvertStringToBigDecimal(ureiapre);
				BigDecimal ureiaPos = JavaUtil.ConvertStringToBigDecimal(ureiapos);
				BigDecimal pesoEntrada = JavaUtil.ConvertStringToBigDecimal(peso_entrada);
				BigDecimal pesoSaida = JavaUtil.ConvertStringToBigDecimal(peso_saida);
				BigDecimal tempo = JavaUtil.ConvertStringToBigDecimal(tempo_duracao);
			
				Date dataS = JavaUtil.ConvertStringToDate(data_sessao);
			
				Date horaEntrada = JavaUtil.ConvertStringToHora(horaentrada);
				Date horaSaida = JavaUtil.ConvertStringToHora(horasaida);
			
				int fluxoBomba = JavaUtil.ConvertStringToInt(fluxo);
			
				/**
				 * Se o id da Clinica do PACIENTE for = ao id da Clinica passado por parametro...
				 * ent�o o Paciente Esta fazendo dialise na sua Clinica, ou seja, N�O ESTA FAZENDO "EM TRANSITO"
				 * Caso contrario ele esta FORA da sua Clinica, ou seja, ESTA FAZENDO DIALISE "EM TRANSITO"
				 */
				if(clinica_id_pac == c_id){
					
					s.setEmTransito(Notransito);
				}else{
					
					s.setEmTransito(transito);
				}
				
				if((Sessao_Excepcional.equals("falso")) || (Sessao_Excepcional.equals("Falso"))){
					boolean sessaoExcepcional = false;
					s.setSessaoExcepcional(sessaoExcepcional);
				}else{
					boolean sessaoExcepcional = true;
					s.setSessaoExcepcional(sessaoExcepcional);
				}
			
				eu.setUreiaPre(ureiaPre);
				eu.setUreiaPos(ureiaPos);
				
				s.setIntercorrencia(intercorrencia);
				s.setDataSessao(dataS);
				s.setExameUreia(eu);
				s.setFluxo(fluxoBomba);
				s.setHoraEntrada(horaEntrada);
				s.setHoraSaida(horaSaida);
				s.setPressoes(s.getPr());
				s.setSolucao(solucao);
				s.setPaciente(p);
				p.getSessoes().add(s);
				s.setProfissional(m);
				s.setClinica(c);
				s.setTempoDuracao(tempo);
				s.setPesoEntrada(pesoEntrada);
				s.setPesoSaida(pesoSaida);
				s.CalcularUltrafiltracao();
				s.setUltraFiltracao(s.getUltraFiltracao());
			}
			
			if(count == 2){
				
				BigDecimal ureiaPre = JavaUtil.ConvertStringToBigDecimal(ureiapre);
				BigDecimal ureiaPos = JavaUtil.ConvertStringToBigDecimal(ureiapos);
				BigDecimal pesoEntrada = JavaUtil.ConvertStringToBigDecimal(peso_entrada);
				BigDecimal pesoSaida = JavaUtil.ConvertStringToBigDecimal(peso_saida);
				BigDecimal tempo = JavaUtil.ConvertStringToBigDecimal(tempo_duracao);
			
				Date dataS = JavaUtil.ConvertStringToDate(data_sessao);
			
				Date horaEntrada = JavaUtil.ConvertStringToHora(horaentrada);
				Date horaSaida = JavaUtil.ConvertStringToHora(horasaida);
			
				int fluxoBomba = JavaUtil.ConvertStringToInt(fluxo);
			
				/**
				 * Se o id da Clinica do PACIENTE for = ao id da Clinica passado por parametro...
				 * ent�o o Paciente Esta fazendo dialise na sua Clinica, ou seja, N�O ESTA FAZENDO "EM TRANSITO"
				 * Caso contrario ele esta FORA da sua Clinica, ou seja, ESTA FAZENDO DIALISE "EM TRANSITO"
				 */
				if(clinica_id_pac == c_id){
					
					s.setEmTransito(Notransito);
				}else{
					
					s.setEmTransito(transito);
				}
				
				if((Sessao_Excepcional.equals("falso")) || (Sessao_Excepcional.equals("Falso"))){
					boolean sessaoExcepcional = false;
					s.setSessaoExcepcional(sessaoExcepcional);
				}else{
					boolean sessaoExcepcional = true;
					s.setSessaoExcepcional(sessaoExcepcional);
				}
			
				eu.setUreiaPre(ureiaPre);
				eu.setUreiaPos(ureiaPos);
			
				s.setIntercorrencia(intercorrencia);
				s.setDataSessao(dataS);
				s.setExameUreia(eu);
				s.setFluxo(fluxoBomba);
				s.setHoraEntrada(horaEntrada);
				s.setHoraSaida(horaSaida);
				s.setPressoes(s.getPr());
				s.setSolucao(solucao);
				s.setPaciente(p);
				p.getSessoes().add(s);
				s.setProfissional(e);
				s.setClinica(c);
				s.setTempoDuracao(tempo);
				s.setPesoEntrada(pesoEntrada);
				s.setPesoSaida(pesoSaida);
				s.CalcularUltrafiltracao();
				s.setUltraFiltracao(s.getUltraFiltracao());
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao setar os dados");
		}
		
		try{
			em.getTransaction().begin();

			em.merge(s);

			em.getTransaction().commit();

		}catch(Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
		
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Sess�o Atualizado Com Sucesso!!!");
		return 200;

	}

	public int RemoverSessao(long id){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Sessao s = null;
		List<Sessao> s_list = new ArrayList<Sessao>();
		
		try{
			
			TypedQuery<Sessao> query = em.createNamedQuery(Sessao.BuscarPorID, Sessao.class);
			query.setParameter("id", id);
			s_list = query.getResultList();
			
			if(s_list.isEmpty()){
				
				System.out.println("Sess�o N�O encontrada! N�o � possivel Remover");
				return 404;
			}else{
				
				Iterator<Sessao> itr = s_list.iterator();
				for(int i=0;i<s_list.size();i++){
					while(itr.hasNext()){
						s = itr.next();
					}
				}
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao buscar a Sess�o do BD");
		}
		
			try { 

			em.getTransaction().begin();
			em.remove(s); 
			em.getTransaction().commit(); 

		} catch (Exception ex) { 

			ex.printStackTrace(); 
			em.getTransaction().rollback(); 

		}finally{
			
			em.close();
			factory.close();
		} 

		System.out.println("Sess�o Removido Com Sucesso!!!");
		return 200;
	}

	public int RemoverSessaoDoPacientePorData(long id, String data) throws ParseException {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Sessao s = null;
		Date dataS = JavaUtil.ConvertStringToDate(data);
		
		try { 

			String consulta = "select s from Sessao s where s.dataSessao = :data and s.paciente.pessoaf.id = :id";
			TypedQuery<Sessao> query = em.createQuery(consulta, Sessao.class);
			query.setParameter("data", dataS);
			query.setParameter("id", id);
			List<Sessao> resultado = query.getResultList();
			
			if(resultado.isEmpty()){
				
				System.out.println("Sess�o do Paciente N�O econtrada nesta data! N�o � possivel remover!");
				return 404;
			}
			
			if(resultado.size() == 1){
				
				Iterator<Sessao> itr = resultado.iterator();
				for(int i=0;i<resultado.size();i++){
					while(itr.hasNext()){
						s = itr.next();
					}
				}
				
			}else{
				
				System.out.println("O Paciente possui mais de uma sess�o nesta data! "
						+ "N�o � possivel remover mais de uma sess�o ao mesmo tempo");
				return 401;
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao buscar a Sess�o");
		}
	
	try{
		em.getTransaction().begin();
		em.remove(s); 
		em.getTransaction().commit(); 

	} catch (Exception ex) { 

		ex.printStackTrace(); 
		em.getTransaction().rollback(); 

	}finally{
		
		em.close();
		factory.close();
	} 

	System.out.println("Sess�o do Paciente feito na Data" + " " +data+" "+ "foi Removido Com Sucesso!!!");
	return 200;
	}
	
	public int RemoverSessaoDoPaciente(long pessoa_id, long sessao_id) throws ParseException {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Sessao s = null;
		
		try { 

			String consulta = "select s from Sessao s where s.paciente.pessoaf.id = :pessoa_id and s.id = :sessao_id  ";
			TypedQuery<Sessao> query = em.createQuery(consulta, Sessao.class);
			query.setParameter("pessoa_id", pessoa_id);
			query.setParameter("sessao_id", sessao_id);
			List<Sessao> resultado = query.getResultList();
			
			if(resultado.isEmpty()){
				
				System.out.println("Sess�o do Paciente N�O econtrada! N�o � possivel remover!");
				return 404;
			}else{
				
				Iterator<Sessao> itr = resultado.iterator();
				for(int i=0;i<resultado.size();i++){
					while(itr.hasNext()){
						s = itr.next();
					}
				}
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao buscar a Sess�o");
		}
	
	try{
		em.getTransaction().begin();
		em.remove(s); 
		em.getTransaction().commit(); 

	} catch (Exception ex) { 

		ex.printStackTrace(); 
		em.getTransaction().rollback(); 

	}finally{
		
		em.close();
		factory.close();
	} 

	System.out.println("Sess�o do Paciente foi Removido Com Sucesso!!!");
	return 200;
	}

	@Override
	public List<Sessao> BuscarSessaoCalculoMedia(long id_paciente, Date dataI, Date dataF) {
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		String consulta = "select s from Sessao s where s.paciente.pessoaf.id = :id_pac and s.dataSessao between :dataInicial and :dataFinal)";
		TypedQuery<Sessao> query = em.createQuery(consulta, Sessao.class);
		query.setParameter("id_pac", id_paciente);
		query.setParameter("dataInicial", dataI);
		query.setParameter("dataFinal", dataF);
		List<Sessao> resultado = query.getResultList();

		return resultado;
	}
}


package br.com.DialiSys.Manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import br.com.DialiSys.Model.CNPJ;
import br.com.DialiSys.Model.Clinica;
import br.com.DialiSys.Model.Endereco;
import br.com.DialiSys.Util.JavaUtil;

public class ClinicaManager {

	public static long idGerado = 0;
	
	public int CadastrarClinica(String nome, String tel, String email, String site,
			String rua, String numero, String complemento, String bairro, String cidade, String uf, 
			String pais, String cep, String numero_cnpj, String data_emissao_cnpj) throws ParseException{

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Clinica c = new Clinica();
		Endereco e = new Endereco();
		CNPJ cnpj = new CNPJ();
		
		try{
			String consulta = "select count(c) from Clinica c where c.cnpj.numero = :numero";
			TypedQuery<Number> query = em.createQuery(consulta, Number.class);
			query.setParameter("numero", numero_cnpj);
			Number c2 = query.getSingleResult();
			
			if(c2.intValue()<1){
				
				e.setRua(rua);
				e.setNumero(numero);
				e.setComplemento(complemento);
				e.setBairro(bairro);
				e.setCidade(cidade);
				e.setUf(uf);
				e.setPais(pais);
				e.setCep(cep);
				
				Date data = JavaUtil.ConvertStringToDate(data_emissao_cnpj);
				
				if(cnpj.validaCNPJ(numero_cnpj) == true){
				
					cnpj.setDataEmissao(data);
					cnpj.setNumero(numero_cnpj);
				
				}else{
					
					System.out.println("CNPJ digitado é INVÁLIDO! Por Favor insira o CNPJ corretamente!");
					return 400;
				}
				
				c.setNome(nome);
				c.setEmail(email);
				c.setTel(tel);
				c.setEndereco(e);
				c.setCnpj(cnpj);
				c.setSite(site);
				
			}else{
				System.out.println("Conflito! Erro 409 - Clinica ja EXISTE!");
				return 409;
			}
		}catch(Exception ex){
				
				ex.printStackTrace();
				System.out.println("Erro ao Buscar Clinica no BD!");
			}
		
		try{

			em.getTransaction().begin();
			em.persist(c);
			idGerado = c.getId();
			em.getTransaction().commit();

		}catch (Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
			return 500;

		}finally{
			
			em.close();
			factory.close();
		}
			
		
		System.out.println("Nova Clínica Cadastrada Com Sucesso!!!");
		return 201;

	}

	public List<Clinica> BuscarTodasClinicas(){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		String consulta = "select c from Clinica c";
		TypedQuery<Clinica> query = em.createQuery(consulta, Clinica.class);
		List<Clinica> resultado = query.getResultList();
		return resultado;	
	}
	
	public Clinica BuscarClinicaPorID(long id){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		TypedQuery<Clinica> query = em.createNamedQuery(Clinica.BuscarPorID, Clinica.class);
		query.setParameter("id", id);
		Clinica resultado = query.getSingleResult();
		return resultado;	
	}


	public Clinica BuscarClinicaPorCNPJ(String cnpj){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		TypedQuery<Clinica> query = em.createNamedQuery(Clinica.BuscarPorCNPJ, Clinica.class);
		query.setParameter("numero_cnpj", cnpj);
		Clinica resultado = query.getSingleResult();
		return resultado;	
	}
	
	public int AtualizarClinica(String clinica_id, String nome, String tel, String email, String site,
			String rua, String numero, String complemento, String bairro, String cidade, String uf, 
			String pais, String cep, String numero_cnpj, String data_emissao_cnpj){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Clinica c = new Clinica();
		Endereco e = new Endereco();
		CNPJ cnpj = new CNPJ();
		long cl_id = JavaUtil.ConvertStringToLong(clinica_id);
		
		try{
			c = em.find(Clinica.class, cl_id);
			if((c.getCnpj().getNumero() == null) || (c.getCnpj().getNumero().equals(""))) {
				
				System.out.println("Clinica NÃO econtrada no BD");
				return 404;
			}else{
				
				Date datacnpj = JavaUtil.ConvertStringToDate(data_emissao_cnpj);
				
				if(cnpj.validaCNPJ(numero_cnpj) == true){
					
					cnpj.setDataEmissao(datacnpj);
					cnpj.setNumero(numero_cnpj);
				
				}else{
					
					System.out.println("CNPJ digitado é INVÁLIDO! Por Favor insira o CNPJ corretamente!");
					return 400;
				}
				
				e.setRua(rua);
				e.setNumero(numero);
				e.setComplemento(complemento);
				e.setCidade(cidade);
				e.setUf(uf);
				e.setPais(pais);
				e.setBairro(bairro);
				e.setCep(cep);
				
				c.setNome(nome);
				c.setEmail(email);
				c.setSite(site);
				c.setTel(tel);
				c.setCnpj(cnpj);
				c.setEndereco(e);
			}
		
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao buscar a Clinica no BD");
		}
		
		
		try{
			em.getTransaction().begin();

			em.merge(c);

			em.getTransaction().commit();

		}catch(Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();

		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Clínica Atualizado Com Sucesso!!!");
		return 200;
	}

	public int RemoverClinica(long id) {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		Clinica c = null;
		List<Clinica> lista_cl = new ArrayList<Clinica>();
		
		
		try { 

			TypedQuery<Clinica> query = em.createNamedQuery(Clinica.BuscarPorID, Clinica.class);
			query.setParameter("id", id);
			lista_cl = query.getResultList();
			
			if(lista_cl.isEmpty()){
				
				String msgErro = "Status: 404 Not Found - Clinica Não Encontrada no BD! ";
				System.out.println(msgErro);
				return 404;
				
			}else{
				
				Iterator<Clinica> itr = lista_cl.iterator();
				
				for(int i = 0; i<lista_cl.size(); i++){
					try{
						while(itr.hasNext()){
						c = em.find(Clinica.class, itr.next().getId());
						}
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.println("Erro ao buscar os dados");
					}
				}
			}
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao buscar a Clinica");
		}

			
		try{
			em.getTransaction().begin();
			em.remove(c);
			em.getTransaction().commit();
			
		} catch (Exception ex) { 

			ex.printStackTrace();
			em.getTransaction().rollback();
			
		}finally{
			
			em.close();
			factory.close();
		}


		System.out.println("Clínica Removido Com Sucesso!!!");
		return 200;
	}
}

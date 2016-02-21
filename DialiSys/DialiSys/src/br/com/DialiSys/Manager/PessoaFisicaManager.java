package br.com.DialiSys.Manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import br.com.DialiSys.Model.CPF;
import br.com.DialiSys.Model.Endereco;
import br.com.DialiSys.Model.Enfermeira;
import br.com.DialiSys.Model.Medico;
import br.com.DialiSys.Model.Paciente;
import br.com.DialiSys.Model.PessoaFisica;
import br.com.DialiSys.Model.Usuario;
import br.com.DialiSys.Util.JavaUtil;

public class PessoaFisicaManager {
	
	public static long IDGerado;
	
	public int CadastrarPessoaFisica(String nome, String tel, String email, String sexo, String datanasc,
			String numero_cpf, String data_emissao_cpf, String rua, String numero, String complemento, String bairro, 
			String cidade, String uf, String pais, String cep){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		PessoaFisica pf = new PessoaFisica();
		Endereco endereco = new Endereco();
		CPF cpf = new CPF();
		Number n;
		boolean masc = true;
		boolean fem = false;
		
		try{
			
			String consulta = "select count(pf) from PessoaFisica pf where pf.cpf.numero = :numero";
			TypedQuery<Number> query = em.createQuery(consulta, Number.class);
			query.setParameter("numero", numero_cpf);
			n = query.getSingleResult();
			
			if(n.intValue()>=1){
				
				System.out.println("Pessoa Fisica ja Existe! Não é possivel cadastrar!");
				return 409;
				
			}else{
				
				Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
				Date datan = JavaUtil.ConvertStringToDate(datanasc);
				
				if((sexo.equals("Masculino")) || (sexo.equals("masculino"))){
					
					pf.setSexo(masc);
				}
				if((sexo.equals("Feminino")) || (sexo.equals("feminino"))){
					
					pf.setSexo(fem);
				}
				
				if(cpf.validaCPF(numero_cpf) == true){
					
					cpf.setDataEmissao(datacpf);
					cpf.setNumero(numero_cpf);
				}else{
					
					System.out.println("CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!");
					return 400;
				}
				
				endereco.setBairro(bairro);
				endereco.setCep(cep);
				endereco.setCidade(cidade);
				endereco.setComplemento(complemento);
				endereco.setNumero(numero);
				endereco.setPais(pais);
				endereco.setRua(rua);
				endereco.setUf(uf);
				
				pf.setNome(nome);
				pf.setTel(tel);
				pf.setEndereco(endereco);
				pf.setEmail(email);
				pf.setCpf(cpf);
				pf.setDatanasc(datan);	
			}
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao setar os dados");
		}
		
		try{

			em.getTransaction().begin();
			em.persist(pf);
			IDGerado = pf.getId();
			em.getTransaction().commit();

		}catch (Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
			
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("Nova PessoaFisica Cadastrada Com Sucesso!!!");
		return 201;

	}

	public List<PessoaFisica> BuscarTodosPessoasFisicas(){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		String consulta = "select pf from PessoaFisica pf";
		TypedQuery<PessoaFisica> query = em.createQuery(consulta, PessoaFisica.class);
		List<PessoaFisica> resultado = query.getResultList();
		return resultado;	
	}
	
	
	public PessoaFisica BuscarPorID(long id){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		TypedQuery<PessoaFisica> query = em.createNamedQuery(PessoaFisica.BuscarPorID, PessoaFisica.class);
				query.setParameter("id", id);
				PessoaFisica resultado = query.getSingleResult();
				return resultado;
	}
	
	public PessoaFisica BuscarPorCPF(String numero){
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		TypedQuery<PessoaFisica> query = em.createNamedQuery(PessoaFisica.BuscarPorCPF, PessoaFisica.class);
		query.setParameter("numero_cpf", numero);
		PessoaFisica resultado = query.getSingleResult();
		return resultado;
	}
	

	public int AtualizarPessoaFisica(String id, String nome, String tel, String email, String sexo, String datanasc,
			String numero_cpf, String data_emissao_cpf, String rua, String numero, String complemento, String bairro, 
			String cidade, String uf, String pais, String cep){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();
		
		PessoaFisica pf = new PessoaFisica();
		Endereco endereco = new Endereco();
		CPF cpf = new CPF();
		List<PessoaFisica> pf_list = new ArrayList<PessoaFisica>();
		boolean masc = true;
		boolean fem = false;
		long pessoa_id = JavaUtil.ConvertStringToLong(id);
		
		try{
			
			TypedQuery<PessoaFisica> query = em.createNamedQuery(PessoaFisica.BuscarPorID, PessoaFisica.class);
			query.setParameter("id", pessoa_id);
			pf_list = query.getResultList();
			
			if(pf_list.isEmpty()){
				
				System.out.println("Pessoa Fisica NÃO Existe! Não é possivel atualizar!");
				return 404;
				
			}else{
				
				Iterator<PessoaFisica> itr = pf_list.iterator();
				for(int i=0; i<pf_list.size(); i++){
					while(itr.hasNext()){
						pf = itr.next();
					}
				}
			}
		}catch(Exception ex){
		
			ex.printStackTrace();
			System.out.println("Erro ao buscar Pessoa Fisica no BD");
		}
		
		try{
					
				Date datacpf = JavaUtil.ConvertStringToDate(data_emissao_cpf);
				Date datan = JavaUtil.ConvertStringToDate(datanasc);
				
				if((sexo.equals("Masculino")) || (sexo.equals("masculino"))){
					
					pf.setSexo(masc);
				}
				if((sexo.equals("Feminino")) || (sexo.equals("feminino"))){
					
					pf.setSexo(fem);
				}
				
				if(cpf.validaCPF(numero_cpf) == true){
					
					cpf.setDataEmissao(datacpf);
					cpf.setNumero(numero_cpf);
				}else{
					
					System.out.println("CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!");
					return 400;
				}
				
				endereco.setBairro(bairro);
				endereco.setCep(cep);
				endereco.setCidade(cidade);
				endereco.setComplemento(complemento);
				endereco.setNumero(numero);
				endereco.setPais(pais);
				endereco.setRua(rua);
				endereco.setUf(uf);
				
				pf.setNome(nome);
				pf.setTel(tel);
				pf.setEndereco(endereco);
				pf.setEmail(email);
				pf.setCpf(cpf);
				pf.setDatanasc(datan);	
			
		}catch(Exception ex){
			
			ex.printStackTrace();
			System.out.println("Erro ao setar os dados");
		}
		
		try{
			em.getTransaction().begin();

			em.merge(pf);

			em.getTransaction().commit();

		}catch(Exception ex) {

			ex.printStackTrace();
			em.getTransaction().rollback();
			
		}finally{
			
			em.close();
			factory.close();
		}

		System.out.println("PessoaFisica Atualizado Com Sucesso!!!");
		return 200;

	}

	public int RemoverPessoaFisica(long id){

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("DialiSys");
		EntityManager em = factory.createEntityManager();

		PessoaFisica pf = null;
		List<PessoaFisica> pf_list = new ArrayList<PessoaFisica>();
		int count = 0;
		
		try{
			
			TypedQuery<PessoaFisica> query = em.createNamedQuery(PessoaFisica.BuscarPorID, PessoaFisica.class);
			query.setParameter("id", id);
			pf_list = query.getResultList();
			
			if(pf_list.isEmpty()){
				
				System.out.println("Pessoa Fisica NÃO Existe! Não é possivel atualizar!");
				return 404;
				
			}else{
				
				Iterator<PessoaFisica> itr = pf_list.iterator();
				for(int i=0; i<pf_list.size(); i++){
					while(itr.hasNext()){
						pf = itr.next();
					}
				}
			}
		}catch(Exception ex){
		
			ex.printStackTrace();
			System.out.println("Erro ao buscar Pessoa Fisica no BD");
		}
		
		try{
			
			String consulta = "select p from Paciente p where p.pessoaf.id = :id_pessoa";
			TypedQuery<Paciente> query = em.createQuery(consulta, Paciente.class);
			query.setParameter("id_pessoa", id);
			List<Paciente> pac_list = query.getResultList();
			
			String consulta2 = "select u from Usuario u where u.pessoaf.id = :id_pessoa";
			TypedQuery<Usuario> query2 = em.createQuery(consulta2, Usuario.class);
			query2.setParameter("id_pessoa", id);
			List<Usuario> user_list = query2.getResultList();
			
			String consulta3 = "select e from Enfermeira e where e.pessoaf.id = :id_pessoa";
			TypedQuery<Enfermeira> query3 = em.createQuery(consulta3, Enfermeira.class);
			query3.setParameter("id_pessoa", id);
			List<Enfermeira> e_list = query3.getResultList();
			
			String consulta4 = "select m from Medico m where m.pessoaf.id = :id_pessoa";
			TypedQuery<Medico> query4 = em.createQuery(consulta4, Medico.class);
			query4.setParameter("id_pessoa", id);
			List<Medico> m_list = query4.getResultList();
			
			if((pac_list.isEmpty()) && (user_list.isEmpty()) && (e_list.isEmpty()) && (m_list.isEmpty())){
				
				count = 1;
			}else{
				
				System.out.println("A Pessoa NÃO pode ser Removida pois está sendo usada em outra Tabela do BD");
				return 400;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao buscar Pessoa nos papeis");
		}
		
		try { 

			if(count == 1){
			em.getTransaction().begin();
			em.remove(pf); 
			em.getTransaction().commit();
			}

		} catch (Exception ex) { 

			ex.printStackTrace(); 
			em.getTransaction().rollback(); 

		}finally{
			
			em.close();
			factory.close();
		} 

		System.out.println("PessoaFisica Removido Com Sucesso!!!");
		return 200;
	}
}


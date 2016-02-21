package br.com.DialiSys.Resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.DialiSys.Manager.EnfermeiraManager;

import com.google.gson.Gson;

@Path("/enfermeiras")
public class EnfermeiraResource {
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response CadastrarEnfermeira(String dados) throws JSONException{
		
		EnfermeiraManager efmg = new EnfermeiraManager();
		JSONObject obj = new JSONObject(dados);
		int cod_http = 0;
		long id = 0;

		
		try{
			cod_http =  efmg.CadastrarEnfermeira(obj.optString("nome"), obj.optString("tel"), obj.optString("email"), 
					obj.optString("sexo"), obj.optString("datanasc"),obj.optString("numero_cpf"), obj.optString("data_emissao_cpf"), obj.optString ("rua"),
					obj.optString("numero"), obj.optString("complemento"), obj.optString("bairro"), 
					obj.optString("cidade"), obj.optString("uf"), obj.optString("pais"), obj.optString("cep"), 
					obj.optJSONArray("clinicas_ids"), obj.optString("numero_coren"), obj.optString("data_emissao_coren"));
			
			if(cod_http == 201){
				
				id = EnfermeiraManager.idGerado;
			}
			if(cod_http == 409){
				String msg_erro = "Status: 409 Conflito! Enfermeira ja EXISTE!";
				return Response.status(409).entity(msg_erro).build();
			}
			
			if(cod_http == 400){
				String msg_erro = "Status: 400 Bad Request - CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!";
				return Response.status(400).entity(msg_erro).build();
			}
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Erro ao Gravar os Dados no BD");
				
			}
			
			String msg = "Status: 201 CREATED - Enfermeira Cadastrada Com Sucesso!";
			System.out.println(msg);
			try{
				return Response.created(new URI(""+id)).build();
				}catch(URISyntaxException e){
					throw new RuntimeException(e);
				}
	}
	

	@GET
	@Produces("application/json")
	public Response BuscarTodos(){
		
		Gson gson = new Gson();
		EnfermeiraManager efm = new EnfermeiraManager();
		String enfermeiras;
		
		try{
			
			enfermeiras = gson.toJson(efm.BuscarTodosEnfermeiros());
		
		}catch(Exception ex){
			
			String msgErro = "Status: 404 Not Found - NÃO existe nenhuma Enfermeira cadastrada no BD!";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		
		String msg = "Status: 200 OK - Busca encontrada com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(enfermeiras).build();
	}
	
	@GET
	@Path("{id}/clinicas")
	@Produces("application/json")
	public Response BuscarClinicasDaEnfermeira(@PathParam("id") long id){
		
		Gson gson = new Gson();
		EnfermeiraManager efm = new EnfermeiraManager();
		String clinicas;
		
		try{
			
			clinicas = gson.toJson(efm.BuscarClinicasDasEnfermeiras(id));
		
		}catch(Exception ex){
			
			String msgErro = "Status: 404 Not Found - NÃO encontrado no BD!";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		
		String msg = "Status: 200 OK - Busca encontrada com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(clinicas).build();
	}
	
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response BuscarPorPessoa(@PathParam("id") long id){
		
		Gson gson = new Gson();
		EnfermeiraManager efm = new EnfermeiraManager();
		String enfermeira;
		JSONObject jsob = new JSONObject();
		
		try{
			
			jsob = efm.BuscarEnfermeiraPorPessoa(id);
			
			enfermeira = gson.toJson(jsob);
		
		}catch(Exception ex){
			
			ex.printStackTrace();
			String msgErro = "Erro 404 - Enfermeira NÃO encontrada no BD!";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		
		String msg = "Status: 200 OK - Busca encontrada com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(enfermeira).build();
	}
	
	
	
	@GET
	@Path("coren/{coren}")
	@Produces("application/json")
	public Response BuscarPorCOREN(@PathParam("coren")  String coren){
		
		Gson gson = new Gson();
		EnfermeiraManager efm = new EnfermeiraManager();
		JSONObject jsob = new JSONObject();
		String enfermeira;
		
		try{
			
			jsob = efm.BuscarEnfermeiraPorCOREN(coren);
			
			enfermeira = gson.toJson(jsob);
		
		}catch(Exception ex){
			
			ex.printStackTrace();
			String msgErro = "Status: 404 Not Found - Enfermeira NÃO encontrada no BD!";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		
		String msg = "Status: 200 OK - Busca encontrada com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(enfermeira).build();
	}

	@GET
	@Path("/{id}/pacientes")
	@Produces("application/json")
	public Response BuscarPacientesDaEnfermeira(@PathParam("id") long id){
		
		Gson gson = new Gson();
		EnfermeiraManager efm = new EnfermeiraManager();
		String pacientes;
		
		try{
			
			pacientes = gson.toJson(efm.BuscarPacientePorEnfermeira(id));
			
		}catch(Exception ex){
	
			String msgErro = "Status: 404 Not Found - Não foi Encontrado nenhum Paciente Para essa Enfermeira";
			System.out.println(msgErro);
			return Response.status(404).entity(msgErro).build();
		}
		
		System.out.println("Pacientes Encontrado com Sucesso!");
		return Response.status(200).entity(pacientes).build();
	}
		
	@PUT
	@Path("{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response AtualizarEnfermeira(@PathParam("id") long pessoa_id, String dados) throws JSONException{
		
		EnfermeiraManager efmg = new EnfermeiraManager();
		JSONObject obj = new JSONObject(dados);
		int cod_http = 0;
		String json = null;
		Gson gson = new Gson();
		
		try{
			cod_http =  efmg.AtualizarEnfermeira(obj.optString("pessoa_id"), obj.optString("nome"), obj.optString("tel"), obj.optString("email"), 
					obj.optString("sexo"), obj.optString("datanasc"),obj.optString("numero_cpf"), obj.optString("data_emissao_cpf"), obj.optString ("rua"),
					obj.optString("numero"), obj.optString("complemento"), obj.optString("bairro"), 
					obj.optString("cidade"), obj.optString("uf"), obj.optString("pais"), obj.optString("cep"), 
					obj.optJSONArray("clinicas_ids"), obj.optString("numero_coren"), obj.optString("data_emissao_coren"));
			
			if(cod_http == 200){
				
				json = gson.toJson(obj);
				
			}
			
			if(cod_http == 404){
				String msg_erro = "Status: 404 Not Found - Enfermeira Não Encontrada no BD! Cadastre antes de Atualizar";
				return Response.status(404).entity(msg_erro).build();
			}
			
			if(cod_http == 400){
				String msg_erro = "Status: 400 Bad Request - CPF digitado é INVÁLIDO! Por Favor insira o CPF corretamente!";
				return Response.status(400).entity(msg_erro).build();
			}
			}catch(Exception ex){
				
				ex.printStackTrace();
				System.out.println("Erro ao atualizar os dados");
				
			}
			
			String msg = "Status: 200 OK - Enfermeira Atualizada Com Sucesso!";
			System.out.println(msg);
			return Response.status(200).entity(json).build();
	}
	
	@DELETE
	@Path("{id}")
	public Response RemoverEnfermeira(@PathParam("id") long id){
		
		EnfermeiraManager efm = new EnfermeiraManager();
		int http_cod;
		
		try{
			
			http_cod = efm.RemoverEnfermeira(id);
			
			if(http_cod == 404){
				String msgErro = "Status: 404 Not Found - Enfermeira NÃO Encontrada no BD! NÃO é possivel Remover!";
				System.out.println(msgErro);
				return Response.status(404).entity(msgErro).build();
			}
		
		}catch(Exception ex){
			
			ex.printStackTrace();
			String msgErro = "Status: 500 Internal Server Error - Erro ao Remover!";
			System.out.println(msgErro);	
		}
		
		String msg = "Status: 200 OK - Enfermeira Removida Com Sucesso!";
		System.out.println(msg);
		return Response.status(200).entity(msg).build();
	}
}



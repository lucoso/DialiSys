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

import br.com.DialiSys.Manager.ClinicaManager;

import com.google.gson.Gson;


@Path("/clinicas")
public class ClinicaResource {

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response CadastrarClinica(String dados) throws JSONException{
		
		ClinicaManager cm = new ClinicaManager();
		JSONObject obj = new JSONObject(dados);
		int cod_http = 0;
		long id = 0;
		
			
		try{
			cod_http =  cm.CadastrarClinica(obj.optString("nome"), obj.optString("tel"), 
					obj.optString("email"), obj.optString("site"),
					obj.optString("rua"), obj.optString("numero"), obj.optString("complemento"), obj.optString("bairro"), 
					obj.optString("cidade"), obj.optString("uf"), obj.optString("pais"), 
					obj.optString("cep"), obj.optString("numero_cnpj"), obj.optString("data_emissao_cnpj"));
			
			if(cod_http == 201){
				
				id = ClinicaManager.idGerado;
			}
			if(cod_http == 409){
				String msg_erro = "Status: 409 Conflito! Clinica ja Existe!";
				return Response.status(409).entity(msg_erro).build();
			}
			
			if(cod_http == 400){
				
				String erro = "CNPJ digitado é INVÁLIDO! Por Favor insira o CNPJ corretamente!";
				return Response.status(400).entity(erro).build();
			}
			}catch(Exception e){
				e.printStackTrace();
				
			}
			
			String msg = "Status: 201 CREATED - Clinica Cadastrada Com Sucesso!";
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
		
		ClinicaManager cm = new ClinicaManager();
		Gson gson = new Gson();
		String clinicas;
		
		try{
			
			clinicas = gson.toJson(cm.BuscarTodasClinicas());
		
		}catch(Exception e){		
				
			System.out.println("Status: 404 - As Clinicas NÃO foram encontradas no BD");
			return Response.status(404).build();
		}
				System.out.println("Status: 200 OK");
				return Response.status(200).entity(clinicas).build();
	}
	
	
	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response BuscarPorID(@PathParam("id") long id){
		
		ClinicaManager cm = new ClinicaManager();
		Gson gson = new Gson();
		String clinica = null; 
			
			try{
				
					clinica = gson.toJson(cm.BuscarClinicaPorID(id));
			
			}catch(Exception e){
					
				String erro = "Status: 404 Not Found - Clinica NÃO encontrada no BD";
				return Response.status(404).entity(erro).build();
				}
			
			System.out.println("Status: 200 OK! Clinica Encontrada com Sucesso");
			return Response.status(200).entity(clinica).build();			
	}

	
	@GET
	@Path("cnpj/{cnpj}")
	@Produces("application/json")
	public Response BuscarPorCNPJ(@PathParam("cnpj") String cnpj){
	
		ClinicaManager cm = new ClinicaManager();
		Gson gson = new Gson();
		String clinica = null; 
		
		try{
			
			clinica = gson.toJson(cm.BuscarClinicaPorCNPJ(cnpj));
		
		}catch(Exception e){
				
			String erro = "Status: 404 Not Found - Clinica NÃO encontrada no BD";
			return Response.status(404).entity(erro).build();
			}
		
		System.out.println("Status: 200 OK! Clinica Encontrada com Sucesso");
		return Response.status(200).entity(clinica).build();
	}

	@PUT
	@Path("{id}")
	@Produces("application/json")
	public Response AtualizarClinica(@PathParam("id") long id, String dados) throws JSONException{
		
		ClinicaManager cm = new ClinicaManager();
		int cod_http = 0;
		JSONObject obj = new JSONObject(dados);
		String json = null;
		Gson gson = new Gson();
			
		try{
			
			cod_http = cm.AtualizarClinica(obj.optString("clinica_id"), obj.optString("nome"), obj.optString("tel"), 
					obj.optString("email"), obj.optString("site"),
					obj.optString("rua"), obj.optString("numero"), obj.optString("complemento"), obj.optString("bairro"), 
					obj.optString("cidade"), obj.optString("uf"), obj.optString("pais"), 
					obj.optString("cep"), obj.optString("numero_cnpj"), obj.optString("data_emissao_cnpj"));
		
			if(cod_http == 200){
				json = gson.toJson(obj);
				
			}
			
			if(cod_http == 404){
				
				String erro = "Status: 404 Not Found - Clinica NÃO encontrada no BD! Cadastre a Clinica antes de Atualizar!";
				System.out.println(erro);
				return Response.status(500).entity(erro).build();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		System.out.println("Status: 200 OK - Clinica Atualizada com Sucesso");
		return Response.status(200).entity(json).build();
	}
	
	@DELETE
	@Path("{id}")
	public Response RemoverClinica(@PathParam("id") long id){
		
		ClinicaManager cm = new ClinicaManager();
		int cod_http = 0;
		
		try{
		
			cod_http = cm.RemoverClinica(id);
			
			if(cod_http == 404){
				
				String erro = "Status: 404 Not Found - Clinica Não esta Cadastrada no BD! "
						+ "Não é possivel Remover a Clinica";
				System.out.println(erro);
				return Response.status(404).entity(erro).build();
			}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Erro ao Remover a Clinica");
		}
		
		String msg = "Status: 200 OK - Clinica Removida com Sucesso";
		System.out.println(msg);
		return Response.status(200).entity(msg).build();
	}
}

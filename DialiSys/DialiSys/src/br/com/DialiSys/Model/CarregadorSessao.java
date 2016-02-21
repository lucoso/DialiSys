package br.com.DialiSys.Model;

import java.util.Date;
import java.util.List;

public interface CarregadorSessao {
	
	public List<Sessao> BuscarSessaoCalculoMedia(long id_paciente, Date dataI, Date dataF);

}

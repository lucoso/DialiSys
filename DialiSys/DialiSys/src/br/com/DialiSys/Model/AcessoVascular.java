package br.com.DialiSys.Model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AcessoVascular {
		
		@Column(name="Tipo_Acesso", length=100, nullable=false)
		private String tipo;

		public String getTipo() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
}

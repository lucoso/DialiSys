package br.com.DialiSys.Model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

	@Embeddable
	public class AntiCoagulante {

		@Column(name="Nome_Anticoagulante", length=100, nullable=false)
		private String nome;

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

}

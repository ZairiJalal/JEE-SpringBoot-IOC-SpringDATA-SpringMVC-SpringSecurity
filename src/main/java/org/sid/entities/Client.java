package org.sid.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity; //importer pour la persistance (pour pouvoir utiliser les annotations des entity)
import javax.persistence.FetchType;
import javax.persistence.Table; //pour l'annotation des tables ( @Table )
import javax.persistence.Id; //pour @Id
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Entity
public class Client implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
	private Long codeClient; 
	private String nomClient;
	private String adresseClient;		
	@OneToMany (mappedBy = "client",fetch=FetchType.LAZY) 
	private Collection<Compte>  comptes;

	public Client() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Client(String nomClient, String adresseClient ) {
		super();
		this.nomClient = nomClient;
		this.adresseClient = adresseClient; 
	}
	public Long getCodeClient() {
		return codeClient;
	}

	public void setCodeClient(Long codeClient) {
		this.codeClient = codeClient;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getAdresseClient() {
		return adresseClient;
	}

	public void setAdresseClient(String adresseClient) {
		this.adresseClient = adresseClient;
	}

	public Collection<Compte> getComptes() {
		return comptes;
	}

	public void setComptes(Collection<Compte> comptes) {
		this.comptes = comptes;
	}


}

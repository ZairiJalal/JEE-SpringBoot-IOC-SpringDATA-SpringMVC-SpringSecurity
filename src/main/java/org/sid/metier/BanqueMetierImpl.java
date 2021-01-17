package org.sid.metier;

import java.util.Date;
import java.util.List;

import org.sid.dao.ICompteRepository;
import org.sid.dao.IOperationRepository;
import org.sid.entities.Compte;
import org.sid.entities.CompteCourant;
import org.sid.entities.Operation;
import org.sid.entities.Retrait;
import org.sid.entities.Versement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;   // Spring g�re les transactions 
 

@Service  // annotation utilise pour les objets de la couche metier
@Transactional  // a importer : import org.springframework.transaction.annotation.Transactional;
public class BanqueMetierImpl implements IBanqueMetier { //puis , on va faire le couplage faible avec la couche dao --> la couche metier va faire appel � la couche dao
  // pour faire l'injection de dependance  --> on va demander a spring d'injecter une implementation de cette interface
	@Autowired // a importer : import org.springframework.beans.factory.annotation.Autowired;
	private ICompteRepository compteRepository;

	@Autowired  
	private IOperationRepository operationRepository;

	 
	@Override
	public Compte getCompte(Long compteId) {
		Compte compte=compteRepository.findById(compteId).orElse(null);
		 if (compte==null) throw new RuntimeException("Compte introuvable"); // c'est une exception non surveiller
		return compte;
	}

	@Override
	public void versement(Long compteId, double montant) {
		Compte compte = getCompte(compteId);
		Versement versement = new Versement(new Date(), montant,compte); // le versement est une operation
	    operationRepository.save(versement); // ici, la methode save() permet l'enregistrement
	    //mettre a jour le solde du compte
	    compte.setSolde(compte.getSolde() + montant);
	    compteRepository.save(compte); // ici, la methode save permet de mettre a jours le compte (update)  ---->Meme dans la console, on aura comme requette : Hibernate: update compte set code_cli=?, date_creation=?, solde=?, decouvert=? where code_compte=?
	 }

	@Override
	public void retrait(Long compteId, double montant) {
		
		Compte compte = getCompte(compteId);
		double facilitesCaisse = 0;
		
		if (compte instanceof CompteCourant) {
			
			 facilitesCaisse = ((CompteCourant) compte).getDecouvert();
			
			 if ( compte.getSolde()+facilitesCaisse < montant )  throw new RuntimeException("Slode insuffisant");
			
		}
		
		Retrait retrait = new Retrait(new Date(), montant,compte); // le retrait est une operation
	    operationRepository.save(retrait); // ici, la methode save() permet l'enregistrement
	    //mettre a jour le solde du compte
	    compte.setSolde(compte.getSolde() - montant);
	    compteRepository.save(compte); // ici, la methode save permet de mettre a jours le compte (update)
	
		
	}

	@Override
	public void virement(Long compteIdRetrait,Long compteIdVersement,double montant) {
		if(compteIdRetrait == compteIdVersement)throw new RuntimeException("Impossible : On ne peut pas effectuer un virement dans le meme compte");
		retrait(compteIdRetrait,montant);
		versement(compteIdVersement,montant);
		
	}

 	/*@Override
	public Page<Operation> listOperationsCompte(Long compteId, int page, int sizePage) {  // page: est le numero de la page
		 
		return operationRepository.listOperation(compteId, new PageRequest(page,sizePage));
 	}*/
 	
     @Override
	public List<Operation> listOperationsCompte(Long compteId) {  // page: est le numero de la page (compteId,  new  PageRequ  );
		 
		return operationRepository.listOperation(compteId);
	} 	
} 
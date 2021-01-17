package org.sid.metier;

  
import java.util.List;

import org.sid.entities.Compte; 
import org.sid.entities.Operation;
import org.springframework.data.domain.Page;

public interface IBanqueMetier {

	 public Compte getCompte(Long compteId);
	 public void versement(Long compteId, double montant );
	 public void retrait(Long compteId, double montant );
	 public void virement(Long compteIdRetrait,Long compteIdVersement,double montant);
	 public List<Operation> listOperationsCompte(Long compteId);
	/* 
	 public List<Operation> listOperationsCompte(String codeCompte);
	*/	
}

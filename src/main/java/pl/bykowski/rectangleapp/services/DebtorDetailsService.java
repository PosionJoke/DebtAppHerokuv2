package pl.bykowski.rectangleapp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DebtorDetailsService {

    private DebtorDetailsRepo debtorDetailsRepo;
    private DebtorHistoryService debtorHistoryService;

    public DebtorDetailsService(DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryService debtorHistoryService) {
        this.debtorDetailsRepo = debtorDetailsRepo;
        this.debtorHistoryService = debtorHistoryService;
    }

    private void saveDebtorDetails(DebtorDetails debtorDetails){
        debtorDetailsRepo.save(debtorDetails);
    }

     public void addNewDebtorDetails(String debtorName, float debtValue, String reasonForTheDebt, String userName, Debtor debtor) {
        DebtorDetails debtorDetails = new DebtorDetails();
        debtorDetails.setName(debtorName);
        debtorDetails.setDebt(debtValue);
        debtorDetails.setDate(LocalDate.now());
        debtorDetails.setReasonForTheDebt(reasonForTheDebt);
        debtorDetails.setUserName(userName);
        debtorDetails.setDebtor(debtor);

        saveDebtorDetails(debtorDetails);
    }

    @Transactional
    public void updateDebtorDetailsDebt(Long debtID, float debtValue) {
        Optional<DebtorDetails> debtorDetails = debtorDetailsRepo.findById(debtID);
//        userRoleOp.ifPresent(userRole -> user.setRoles(new HashSet<>(Arrays.asList(userRole))));
        debtorDetails.ifPresent(debtorD -> isThisDebtUnderZero(debtorD, debtValue));
        if(debtorDetails.isPresent()){
            DebtorDetails debtorDetails1 = debtorDetails.get();
            isThisDebtUnderZero(debtorDetails1, debtValue);
        }
    }

    public DebtorDetails findById(Long id){
        Optional<DebtorDetails> debtorDetails = debtorDetailsRepo.findById(id);
        return debtorDetailsRepo.findById(id).get();

    }

    public void deleteById(Long id){
        debtorDetailsRepo.deleteById(id);
    }

    public boolean isThisNameExist(String name){
        boolean isThisNameExist = false;
        if(debtorDetailsRepo.findByName(name) != null){
            isThisNameExist = true;
        }
        return isThisNameExist;
    }

    private void isThisDebtUnderZero(DebtorDetails debtorDetails, float debtValue){
        float newDebt = debtorDetails.getDebt() + debtValue;
        if(newDebt <= 0){
            debtorDetails.setDebt(0);
            deleteDebtByIdAndName(debtorDetails.getName(), debtorDetails.getId());
        }else {
            debtorDetails.setDebt(newDebt);
            debtorDetailsRepo.save(debtorDetails);
        }
    }

    @Transactional
    public void deleteDebtByIdAndName(String debtorName, Long debtorID) {
        DebtorDetails debtorDetailsCopy = debtorDetailsRepo.findByNameAndId(debtorName, debtorID);

        debtorHistoryService.saveEntityDebtorHistory(debtorDetailsCopy);

        debtorDetailsRepo.delete(debtorDetailsCopy);
    }

    public List<DebtorDetails> findByUserName(String name) {
          return debtorDetailsRepo.findByUserName(name);
    }
}

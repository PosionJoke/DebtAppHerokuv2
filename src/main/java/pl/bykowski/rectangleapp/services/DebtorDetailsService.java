package pl.bykowski.rectangleapp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DebtorDetailsService {

    private final DebtorDetailsRepo debtorDetailsRepo;
    private final DebtorHistoryService debtorHistoryService;

    public DebtorDetailsService(DebtorDetailsRepo debtorDetailsRepo, DebtorHistoryService debtorHistoryService) {
        this.debtorDetailsRepo = Objects.requireNonNull(debtorDetailsRepo, "debtorDetailsRepo must be not null");
        this.debtorHistoryService = Objects.requireNonNull(debtorHistoryService, "debtorHistoryService must be not null");
    }

    private void saveDebtorDetails(DebtorDetails debtorDetails) {
        debtorDetailsRepo.save(debtorDetails);
    }

    public DebtorDetails addNewDebtorDetails(String debtorName, BigDecimal debtValue, String reasonForTheDebt, String userName, Debtor debtor) {
        DebtorDetails debtorDetails = new DebtorDetails();
        debtorDetails.setName(debtorName);
        debtorDetails.setDebt(debtValue);
        debtorDetails.setDate(LocalDate.now());
        debtorDetails.setReasonForTheDebt(reasonForTheDebt);
        debtorDetails.setUserName(userName);
        debtorDetails.setDebtor(debtor);

        saveDebtorDetails(debtorDetails);
        return debtorDetails;
    }

    @Transactional
    public void updateDebtorDetailsDebt(Long debtID, BigDecimal debtValue) {
        Optional<DebtorDetails> debtorDetails = debtorDetailsRepo.findById(debtID);
        debtorDetails.ifPresent(debtorD -> isThisDebtUnderZero(debtorD, debtValue));
    }

    public Optional<DebtorDetails> findById(Long id) {
        return debtorDetailsRepo.findById(id);
    }

    public void deleteById(Long id) {
        debtorDetailsRepo.deleteById(id);
    }

    // todo pls use debuger to check is this if works fine
    private void isThisDebtUnderZero(DebtorDetails debtorDetails, BigDecimal debtValue) {
        BigDecimal newDebt = debtorDetails.getDebt().add(debtValue);
        if (newDebt.compareTo(new BigDecimal(0)) <= 0) {
            debtorDetails.setDebt(new BigDecimal(0));
            deleteDebtById(debtorDetails.getId());
        } else {
            debtorDetails.setDebt(newDebt);
            debtorDetailsRepo.save(debtorDetails);
        }
    }

    @Transactional
    public void deleteDebtById(Long debtorID) {
        Optional<DebtorDetails> debtorDetailsCopyOptional = debtorDetailsRepo.findById(debtorID);
        debtorDetailsCopyOptional.ifPresent(debtorDetails -> {
            debtorHistoryService.saveEntityDebtorHistory(debtorDetails);
            debtorDetailsRepo.delete(debtorDetails);
        });
    }

    public List<DebtorDetails> findByUserName(String name) {
        return debtorDetailsRepo.findByUserName(name);
    }
}

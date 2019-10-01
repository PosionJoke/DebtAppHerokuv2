package pl.bykowski.rectangleapp.controller;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorDetailsRepo;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDetailsDTOService;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j
@Controller
public class DebtorDetailsController {

    private final DebtorDetailsRepo debtorDetailsRepo;
    private final DebtorDetailsService debtorDetailsService;
    private final DebtorService debtorService;
    private DebtorDetailsDTOService debtorDetailsDTOService;

    public DebtorDetailsController(DebtorDetailsRepo debtorDetailsRepo, DebtorDetailsService debtorDetailsService,
                                   DebtorDetailsDTOService debtorDetailsDTOService, DebtorService debtorService) {
        this.debtorDetailsDTOService = Objects.requireNonNull(debtorDetailsDTOService, "debtorDetailsDTOService must be not null");
        this.debtorDetailsService = Objects.requireNonNull(debtorDetailsService, "debtorDetailsService must be not null");
        this.debtorDetailsRepo = Objects.requireNonNull(debtorDetailsRepo, "debtorDetailsRepo must be not null");
        this.debtorService = Objects.requireNonNull(debtorService, "debtorService must be not null");
    }

    @GetMapping("/debtor-details-list")
    public ModelAndView showDebtorDetailsList(Principal principal) {
        List<DebtorDetails> debtorDetailsList = debtorDetailsService.findByUserName(principal.getName());
        List<DebtorDetailsDTO> debtorDetailsDTOList1 = debtorDetailsDTOService.returnDebtorDetailsDTOList(debtorDetailsList);
        return new ModelAndView("debtor-details-list")
                .addObject("debtorLIST", debtorDetailsDTOList1);
    }

    @GetMapping("/debtor-details-debt-edit")
    public ModelAndView editDebtorDetails(@RequestParam Long id, @RequestParam String name) {

        Optional<DebtorDetails> debtorDetails = debtorDetailsRepo.findByNameAndId(name, id);

        DebtorDetailsDTO debtorDetailsDTO = debtorDetails
                .map(debtorDetails2 -> debtorDetailsDTOService.returnDebtorDetailsDTO(debtorDetails2))
                .orElse(new DebtorDetailsDTO());

        return new ModelAndView("debtor-details-debt-edit")
                .addObject("name", name)
                .addObject("id", id)
                .addObject("debtor", debtorDetailsDTO);
    }

    @GetMapping("/make-new-debtor-details")
    public ModelAndView makeNewDebtorDetails(@RequestParam String name) {
        return new ModelAndView("make-new-debtor-details")
                .addObject("debtorDetails", new DebtorDetailsDTO())
                .addObject("name", name);
    }

    @GetMapping("/debtor-details-delete")
    public ModelAndView deleteDebtorDetails(@RequestParam Long id) {
        return new ModelAndView("debtor-details-delete")
                .addObject("id", id);
    }

    @GetMapping("/debtor-details-delete-by-id")
    public ModelAndView deleteDebtorDetailsById(@RequestParam Long id, Principal principal) {

        debtorService.deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(id, principal);

        List<DebtorDetails> debtorDetailsList = debtorDetailsService.findByUserName(principal.getName());
        List<DebtorDetailsDTO> debtorDetailsDTOList1 = debtorDetailsDTOService.returnDebtorDetailsDTOList(debtorDetailsList);
        return new ModelAndView("debtor-details-list")
                .addObject("debtorLIST", debtorDetailsDTOList1);
    }

    @PostMapping("/make-new-debtor-details")
    public ModelAndView saveNewDebtorDetails(@ModelAttribute DebtorDetailsDTO debtorDetails, Principal principal, @RequestParam String name) {
        Debtor debtor = debtorService.findDebtorByName(name);
        debtorService.updateTotalDebtAndMakeNewDebtorDetails(debtorDetails, debtor, principal.getName());
        return new ModelAndView("debtor-details-list")
                .addObject("debtorLIST", debtorDetailsService.findByUserName(principal.getName()));
    }

    @PostMapping("/debtor-details-save")
    public ModelAndView saveDebtorDetails(@ModelAttribute DebtorDetailsDTO debtorDetailsDTO, Principal principal,
                                          @RequestParam Long id) {

        debtorService.updateTotalDebtAndUpdateDebtorDetailsDebt(debtorDetailsDTO, id);

        return new ModelAndView("debtor-details-list")
                .addObject("debtorLIST", debtorDetailsService.findByUserName(principal.getName()));
    }
}

package pl.bykowski.rectangleapp.controller;

import pl.bykowski.rectangleapp.model.dto.CurencyTypes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.dto.DebtorDTO;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.services.CurrencyService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorDTOService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class DebtorController {
    private final DebtorRepo debtorRepo;
    private final DebtorService debtorService;
    private final CurrencyService currencyService;
    private DebtorDTOService debtorDTOService;

    public DebtorController(DebtorRepo debtorRepo, DebtorService debtorService, CurrencyService currencyService,
                            DebtorDTOService debtorDTOService) {

        this.currencyService = Objects.requireNonNull(currencyService, "currencyService must be not null");
        this.debtorDTOService = Objects.requireNonNull(debtorDTOService, "debtorDTOService must be not null");
        this.debtorRepo = Objects.requireNonNull(debtorRepo, "debtorRepo must be not null");
        this.debtorService = Objects.requireNonNull(debtorService, "debtorService must be not null");
    }

    @GetMapping("/debtor-list")
    public ModelAndView showDebtorList(Principal principal,
                                       @RequestParam(required = false, defaultValue = "PLN") String currency) {

        String currencyRate = currencyService.calculateCurrencyRates(currency, "PLN");

        List<Debtor> debtors = debtorService.findByUserName(principal.getName());
        List<Debtor> debtorsWithCurrencyRate = currencyService.setCurrencyRateForDebtors(debtors, currencyRate);

        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorsWithCurrencyRate)
                .addObject("currencyTypes", CurencyTypes.values())
                .addObject("currency", currency);
    }

    @GetMapping("/debtor-create")
    public ModelAndView createDebtor() {
        return new ModelAndView("debtor-create")
                .addObject("debtor", new DebtorDetailsDTO());
    }

    @GetMapping("/debtor-debt-edit")
    public ModelAndView debtorDebtEdit(@RequestParam Long id, @RequestParam String name) {

        Optional<Debtor> debtorOptional = debtorRepo.findByName(name);

        DebtorDTO debtorDTO = debtorOptional
                .map(debtor -> debtorDTOService.returnDebtorDTO(debtor))
                .orElse(new DebtorDTO());
        return new ModelAndView("debtor-debt-edit")
                .addObject("name", name)
                .addObject("id", id)
                .addObject("debtor", debtorDTO);
    }

    @PostMapping("/debtor-save")
    public ModelAndView saveDebtor(@ModelAttribute DebtorDTO debtorDTO, Principal principal,
                                   @RequestParam Long id) {

        Optional<Debtor> debtorToUpdateOptional = debtorRepo.findById(id);

        BigDecimal actualTotalDebt = debtorToUpdateOptional
                .map(Debtor::getTotalDebt)
                .orElse(new BigDecimal(0));

        String actualUserName = principal.getName();
        debtorService.updateTotalDebt(id, actualTotalDebt);

        List<Debtor> debtorList = debtorService.findByUserName(principal.getName());
        List<DebtorDTO> debtorDTOList1 = debtorDTOService.returnDebtorDTOList(debtorList);

        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorDTOList1);
    }

    @PostMapping("/make-new-debtor")
    public ModelAndView makeNewDebtor(@ModelAttribute DebtorDetailsDTO debtorDetailsDTO, Principal principal) {

        debtorService.addNewDebtor(debtorDetailsDTO.getName(), debtorDetailsDTO.getDebt(), debtorDetailsDTO.getReasonForTheDebt(), principal.getName());

        List<Debtor> debtorList = debtorService.findByUserName(principal.getName());
        List<DebtorDTO> debtorDTOList1 = debtorDTOService.returnDebtorDTOList(debtorList);
        return new ModelAndView("debtor-list")
                .addObject("debtors", debtorDTOList1);
    }
}

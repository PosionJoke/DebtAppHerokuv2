package pl.bykowski.rectangleapp.services;

import junitparams.JUnitParamsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.bykowski.rectangleapp.model.DebtorHistory;
import pl.bykowski.rectangleapp.model.dto.DebtorHistoryDTO;
import pl.bykowski.rectangleapp.services.tdo_services.DebtorHistoryDTOService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class DebtorHistoryDTOServiceTest {

    private DebtorHistoryDTOService debtorHistoryDTOService;

    @Before
    public void init() {
        debtorHistoryDTOService = new DebtorHistoryDTOService();
    }

    @Test
    public void should_return_debtorHistoryDTOList_based_on_debtorHistoryList() {
        //given
        DebtorHistory debtorHistory1 = new DebtorHistory();
        DebtorHistory debtorHistory2 = new DebtorHistory();
        List<DebtorHistory> debtorHistoryList = Arrays.asList(debtorHistory1, debtorHistory2);
        //when
        List<DebtorHistoryDTO> found = debtorHistoryDTOService.returnDebtorHistoryDTOList(debtorHistoryList);
        //then
        assertThat(found.size()).isEqualTo(debtorHistoryList.size());
    }
}

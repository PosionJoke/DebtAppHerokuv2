package pl.bykowski.rectangleapp.gui.debtor_gui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.bykowski.rectangleapp.form.DebtorGUIForm;
import pl.bykowski.rectangleapp.services.DebtorService;


@StyleSheet("/css/style.css")
@Route(value = DebtorGUI.VIEW_NAME)
public class DebtorGUI extends VerticalLayout {

    public static final String VIEW_NAME = "debtorgui";

    private static final StringToFloatConverter DEBT_TO_FLOAT_CONVERTER = new StringToFloatConverter("Invalid debt format");
    private static final StringToLongConverter DEBT_TO_LONG_CONVERTER = new StringToLongConverter("Invalid debt format");

    private transient DebtorService debtorService;

    private Binder<DebtorGUIForm> debtorGUIFormBinder;

    //define variables which should be in GUI
    private TextField nameTextField;
    private TextField debtTextField;
    private TextField reasonForTheDebtTextField;
    private TextField idDebtTextField;

    private Button infoButton;
    private Button addDebtorButton;
    private Button addDebtButton;
    private Button updateButton;
    private Button deleteDebtButton;
    private Button showDebtorDetailsButton;
    private Button showDebtorHistoryButton;
    private Button showDebtorListButton;
    private Button logOutButton;
    private Button logInButton;

    private TextArea areaInfo;

    public DebtorGUI(DebtorService debtorService) {

        this.debtorService = debtorService;

        this.nameTextField = new TextField("Type Name: ");
        this.debtTextField = new TextField("Type Debt: ");
        this.reasonForTheDebtTextField = new TextField("Type Reason for the debt: ");
        this.idDebtTextField = new TextField("Type ID of debt: ");

        this.infoButton = new Button("Show info by name");
        this.addDebtorButton = new Button("Add new Debtor");
        this.addDebtButton = new Button("Add new Debt");
        this.updateButton = new Button("Update debt");
        this.deleteDebtButton = new Button("Delete Debt by ID ");
        this.showDebtorDetailsButton = new Button("Show Debtor Details");
        this.showDebtorHistoryButton = new Button("Show Debtor History");
        this.showDebtorListButton = new Button("Show Debtor List");
        this.logOutButton = new Button("Logout");
        this.logInButton = new Button(("Login"));

        this.areaInfo = new TextArea("Info");

        debtorGUIFormBinder = new Binder<>();
        debtorGUIFormBinder.forField(nameTextField).bind(DebtorGUIForm::getTextFieldName, DebtorGUIForm::setTextFieldName);
        debtorGUIFormBinder.forField(reasonForTheDebtTextField).bind(DebtorGUIForm::getTextFieldReasonForTheDebt, DebtorGUIForm::setTextFieldReasonForTheDebt);
        debtorGUIFormBinder.forField(debtTextField).withConverter(DEBT_TO_FLOAT_CONVERTER).bind(DebtorGUIForm::getTextFieldDebt, DebtorGUIForm::setTextFieldDebt);
        debtorGUIFormBinder.forField(idDebtTextField).withConverter(DEBT_TO_LONG_CONVERTER).bind(DebtorGUIForm::getTextFieldIdDebt, DebtorGUIForm::setTextFieldIdDebt);
        debtorGUIFormBinder.forField(areaInfo).bind(DebtorGUIForm::getAreaInfo, DebtorGUIForm::setAreaInfo);
        debtorGUIFormBinder.setBean(new DebtorGUIForm());


        infoButton.addClickListener(buttonClickEvent -> onInfoButtonClick());

        addDebtorButton.addClickListener(buttonClickEvent -> onAddDebtorButtonClick());

        addDebtButton.addClickListener(buttonClickEvent -> onAddDebtButtonClick());

        updateButton.addClickListener(buttonClickEvent -> onUpdateButtonClick());

        deleteDebtButton.addClickListener(buttonClickEvent -> onDeleteDebtButtonClick());

        showDebtorDetailsButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(DebtorDetailsListGUI.VIEW_NAME)));

        showDebtorHistoryButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(DebtorHistoryListGUI.VIEW_NAME)));

        showDebtorListButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(DebtorListGUI.VIEW_NAME)));

        logOutButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("logout")));

        logInButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("login")));

        VerticalLayout topBar = new VerticalLayout();
        topBar.add(logOutButton);
        topBar.add(logInButton);
        add(topBar);

        HorizontalLayout horizontalLayoutShowDebtorsGrid = new HorizontalLayout();
        horizontalLayoutShowDebtorsGrid.add(showDebtorDetailsButton);
        horizontalLayoutShowDebtorsGrid.add(showDebtorHistoryButton);
        horizontalLayoutShowDebtorsGrid.add(showDebtorListButton);
        add(horizontalLayoutShowDebtorsGrid);

        add(nameTextField);
        add(debtTextField);
        add(reasonForTheDebtTextField);
        add(idDebtTextField);

        add(updateButton);
        add(infoButton);
        add(addDebtorButton);
        add(addDebtButton);
        add(deleteDebtButton);

        add(areaInfo);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private void onInfoButtonClick() {
        DebtorGUIForm debtorGUIForm = debtorGUIFormBinder.getBean();

        String name = debtorGUIForm.getTextFieldName();
        debtorGUIForm.setAreaInfo(debtorService.showInfo(name));
        debtorGUIFormBinder.readBean(debtorGUIForm);
    }

    private void onAddDebtorButtonClick() {
        DebtorGUIForm debtorGUIForm = debtorGUIFormBinder.getBean();

        String name = debtorGUIForm.getTextFieldName();
        float debtValue = debtorGUIForm.getTextFieldDebt();
        String reason = debtorGUIForm.getTextFieldReasonForTheDebt();
        String userName = findUserName();
        debtorGUIForm.setAreaInfo(debtorService.addNewDebtor(name, debtValue, reason, userName));
        debtorGUIFormBinder.readBean(debtorGUIForm);
    }

    private void onAddDebtButtonClick() {
        DebtorGUIForm debtorGUIForm = debtorGUIFormBinder.getBean();

        String name = debtorGUIForm.getTextFieldName();
        float debtValue = debtorGUIForm.getTextFieldDebt();
        String reason = debtorGUIForm.getTextFieldReasonForTheDebt();
        String userName = findUserName();
        debtorGUIForm.setAreaInfo(debtorService.addNewDebt(name, debtValue, reason, userName));
        debtorGUIFormBinder.readBean(debtorGUIForm);
    }

    private void onUpdateButtonClick() {
        DebtorGUIForm debtorGUIForm = debtorGUIFormBinder.getBean();

        Long debtorID = debtorGUIForm.getTextFieldIdDebt();
        float debtValue = debtorGUIForm.getTextFieldDebt();
        debtorService.updateDebtByNewDebt(debtorID, debtValue);
        debtorGUIFormBinder.readBean(debtorGUIForm);
    }

    private void onDeleteDebtButtonClick() {
        DebtorGUIForm debtorGUIForm = debtorGUIFormBinder.getBean();

        String name = debtorGUIForm.getTextFieldName();
        String userName = findUserName();
        Long debtorID = debtorGUIForm.getTextFieldIdDebt();
        debtorService.deleteDebtByID(name, debtorID, userName);

        String info = debtorService.showInfo(name);
        debtorGUIForm.setAreaInfo(info);
        debtorGUIFormBinder.readBean(debtorGUIForm);
    }

    private String findUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return currentPrincipalName;
    }
}


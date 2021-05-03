package com.sitrumm.coronarki.gui.views;

import com.sitrumm.coronarki.model.covid.GlobalEntity;
import com.sitrumm.coronarki.model.covid.SummaryEntity;
import com.sitrumm.coronarki.service.CovidService;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.text.NumberFormat;

public class SummaryView extends VerticalLayout {

    private CovidService covidService;

    // GUI

    private Accordion summaryAccordion;

    private VerticalLayout summaryInformation;


    public SummaryView() {
        initGUI();
    }

    private void initGUI() {
        summaryAccordion = new Accordion();
        summaryAccordion.setWidthFull();

        summaryInformation = new VerticalLayout();
    }

    public void fillGUI(CovidService covidService) {
        this.covidService = covidService;
        GlobalEntity globalData = getData().getGlobal();

        TextField dateTextField = new TextField();
        dateTextField.setValue(globalData.getDate().toString());
        dateTextField.setLabel("Current date");
        dateTextField.setReadOnly(true);

        TextField newConfirmedTextField = new TextField();
        newConfirmedTextField.setValue(NumberFormat.getInstance().format(globalData.getNewConfirmed()));
        newConfirmedTextField.setLabel("New confirmed infections");
        newConfirmedTextField.setReadOnly(true);

        TextField totalConfirmedTextField = new TextField();
        totalConfirmedTextField.setValue(NumberFormat.getInstance().format(globalData.getTotalConfirmed()));
        totalConfirmedTextField.setLabel("Total confirmed infections");
        totalConfirmedTextField.setReadOnly(true);

        TextField newDeathsTextField = new TextField();
        newDeathsTextField.setValue(NumberFormat.getInstance().format(globalData.getNewDeaths()));
        newDeathsTextField.setLabel("New deaths");
        newDeathsTextField.setReadOnly(true);

        TextField totalDeathsTextField = new TextField();
        totalDeathsTextField.setValue(NumberFormat.getInstance().format(globalData.getTotalDeaths()));
        totalDeathsTextField.setLabel("Total deaths");
        totalDeathsTextField.setReadOnly(true);

        TextField newRecoveredTextField = new TextField();
        newRecoveredTextField.setValue(NumberFormat.getInstance().format(globalData.getNewRecovered()));
        newRecoveredTextField.setLabel("New recovered");
        newRecoveredTextField.setReadOnly(true);

        TextField totalRecoveredTextField = new TextField();
        totalRecoveredTextField.setValue(NumberFormat.getInstance().format(globalData.getTotalRecovered()));
        totalRecoveredTextField.setLabel("Total recovered");
        totalRecoveredTextField.setReadOnly(true);

        HorizontalLayout infectionsLayout = new HorizontalLayout();
        HorizontalLayout deathsLayout = new HorizontalLayout();
        HorizontalLayout recoveredLayout = new HorizontalLayout();

        infectionsLayout.add(newConfirmedTextField, totalConfirmedTextField);
        deathsLayout.add(newDeathsTextField, totalDeathsTextField);
        recoveredLayout.add(newRecoveredTextField, totalRecoveredTextField);

        summaryInformation.add(dateTextField, infectionsLayout, deathsLayout, recoveredLayout);

        addGUI();
    }

    private void addGUI() {
        summaryAccordion.add(new AccordionPanel("Summary", summaryInformation)).addThemeVariants(DetailsVariant.FILLED);
        summaryAccordion.close();
        add(summaryAccordion);
    }

    private SummaryEntity getData() {
        return covidService.getCovidSummary();
    }

}

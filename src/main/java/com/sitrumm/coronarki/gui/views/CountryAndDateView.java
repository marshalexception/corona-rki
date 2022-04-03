package com.sitrumm.coronarki.gui.views;

import com.sitrumm.coronarki.model.DayCountryEntity;
import com.sitrumm.coronarki.model.SummaryEntity;
import com.sitrumm.coronarki.adapter.CovidAdapter;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import lombok.extern.slf4j.Slf4j;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CountryAndDateView extends VerticalLayout implements ComponentEventListener<ClickEvent<Button>> {

    private transient CovidAdapter covidAdapter;

    // GUI

    private Accordion countryAndDateAccordion;

    private AccordionPanel accordionPanel;

    private ComboBox<String> filterForCountry;

    private TextField dayRangeTextField;

    private Button searchButton;

    private VerticalLayout countrySummary;

    private DatePicker datePicker;

    private Grid<DayCountryEntity> rkiDataGrid;

    public CountryAndDateView() {
        initGUI();
    }

    private void initGUI() {
        this.filterForCountry = new ComboBox<>();
        filterForCountry.setLabel("Country");
        filterForCountry.setHelperText("Select a country to display the current coronavirus data.");

        dayRangeTextField = new TextField();
        dayRangeTextField.setLabel("Days");
        dayRangeTextField.setHelperText("Select day range to display the current coronavirus data (default 14).");
        dayRangeTextField.setPattern("[0-9]*");
        dayRangeTextField.setPreventInvalidInput(true);
        dayRangeTextField.setRequiredIndicatorVisible(true);

        this.searchButton = new Button("Search RKI data", VaadinIcon.SEARCH.create());
        searchButton.addClickListener(this);

        countryAndDateAccordion = new Accordion();
        countryAndDateAccordion.setWidthFull();
        HorizontalLayout searchParameterGrid = new HorizontalLayout(filterForCountry, dayRangeTextField);
        VerticalLayout searchWithoutGrid = new VerticalLayout(searchParameterGrid, searchButton);
        accordionPanel = new AccordionPanel("Search for Country and Date", searchWithoutGrid);
        countryAndDateAccordion.add(accordionPanel).addThemeVariants(DetailsVariant.FILLED);
        countryAndDateAccordion.close();
        add(countryAndDateAccordion);
    }

    @Override
    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
        if (buttonClickEvent.getSource().equals(searchButton)) {
            clearGUI();
            if (filterForCountry.getValue() != null) {
                String searchCountry = filterForCountry.getValue().toLowerCase();
                List<DayCountryEntity> rkiData = covidAdapter.getDataByCountryDayOne(searchCountry);
                if (!dayRangeTextField.getValue().isEmpty()) {
                    fillGrid(rkiData, Long.parseLong(dayRangeTextField.getValue()));
                } else {
                    fillGrid(rkiData, 14L);
                }
            } else {
                Notification notification = new Notification("No country selected. Please select one to search for their current data.", 3000);
                notification.open();
            }
        }
    }

    private void clearGUI() {
        if (rkiDataGrid != null) {
            countryAndDateAccordion.remove(accordionPanel);
        }
    }

    private List<String> loadCountries() {
        List<String> countries = new ArrayList<>();
        SummaryEntity covidSummary = covidAdapter.getCovidSummary();
        if (covidSummary != null) {
            covidSummary.getCountries().forEach(country -> countries.add(country.getCountry()));
        }
        return countries;
    }

    @SuppressWarnings("deprecation")
    private void fillGrid(List<DayCountryEntity> rkiData, Long days) {
        createSummaryForCountry(rkiData);

        rkiDataGrid = new Grid<>(DayCountryEntity.class);
        ListDataProvider<DayCountryEntity> dataProvider = new ListDataProvider<>(rkiData);
        rkiDataGrid.setDataProvider(dataProvider);
        rkiDataGrid.setColumns("date", "active", "confirmed", "deaths", "recovered");

        // add filter possibility for date
        datePicker = new DatePicker("Filter by date: ");
        datePicker.addValueChangeListener(event -> applyFilter(dataProvider, event));

        CountryBarChartView countryBarChartView = new CountryBarChartView(rkiData, days);
        CountryLineChartView countryLineChartView = new CountryLineChartView(rkiData, days);

        HorizontalLayout searchParameterGrid = new HorizontalLayout();

        searchParameterGrid.add(filterForCountry, dayRangeTextField);

        VerticalLayout searchWithGrid = new VerticalLayout(
                searchParameterGrid, searchButton, new Hr(),
                countrySummary, new Hr(),
                countryBarChartView, new Hr(),
                countryLineChartView, new Hr(),
                datePicker, rkiDataGrid);

        if (accordionPanel.isAttached()) {
            countryAndDateAccordion.remove(accordionPanel);
        }

        accordionPanel = new AccordionPanel("Search for Country and Date", searchWithGrid);
        countryAndDateAccordion.add(accordionPanel).addThemeVariants(DetailsVariant.FILLED);
    }

    private void createSummaryForCountry(List<DayCountryEntity> rkiData) {
        String currentCountry = filterForCountry.getValue();
        ListItem firstInfection = new ListItem("First infection in " + currentCountry + " occurred on " + rkiData.get(0).getDate() + ".");
        String formattedDeaths = NumberFormat.getInstance().format(rkiData.get(rkiData.size() - 1).getDeaths());
        ListItem overallDeaths = new ListItem("Overall " + formattedDeaths + " people died in " + currentCountry + ".");
        countrySummary = new VerticalLayout();
        countrySummary.add(new UnorderedList(firstInfection, overallDeaths));
    }

    private void applyFilter(ListDataProvider<DayCountryEntity> dataProvider, AbstractField.ComponentValueChangeEvent<DatePicker, LocalDate> event) {
        log.info("[CountryAndDateView] - date filter with value selected, event: " + event.toString());
        dataProvider.clearFilters();
        if (datePicker.getValue() != null) {
            dataProvider.addFilter(entity -> Objects.equals(datePicker.getValue(), entity.getDate()));
        }
    }

    public void setServiceAndInitData(CovidAdapter covidAdapter) {
        this.covidAdapter = covidAdapter;
        filterForCountry.setItems(loadCountries());
    }
}

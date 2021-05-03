package com.sitrumm.coronarki.gui.views;

import com.sitrumm.coronarki.model.DayCountryEntity;
import com.sitrumm.coronarki.service.CovidService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.ListDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CountryAndDateView extends VerticalLayout implements ComponentEventListener<ClickEvent<Button>> {

    private CovidService covidService;

    @Value("${error.no_filter_selected}")
    private String NO_FILTER_SELECTED;

    // GUI

    private Accordion countryAndDateAccordion;

    private AccordionPanel accordionPanel;

    private Select<String> filterForCountry;

    private Button searchButton;

    private VerticalLayout countrySummary;

    private DatePicker datePicker;

    private Grid<DayCountryEntity> rkiDataGrid;

    private CountryChartView countryChartView;

    public CountryAndDateView() {
        initGUI();
    }

    private void initGUI() {
        this.filterForCountry = new Select<>("France", "Germany", "South-Africa"); // todo
        filterForCountry.setLabel("Country");
        filterForCountry.setHelperText("Select a country to display the current coronavirus data.");

        this.searchButton = new Button("Search RKI data", VaadinIcon.SEARCH.create());
        searchButton.addClickListener(this);

        countryAndDateAccordion = new Accordion();
        countryAndDateAccordion.setWidthFull();
        VerticalLayout searchWithoutGrid = new VerticalLayout(filterForCountry, searchButton);
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
                List<DayCountryEntity> rkiData = covidService.getDataByCountryDayOne(searchCountry);
                fillGrid(rkiData);
            } else {
                // todo
                Notification notification = new Notification(NO_FILTER_SELECTED);
                notification.open();
            }
        }
    }

    private void clearGUI() {
        if (rkiDataGrid != null) {
            countryAndDateAccordion.remove(accordionPanel);
        }
    }

    private void fillGrid(List<DayCountryEntity> rkiData) {
        createSummaryForCountry(rkiData);

        rkiDataGrid = new Grid<>(DayCountryEntity.class);
        ListDataProvider<DayCountryEntity> dataProvider = new ListDataProvider<>(rkiData);
        rkiDataGrid.setDataProvider(dataProvider);
        rkiDataGrid.setColumns("date", "active", "confirmed", "deaths", "recovered");

        // add filter possibility for date
        datePicker = new DatePicker("Filter by date: ");
        datePicker.addValueChangeListener(event -> applyFilter(dataProvider, event));

        countryChartView = new CountryChartView(rkiData);

        VerticalLayout searchWithGrid = new VerticalLayout(
                filterForCountry, searchButton, new Hr(),
                countrySummary, new Hr(),
                countryChartView, new Hr(),
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

    public void setService(CovidService covidService) {
        this.covidService = covidService;
    }
}

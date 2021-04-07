package com.sitrumm.coronarki.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitrumm.coronarki.gui.helper.RkiDataFilter;
import com.sitrumm.coronarki.model.rki.DayCountryEntity;
import com.sitrumm.coronarki.service.RkiService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Route
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Slf4j
public class MainView extends VerticalLayout implements AppShellConfigurator, ComponentEventListener<ClickEvent<Button>> {

    @Value("${rki.basepath}")
    private String basePath;

    @Autowired
    private RkiService rkiService;

    @Value("${error.no_filter_selected}")
    private String NO_FILTER_SELECTED;

    private static final Collection countries = new ArrayList<>(Arrays.asList("France", "Germany", "USA"));

    /** UI **/

    private final ListBox filterForCountry;

    private final Button searchButton;

    private VerticalLayout dashboard;

    private DatePicker datePicker;

    private Grid<DayCountryEntity> rkiDataGrid;


    public MainView() {
        Image rki = loadImage();
        H1 title = new H1("RKI Data Application");
        HorizontalLayout overview = new HorizontalLayout(rki, title);
        add(overview);
        add(new Text("This application allows you to display the present rki data regarding the SARS-CoV-2."));
        
        this.filterForCountry = new ListBox();
        filterForCountry.setItems(countries);

        this.searchButton = new Button("Search RKI data", VaadinIcon.SEARCH.create());
        searchButton.addClickListener(this);

        HorizontalLayout actions = new HorizontalLayout(filterForCountry, searchButton);
        add(actions);
    }

    private Image loadImage() {
        return new Image();
    }

    /**
     * // Find the application directory
     * String basepath = VaadinService.getCurrent()
     *                   .getBaseDirectory().getAbsolutePath();
     *
     * // Image as a file resource
     * FileResource resource = new FileResource(new File(basepath +
     *                         "/WEB-INF/images/image.png"));
     *
     * // Show the image in the application
     * Image image = new Image("Image from file", resource);
     *
     * // Let the user view the file in browser or download it
     * Link link = new Link("Link to the image file", resource);
     * @param buttonClickEvent
     * @throws Exception
     */

    private void searchForData(ClickEvent<Button> buttonClickEvent) throws Exception {
        // create url according to search filter
        String queryPath = createQueryUrl();
        URL url = new URL(queryPath);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        String rkiString = parseJson(con);
        ObjectMapper mapper = new ObjectMapper();
//        RkiData rkiData = mapper.readValue(rkiString, RkiData.class);


//        fillGrid(rkiData.getFeatures());

//        add("Neue Fälle in " + filterForBundesland.getValue() + " " + rkiData.getFeatures().size());
    }

    private String createQueryUrl() {
        String bundeslandFilter = filterForCountry.getValue().toString();
        if (bundeslandFilter.length() != 0) {
            String upperCase = bundeslandFilter.toUpperCase();
            String formattedQuery = "where=Bundesland%20%3D%20'" + upperCase + "'";
//            return basePath.replace(defaultQueryValue, formattedQuery);
        } else {
            return basePath;
        }
        return "";
    }

    private String parseJson(HttpURLConnection con) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

        StringBuilder result = new StringBuilder();

        String output;
        log.info("Reading data from RKI");
        while ((output = br.readLine()) != null) {
            result.append(output);
        }

        return result.toString();
    }

    @Override
    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
        if (buttonClickEvent.getSource().equals(searchButton)) {
            clearGUI();
            if (filterForCountry.getValue() != null) {
                String searchCountry = filterForCountry.getValue().toString().toLowerCase();
                List<DayCountryEntity> rkiData = rkiService.getDataByCountryDayOne(searchCountry);
                fillGrid(rkiData);
            } else {
                Notification notification = new Notification(NO_FILTER_SELECTED);
                notification.open();
            }
        }
    }

    private void clearGUI() {
        if (rkiDataGrid != null) {
            remove(dashboard);
            remove(datePicker);
            remove(rkiDataGrid);
        }
    }

    private void fillGrid(List<DayCountryEntity> rkiData) {
        createDashboard(rkiData);

        rkiDataGrid = new Grid<>(DayCountryEntity.class);
        ListDataProvider<DayCountryEntity> dataProvider = new ListDataProvider<>(rkiData);
        rkiDataGrid.setDataProvider(dataProvider);
        rkiDataGrid.setColumns("date", "active", "confirmed", "deaths", "recovered");

        // add filter possibility for date
         datePicker = new DatePicker("Filter by date: ");
        datePicker.addValueChangeListener(event -> {
           applyFilter(dataProvider);
        });

        add(datePicker);
        add(rkiDataGrid);
    }

    private void createDashboard(List<DayCountryEntity> rkiData) {
        String currentCountry = filterForCountry.getValue().toString();
        Label firstInfection = new Label("First infection in " + currentCountry + " occurred on " + rkiData.get(0).getDate() + ".");
        Label overallDeaths = new Label("Overall " + rkiData.get(rkiData.size() - 1).getDeaths() + " people died in " + currentCountry + ".");
        dashboard = new VerticalLayout();
        dashboard.add(firstInfection, overallDeaths);
        add(dashboard);
    }

    private void applyFilter(ListDataProvider<DayCountryEntity> dataProvider) {
        dataProvider.clearFilters();
        if (datePicker.getValue() != null) {
            dataProvider.addFilter(entity -> {
                LocalDate entityDate = entity.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return Objects.equals(datePicker.getValue(), entityDate);
            });
        }
    }


    /**

    private EmployeeRepository employeeRepository;

    private EmployeeEditor editor;

    Grid<Employee> grid;

    TextField filter;

    private Button addButton;

    public MainView(EmployeeRepository repo, EmployeeEditor editor) {
        this.employeeRepository = repo;
        this.editor = editor;
        this.grid = new Grid<>(Employee.class);
        this.filter = new TextField();
        this.addButton = new Button("New employee", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addButton);
        add(actions, grid, editor);

        grid.setHeight("200px");
        grid.setColumns("id", "firstName", "lastName");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Filter by last name");

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listEmployees(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editEmployee(e.getValue());
        });

        addButton.addClickListener(e -> editor.editEmployee(new Employee("", "")));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listEmployees(filter.getValue());
        });

        listEmployees(null);
    }

    void listEmployees(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(employeeRepository.findAll());
        } else {
            grid.setItems(employeeRepository.findByLastNameStartsWithIgnoreCase(filterText));
        }
    }

     */

}

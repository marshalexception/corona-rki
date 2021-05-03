package com.sitrumm.coronarki.gui.views;

import com.sitrumm.coronarki.service.CovidService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Route
@Theme(value = Lumo.class, variant = Lumo.DARK)
@Slf4j
public class MainView extends VerticalLayout implements AppShellConfigurator {

    @Autowired
    private CovidService covidService;


    private final SummaryView summaryView;

    private final CountryAndDateView countryAndDateView;

    public MainView() {
        H1 title = new H1("RKI Data Application");
        HorizontalLayout overview = new HorizontalLayout(title);
        add(overview);

        Text description = new Text("This application allows you to display the present worldwide data regarding the SARS-CoV-2.");
        Icon database = new Icon(VaadinIcon.DATABASE);
        add(new HorizontalLayout(database, description));

        // global summary
        summaryView = new SummaryView();
        add(summaryView);

        // search for country and date
        countryAndDateView = new CountryAndDateView();
        add(countryAndDateView);
    }

    /**
     * prevents NullPointerException, because autowired bean is injected after constructor
     */
    @PostConstruct
    private void fillGUI() {
        summaryView.fillGUI(covidService);
        countryAndDateView.setService(covidService);
    }

}

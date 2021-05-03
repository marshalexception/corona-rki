package com.sitrumm.coronarki.gui.views;

import com.sitrumm.coronarki.model.DayCountryEntity;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisTitle;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataLabels;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.PlotOptionsBar;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.VerticalAlign;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CountryChartView extends Chart {

    private final List<DayCountryEntity> rkiData;

    public CountryChartView(List<DayCountryEntity> rkiData) {
        this.rkiData = rkiData;
        createBarChart(14L);
    }

    private void createBarChart(Long numberOfDays) {
        Configuration configuration = getConfiguration();
        configuration.setTitle("Corona virus data for " + rkiData.get(0).getCountry());
        getConfiguration().getChart().setType(ChartType.LINE);

        configuration.addSeries(new ListSeries("Infections", getInfections(numberOfDays + 1)));
//        configuration.addSeries(new ListSeries("Deaths", getDeaths(numberOfDays + 1)));
        configuration.addSeries(new ListSeries("Recovered", getRecovered(numberOfDays + 1)));
//        configuration.addSeries(new ListSeries("Active", getActive(numberOfDays + 1)));

        XAxis x = new XAxis();
        x.setCategories(getDates(numberOfDays + 1));
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        AxisTitle yTitle = new AxisTitle();
        yTitle.setText("Population");
        yTitle.setAlign(VerticalAlign.HIGH);
        y.setTitle(yTitle);
        configuration.addyAxis(y);

        Tooltip tooltip = new Tooltip();
        configuration.setTooltip(tooltip);

        PlotOptionsBar plotOptions = new PlotOptionsBar();
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        plotOptions.setDataLabels(dataLabels);
        configuration.setPlotOptions(plotOptions);
    }

    private Long[] getInfections(long days) {
        ArrayList<Long> infections = new ArrayList<>();

        Observable.fromIterable(rkiData)
                .filter(entry -> entry.getDate().isAfter(LocalDate.now().minusDays(days)))
                .subscribe(
                        entry -> infections.add(entry.getConfirmed()),
                        throwable -> log.error(throwable.getMessage()),
                        () -> log.info("[CountryChart] - Completed creating line for infections")
                );

        return infections.toArray(new Long[0]);
    }

    private Long[] getDeaths(long days) {
        ArrayList<Long> deaths = new ArrayList<>();

        Observable.fromIterable(rkiData)
                .filter(entry -> entry.getDate().isAfter(LocalDate.now().minusDays(days)))
                .subscribe(
                        entry -> deaths.add(entry.getDeaths()),
                        throwable -> log.error(throwable.getMessage()),
                        () -> log.info("[CountryChart] - Completed creating line for deaths")
                );

        return deaths.toArray(new Long[0]);
    }

    private Long[] getRecovered(long days) {
        ArrayList<Long> recovered = new ArrayList<>();

        Observable.fromIterable(rkiData)
                .filter(entry -> entry.getDate().isAfter(LocalDate.now().minusDays(days)))
                .subscribe(
                        entry -> recovered.add(entry.getRecovered()),
                        throwable -> log.error(throwable.getMessage()),
                        () -> log.info("[CountryChart] - Completed creating line for recovered")
                );

        return recovered.toArray(new Long[0]);
    }

    private Long[] getActive(long days) {
        ArrayList<Long> active = new ArrayList<>();

        Observable.fromIterable(rkiData)
                .filter(entry -> entry.getDate().isAfter(LocalDate.now().minusDays(days)))
                .subscribe(
                        entry -> active.add(entry.getActive()),
                        throwable -> log.error(throwable.getMessage()),
                        () -> log.info("[CountryChart] - Completed creating line for active")
                );

        return active.toArray(new Long[0]);
    }

    private String[] getDates(Long days) {
        ArrayList<String> dates = new ArrayList<>();

        Observable.fromIterable(rkiData)
                .filter(entry -> entry.getDate().isAfter(LocalDate.now().minusDays(days)))
                .subscribe(
                        entry -> dates.add(entry.getDate().toString()),
                        throwable -> log.error(throwable.getMessage()),
                        () -> log.info("[CountryChart] - Completed creating x-axis with dates")
                );

        return dates.toArray(new String[0]);
    }
}

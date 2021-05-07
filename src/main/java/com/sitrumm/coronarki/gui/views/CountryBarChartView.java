package com.sitrumm.coronarki.gui.views;

import com.sitrumm.coronarki.model.DayCountryEntity;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataLabels;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.PlotOptionsBar;
import com.vaadin.flow.component.charts.model.Tooltip;
import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.sitrumm.coronarki.gui.helper.ChartHelper.calculateDayToDayDifference;
import static com.sitrumm.coronarki.gui.helper.ChartHelper.getXAxis;
import static com.sitrumm.coronarki.gui.helper.ChartHelper.getYAxis;

@Slf4j
public class CountryBarChartView extends Chart {

    private final List<DayCountryEntity> rkiData;

    public CountryBarChartView(List<DayCountryEntity> rkiData, Long days) {
        this.rkiData = rkiData;
        createBarChart(days, rkiData.get(0).getCountry());
    }

    @SuppressWarnings("Duplicates")
    private void createBarChart(Long days, String country) {
        Configuration configuration = getConfiguration();
        configuration.setTitle("Corona virus data for " + country);
        getConfiguration().getChart().setType(ChartType.COLUMN);

        configuration.addSeries(new ListSeries("New Infections", getInfections(days + 1)));
        configuration.addSeries(new ListSeries("Recovered", getRecovered(days + 1)));
        configuration.addSeries(new ListSeries("Active", getActive(days + 1)));

        configuration.addxAxis(getXAxis(days, rkiData));
        configuration.addyAxis(getYAxis());

        Tooltip tooltip = new Tooltip();
        configuration.setTooltip(tooltip);

        PlotOptionsBar plotOptions = new PlotOptionsBar();
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        plotOptions.setDataLabels(dataLabels);
        configuration.setPlotOptions(plotOptions);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Long[] getInfections(long days) {
        ArrayList<Long> infections = new ArrayList<>();

        Observable.fromIterable(rkiData)
                .filter(entry -> entry.getDate().isAfter(LocalDate.now().minusDays(days + 1)))
                .subscribe(
                        entry -> infections.add(entry.getConfirmed()),
                        throwable -> log.error(throwable.getMessage()),
                        () -> log.info("[CountryChart] - Completed creating line for infections")
                );

        return calculateDayToDayDifference(infections);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Long[] getRecovered(long days) {
        ArrayList<Long> recovered = new ArrayList<>();

        Observable.fromIterable(rkiData)
                .filter(entry -> entry.getDate().isAfter(LocalDate.now().minusDays(days)))
                .subscribe(
                        entry -> recovered.add(entry.getRecovered()),
                        throwable -> log.error(throwable.getMessage()),
                        () -> log.info("[CountryChart] - Completed creating line for recovered")
                );

        return calculateDayToDayDifference(recovered);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Long[] getActive(long days) {
        ArrayList<Long> active = new ArrayList<>();

        Observable.fromIterable(rkiData)
                .filter(entry -> entry.getDate().isAfter(LocalDate.now().minusDays(days)))
                .subscribe(
                        entry -> active.add(entry.getActive()),
                        throwable -> log.error(throwable.getMessage()),
                        () -> log.info("[CountryChart] - Completed creating line for active")
                );

        return calculateDayToDayDifference(active);
    }

}

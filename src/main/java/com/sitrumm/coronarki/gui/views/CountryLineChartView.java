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
public class CountryLineChartView extends Chart {

    private final List<DayCountryEntity> rkiData;

    public CountryLineChartView(List<DayCountryEntity> rkiData, Long days) {
        this.rkiData = rkiData;
        createLineChart(days, rkiData.get(0).getCountry());
    }

    @SuppressWarnings("Duplicates")
    private void createLineChart(Long days, String country) {
        Configuration configuration = getConfiguration();
        configuration.setTitle("Deaths in " + country);
        getConfiguration().getChart().setType(ChartType.LINE);

        configuration.addSeries(new ListSeries("Deaths", getDeaths(days + 1)));

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

    private Long[] getDeaths(long days) {
        ArrayList<Long> deaths = new ArrayList<>();

        Observable.fromIterable(rkiData)
                .filter(entry -> entry.getDate().isAfter(LocalDate.now().minusDays(days)))
                .subscribe(
                        entry -> deaths.add(entry.getDeaths()),
                        throwable -> log.error(throwable.getMessage()),
                        () -> log.info("[CountryChart] - Completed creating line for deaths")
                );

        return calculateDayToDayDifference(deaths);
    }
}

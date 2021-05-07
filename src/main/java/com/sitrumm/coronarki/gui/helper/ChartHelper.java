package com.sitrumm.coronarki.gui.helper;

import com.sitrumm.coronarki.model.DayCountryEntity;
import com.vaadin.flow.component.charts.model.AxisTitle;
import com.vaadin.flow.component.charts.model.VerticalAlign;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ChartHelper {

    private ChartHelper() { }

    public static XAxis getXAxis(Long days, List<DayCountryEntity> rkiData) {
        XAxis x = new XAxis();
        x.setCategories(getDates(days + 1, rkiData));
        return x;
    }

    public static YAxis getYAxis() {
        YAxis y = new YAxis();
        y.setMin(0);
        AxisTitle yTitle = new AxisTitle();
        yTitle.setText("Population");
        yTitle.setAlign(VerticalAlign.HIGH);
        y.setTitle(yTitle);
        return y;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String[] getDates(Long days, List<DayCountryEntity> rkiData) {
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

    public static Long[] calculateDayToDayDifference(List<Long> arrayList) {
        Long[] dayToDayArray = new Long[arrayList.size() - 1];

        for (int i = 0; i < dayToDayArray.length; i++) {
            dayToDayArray[i] = arrayList.get(i + 1) - arrayList.get(i);
        }

        return dayToDayArray;
    }

}

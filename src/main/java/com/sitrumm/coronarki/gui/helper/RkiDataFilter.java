package com.sitrumm.coronarki.gui.helper;

import com.sitrumm.coronarki.model.DayCountryEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class RkiDataFilter {

    String date;

    public boolean test(DayCountryEntity entity) {
        if (date.length() > 0 && !StringUtils.containsIgnoreCase(String.valueOf(entity.getDate()), date)) {
            return false;
        }
        return true;
    }
}

package io.discount.app.managers;

import java.util.ArrayList;

import io.discount.app.models.Discount;

/**
 * Created by Jan on 24.3.14.
 */
public interface IDiscountManager extends IBaseManager<Discount> {
    ArrayList<Discount> getByGPS(String gps);
}

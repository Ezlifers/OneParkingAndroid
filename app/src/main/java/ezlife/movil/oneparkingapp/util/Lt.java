package ezlife.movil.oneparkingapp.util;

import java.util.ArrayList;
import java.util.List;

import ezlife.movil.oneparkingapp.net.models.Car;

/**
 * Created by Dario Chamorro on 25/09/2016.
 */

public class Lt {

    private static List<Car> cars;
    public static List<Car> getCars() {
        if (cars == null)
            cars = new ArrayList<>();
        return cars;
    }

}

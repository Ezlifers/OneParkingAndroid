package ezlife.movil.oneparkingapp.net.api.car;

/**
 * Created by Dario Chamorro on 26/09/2016.
 */

public class CarRes {

    boolean success, outRange;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isOutRange() {
        return outRange;
    }

    public void setOutRange(boolean outRange) {
        this.outRange = outRange;
    }
}

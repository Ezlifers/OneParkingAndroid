package ezlife.movil.oneparkingapp.net.api.login;

import java.util.List;

import ezlife.movil.oneparkingapp.net.models.Car;
import ezlife.movil.oneparkingapp.net.models.User;

/**
 * Created by Dario Chamorro on 25/09/2016.
 */

public class ClientRes {

    boolean success;
    Client usuario;
    String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Client getUsuario() {
        return usuario;
    }

    public void setUsuario(Client usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class Client extends User{
        private String _id;
        boolean validado;
        List<Car> vehiculos;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public boolean isValidado() {
            return validado;
        }

        public void setValidado(boolean validado) {
            this.validado = validado;
        }

        public List<Car> getVehiculos() {
            return vehiculos;
        }

        public void setVehiculos(List<Car> vehiculos) {
            this.vehiculos = vehiculos;
        }
    }

}

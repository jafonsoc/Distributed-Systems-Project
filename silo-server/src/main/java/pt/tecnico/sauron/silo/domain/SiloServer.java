package pt.tecnico.sauron.silo.domain;

import pt.tecnico.sauron.silo.grpc.Silo.ResponseMessage;

import java.lang.String;
import java.util.ArrayList;

public class SiloServer {
    private ArrayList<Camera> _cameras;
    private ArrayList<Car> _observations;

    public SiloServer() {
        _cameras = new ArrayList<>();
        _observations = new ArrayList<>();
    }

    @Override
    public synchronized String toString() {
        return "hey";
    }

    public ResponseMessage play() {
        return ResponseMessage.CLEAR_FAIL;

    }
}

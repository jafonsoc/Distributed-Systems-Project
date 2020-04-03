package pt.tecnico.sauron.silo.domain;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.lang.String;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SiloServer {
    private Map<String, Camera> _cameras;
    private Map<String, LinkedList<Observation>> _cars;
    private Map<String, LinkedList<Observation>> _people;

    public SiloServer() {
        _cameras = new HashMap<>();
        _cars = Collections.synchronizedMap(new LinkedHashMap<>());
        _people = Collections.synchronizedMap(new LinkedHashMap<>());
    }

    // Track command
    public Observation trackPerson(String id) {
        if (!Person.isValidId(id))
            throw new SiloException(ErrorMessage.INVALID_PERSON_ID);
        return track(_people, id);
    }

    public Observation trackCar(String id) {
        if (!Car.isValidId(id))
            throw new SiloException(ErrorMessage.INAVLID_CAR_ID);
        return track(_cars, id);
    }

    // Having a generic method for command allows for adding more types of
    // observations in the future
    private Observation track(Map<String, LinkedList<Observation>> observations, String id) {
        if (observations.containsKey(id)) {
            LinkedList<Observation> list = observations.get(id);

            // sorting is handled in insertion so the first element is always the most
            // recent
            return list != null ? list.get(0) : null;
        }

        return null;
    }

    // Trace command
    public List<Observation> tracePerson(String id) {
        if (!Person.isValidId(id))
            throw new SiloException(ErrorMessage.INVALID_PERSON_ID);
        return _people.get(id);
    }

    public List<Observation> traceCar(String id) {
        if (!Car.isValidId(id))
            throw new SiloException(ErrorMessage.INAVLID_CAR_ID);
        return _cars.get(id);
    }

    // Track Match command
    public List<Observation> trackMatchPerson(String id) {
        return trackMatch(_people, id);
    }

    public List<Observation> trackMatchCar(String id) {
        return trackMatch(_cars, id);
    }

    // Having a generic method for command allows for adding more types of
    // observations in the future
    private List<Observation> trackMatch(Map<String, LinkedList<Observation>> observations, String expr) {
        List<Observation> list = new LinkedList<>();

        if (!expr.contains("*")) {
            for (String id : observations.keySet()) {
                if (id.contains(expr)) {
                    // sorting is handled in insertion so the first element is always most recent
                    list.add(observations.get(id).get(0));
                }
            }
        } else {
            // since split method uses regex it's better to not use a special regex
            // character
            String[] patterns = expr.replace("*", "@").split("@", -1);

            for (String id : observations.keySet()) {
                if (id.startsWith(patterns[0]) && id.endsWith(patterns[1])) {
                    // sorting is handled in insertion so the first element is always most recent
                    list.add(observations.get(id).get(0));
                }
            }
        }

        return list;
    }

    // TODO: Create custom exception
    public boolean registerCamera(String name, double longitude, double latitude) {
        if (_cameras.containsKey(name))
            throw new SiloException(ErrorMessage.CAMERA_ALREADY_EXISTS);
        _cameras.put(name, new Camera(name, longitude, latitude));
        return true;
    }

    public void reportObservation(String cameraName, String type, String id) {
        Camera camera = this._cameras.get(cameraName);
        switch (type) {
            case "person":
                Person person = new Person(id, camera);
                if (this._people.containsKey(id)) {
                    // Just add the observation
                    this._people.get(id).addFirst(person);
                } else {
                    // Create new entry in map
                    LinkedList<Observation> observations = new LinkedList<Observation>();
                    observations.addFirst(person);
                    this._people.put(id, observations);
                }
                break;
            case "car":
                Car car = new Car(id, camera);
                if (this._cars.containsKey(id)) {
                    // Just add the observation
                    this._cars.get(id).addFirst(car);;
                } else {
                    // Create new entry in map
                    LinkedList<Observation> observations = new LinkedList<Observation>();
                    observations.addFirst(car);
                    this._cars.put(id, observations);
                }
                break;
            default:
                break;
        }
    }

    public Camera camInfo(String name) {
        return _cameras.get(name);
    }

    public void clear() {
        _cameras = Collections.synchronizedMap(new HashMap<>());
        _cars = Collections.synchronizedMap(new LinkedHashMap<>());
        _people = Collections.synchronizedMap(new LinkedHashMap<>());
    }

    public Map<String, Camera> getCameras() {
        return this._cameras;
    }

    public void setCameras(Map<String, Camera> _cameras) {
        this._cameras = _cameras;
    }

    public Map<String, LinkedList<Observation>> getCars() {
        return this._cars;
    }

    public void setCars(Map<String, LinkedList<Observation>> _cars) {
        this._cars = _cars;
    }

    public Map<String, LinkedList<Observation>> getPeople() {
        return this._people;
    }

    public void setPeople(Map<String, LinkedList<Observation>> _people) {
        this._people = _people;
    }

    public boolean hasCamera(String name) {
        return this._cameras.containsKey(name);
    }

    public boolean isValidId(String type, String id) {
        switch (type) {
            case "person":
                return Person.isValidId(id);
            case "car":
                return Car.isValidId(id);
            default:
                return false;
        }
    }

    public boolean isValidType(String type) {
        return type.equals("person") || type.equals("car");
    }

}

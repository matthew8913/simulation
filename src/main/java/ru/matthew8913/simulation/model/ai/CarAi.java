package ru.matthew8913.simulation.model.ai;

import ru.matthew8913.simulation.model.Habitat;
import ru.matthew8913.simulation.model.helpers.Point;
import ru.matthew8913.simulation.model.VehicleList;
import ru.matthew8913.simulation.model.vehicles.Car;
import ru.matthew8913.simulation.model.vehicles.Vehicle;

public class CarAi extends BaseAi{

    public CarAi() {
        super();
    }

    @Override
    public void move() {
        synchronized (VehicleList.getInstance().getVehicles()) {
            for(Vehicle v:VehicleList.getInstance().getVehicles()){
                if(v instanceof Car){
                    v.move();
                }
            }
        }
    }

    @Override
    public void generateMoveVectors() {
        for(Vehicle v : VehicleList.getInstance().getVehicles()){
            if(v instanceof Car){
                if(v.getMoveVector()==null){
                    int xStart = (int)v.getCoordinates().x();
                    int yStart = (int)v.getCoordinates().y();
                    if(xStart < Habitat.WIDTH/2 || yStart < Habitat.HEIGHT/2){
                        int xEnd = Habitat.WIDTH - (int)(Math.random()*Habitat.WIDTH/2);
                        int yEnd = Habitat.HEIGHT - (int)(Math.random()*Habitat.HEIGHT/2);

                        double dx = xEnd - xStart;
                        double dy = yEnd - yStart;
                        double length =Math.sqrt(dx * dx + dy * dy);

                        double nx = (dx / length);
                        double ny = (dy / length);

                        v.setEndPoint(new Point(xEnd, yEnd));
                        v.setMoveVector(new Point(nx,ny));
                    }
                }

            }
        }
    }
}

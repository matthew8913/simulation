package ru.matthew8913.simulation.model;


import ru.matthew8913.simulation.model.vehicles.Truck;
import ru.matthew8913.simulation.model.vehicles.Vehicle;


public class TruckAi extends BaseAi {
    public TruckAi() {
        super();
    }

    @Override
    public void move() {
        synchronized (vehicleList) {
            for(Vehicle v:vehicleList.getVehicles()){
                if(v instanceof Truck){
                    v.move();
                }
            }
        }
    }


    @Override
    public void generateMoveVectors() {
        for(Vehicle v : vehicleList.getVehicles()){
            if(v instanceof Truck){
                if(v.getMoveVector()==null){
                    int xStart = (int)v.getCoordinates().x();
                    int yStart = (int)v.getCoordinates().y();
                    if(xStart > Habitat.WIDTH/2 || yStart > Habitat.HEIGHT/2){
                        int xEnd = (int)(Math.random()*Habitat.WIDTH/2);
                        int yEnd = (int)(Math.random()*Habitat.HEIGHT/2);

                        double dx = xEnd - xStart;
                        double dy = yEnd - yStart;
                        double length = Math.sqrt(dx * dx + dy * dy);

                        double nx = (dx * 2 / length);
                        double ny = (dy * 2 / length);

                        v.setEndPoint(new Point(xEnd, yEnd));
                        v.setMoveVector(new Point(nx,ny));
                    }
                }

            }
        }
    }


}

package model;

public class FreeRoom extends Room{

    public FreeRoom(String roomNumber,RoomType roomType){
        super(roomNumber,0.0,roomType);
    }

    @Override
    public String toString() {
        return "Room number "+super.getRoomNumber()+" is free "+super.getRoomType()+" room";
    }
}

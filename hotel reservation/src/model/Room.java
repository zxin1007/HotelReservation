package model;

public class Room implements IRoom{

    private final String roomNumber;
    private final Double price;
    private final RoomType roomType;

    public Room(String roomNumber,Double price,RoomType roomType){
        if (price<0){
            throw new IllegalArgumentException("price can not be negative");
        }
        this.roomNumber=roomNumber;
        this.price=price;
        this.roomType=roomType;
    }


    public String getRoomNumber(){return this.roomNumber;}
    public Double getRoomPrice(){return this.price;}
    public RoomType getRoomType(){return this.roomType;}
    public boolean isFree(){return price==0;}
    @Override
    public String toString(){return "Room Number: "+roomNumber+" Price: "+price+" Room Type: "+roomType;}

    @Override
    public int hashCode() {
        long temp=Double.doubleToLongBits(Double.parseDouble(roomNumber));
        return (int)(temp^(temp>>>32));
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj){
            return true;
        }
        if (obj==null||getClass()!=obj.getClass()){
            return false;
        }
        Room another = (Room) obj;
        return this.roomNumber.equals(another.roomNumber);
    }

}

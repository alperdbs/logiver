package com.alpertunademirbas.nakile;

public class Transfer {
    private  Integer id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String transferDate;
    private String nameSurname;
    private String transferLocation;
    private String transferPrice;
    private String character;

    public Transfer(String name, String surname, String phoneNumber, String transferDate, String nameSurname, String transferLocation, String transferPrice, String character) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.transferDate = transferDate;
        this.nameSurname = nameSurname;
        this.transferLocation = transferLocation;
        this.transferPrice = transferPrice;
        this.character = character;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public String getTransferLocation() {
        return transferLocation;
    }

    public String getTransferPrice() {
        return transferPrice;
    }

    public String getCharacter() {
        return character;
    }
}

package com.fueltracker.dto.Api;

public record ApiStation(
      String id,
      String name,
      String brand,
      String street,
      String place,
      double lat,
      double lng,
      double distance,
      //Optionals
      Double diesel,
      Double e5,
      Double e10,

      boolean isOpen,
      String houseNumber,
      Integer postCode
) {
}

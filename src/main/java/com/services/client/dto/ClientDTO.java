package com.services.client.dto;


import com.services.client.enums.HorsemanStatus;
import com.services.client.enums.SportsCategory;
import lombok.Data;


@Data
public class ClientDTO {
    private final String name;
    private final SportsCategory sportsCategory;
    private final HorsemanStatus horsemanStatus;

}

package com.services.client.controller.rest.dto;


import com.services.client.entity.enums.HorsemanStatus;
import com.services.client.entity.enums.SportsCategory;
import lombok.Data;


@Data
public class ClientDTO {
    private final String name;
    private final SportsCategory sportsCategory;
    private final HorsemanStatus horsemanStatus;

}

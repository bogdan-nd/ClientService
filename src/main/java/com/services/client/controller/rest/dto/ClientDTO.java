package com.services.client.controller.rest.dto;


import com.services.client.entity.enums.HorsemanStatus;
import com.services.client.entity.enums.SportsCategory;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class ClientDTO {
    private String name;
    private SportsCategory sportsCategory;
    private HorsemanStatus horsemanStatus;

}

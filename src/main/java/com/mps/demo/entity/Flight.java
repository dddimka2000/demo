package com.mps.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "flight")
@Data
public class Flight {
    @Id
    private Long number;
    @DBRef(lazy = true)
    private Airplane airplane;
    private List<WayPoint> wayPoints;
    private List<TemporaryPoint> passedPoints;

}

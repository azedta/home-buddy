package com.azed.home_buddy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "robot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Robot {

    @Id
    private String robotName = "HomeBuddy-v1";

    private Integer batteryLevel;
    private String robotStatus;
    private String trayStatus;
    private String sensorStatus;
    private String dispenserStatus;

    @ElementCollection
    @CollectionTable(
            name = "robot_activities", // Name of the join table
            joinColumns = @JoinColumn(name = "robot_name") // Foreign key column to the owning entity
    )
    private Set<String> activity; // All the robot activity logs
}

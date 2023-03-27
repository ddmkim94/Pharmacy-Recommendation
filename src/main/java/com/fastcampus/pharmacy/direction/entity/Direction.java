package com.fastcampus.pharmacy.direction.entity;

import com.fastcampus.pharmacy.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "direction")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Direction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 고객의 주소 정보
    private String inputAddress;
    private double inputLatitude;
    private double inputLongitude;

    // 약국의 주소 정보
    private String targetAddress;
    private double targetLatitude;
    private double targetLongitude;
    private String targetPharmacyName;

    // 고객 주소 와 약국 주소 사이의 거리
    private double distance;
}

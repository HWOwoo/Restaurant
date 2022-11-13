package com.example.restaurant.wishlist.entity;

import com.example.restaurant.db.MemoryDbEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WishListEntity extends MemoryDbEntity {

    private String title;           // 음식명, 장소명
    private String category;        // 카테고리
    private String address;         // 주소
    private String readAddress;     // 도로명
    private String homePageLink;    // 홈페이지 주소
    private String imageLink;       // 음식,가게 이미지 주소
    private boolean isVisit;        // 방문 여부
    private int visitCount;         // 방문 횟수
    private LocalDateTime lastVisitDate; // 방문 마지막 일자

}

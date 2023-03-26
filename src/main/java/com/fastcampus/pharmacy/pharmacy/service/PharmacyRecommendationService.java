package com.fastcampus.pharmacy.pharmacy.service;

import com.fastcampus.pharmacy.api.dto.DocumentDto;
import com.fastcampus.pharmacy.api.dto.KakaoApiResponseDto;
import com.fastcampus.pharmacy.api.service.KakaoAddressSearchService;
import com.fastcampus.pharmacy.direction.entity.Direction;
import com.fastcampus.pharmacy.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    public void recommendPharmacyList(String address) {

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[PharmacyRecommendationService recommendPharmacyList fail] Input address: {}", address);
            return;
        }

        // 카카오 API 에서 받은 주소 중 첫 번째 주소 정보를 가져옴
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        // 해당 주소와 약국들의 주소를 이용해서 거리 계산을 한 후 가장 가까운 약국 3개를 리스트로 반환
        List<Direction> directionList = directionService.buildDirectionList(documentDto);

        // 받아온 약국 정보를 데이터베이스에 저장
        directionService.saveAll(directionList);
    }
}

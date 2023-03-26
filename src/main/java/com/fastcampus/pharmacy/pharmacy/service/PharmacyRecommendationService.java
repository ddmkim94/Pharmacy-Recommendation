package com.fastcampus.pharmacy.pharmacy.service;

import com.fastcampus.pharmacy.api.dto.DocumentDto;
import com.fastcampus.pharmacy.api.dto.KakaoApiResponseDto;
import com.fastcampus.pharmacy.api.service.KakaoAddressSearchService;
import com.fastcampus.pharmacy.direction.dto.OutputDto;
import com.fastcampus.pharmacy.direction.entity.Direction;
import com.fastcampus.pharmacy.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    public List<OutputDto> recommendPharmacyList(String address) {

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[PharmacyRecommendationService recommendPharmacyList fail] Input address: {}", address);
            return Collections.emptyList();
        }

        // 카카오 API 에서 받은 주소 중 첫 번째 주소 정보를 가져옴
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        // 해당 주소와 약국들의 주소를 이용해서 거리 계산을 한 후 가장 가까운 약국 3개를 리스트로 반환
        // List<Direction> directionList = directionService.buildDirectionList(documentDto);
        
        // 카카오 API 사용!!
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);

        // 받아온 약국 정보를 데이터베이스에 저장
        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction) {

        String params = String.join(",", direction.getTargetPharmacyName(),
                String.valueOf(direction.getTargetLatitude()),
                String.valueOf(direction.getTargetLongitude()));

        /**
         * 한글 이름 인코딩
         * KakaoUriBuilderService 와 다르게 String 으로 바로 받는 이유는
         * RestTemplate 를 사용하지 않기 때문이다.
         * url 을 String 으로 줄 수 있지만 RestTemplate 내부에서도 인코딩을 진행하기 때문에 문제가 발생할 수도 있음
          */
        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params).toUriString();

        log.info("direction params: {} url: {}", params, result);

        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(result)
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}

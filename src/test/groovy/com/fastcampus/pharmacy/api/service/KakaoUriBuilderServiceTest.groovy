package com.fastcampus.pharmacy.api.service

import spock.lang.Specification

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class KakaoUriBuilderServiceTest extends Specification {

    private KakaoUriBuilderService kakaoUriBuilderService

    def setup() {
        kakaoUriBuilderService = new KakaoUriBuilderService()
    }

    def "buildUriByAddressSearch - 한글 파라미터의 인코딩이 정상적으로 동작하나?"() {
        given:
        String address = "서울 동대문구"
        def charset = StandardCharsets.UTF_8

        when:
        def uri = kakaoUriBuilderService.buildUriByAddressSearch(address)
        def decodedResult = URLDecoder.decode(uri.toString(), charset)

        then:
        decodedResult == "https://dapi.kakao.com/v2/local/search/address.json?query=서울 동대문구"
    }


}

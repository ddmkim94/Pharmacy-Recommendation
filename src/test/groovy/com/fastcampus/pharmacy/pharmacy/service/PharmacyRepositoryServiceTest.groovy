package com.fastcampus.pharmacy.pharmacy.service

import com.fastcampus.pharmacy.AbstractIntegrationContainerBaseTest
import com.fastcampus.pharmacy.pharmacy.entity.Pharmacy
import com.fastcampus.pharmacy.pharmacy.repository.PharmacyRepository
import org.springframework.beans.factory.annotation.Autowired

class PharmacyRepositoryServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private PharmacyRepository pharmacyRepository

    @Autowired
    private PharmacyRepositoryService pharmacyRepositoryService

    // 테스트 시작전에 DB 초기화
    def setup() {
        pharmacyRepository.deleteAll()
    }

    def "PharmacyRepository update - dirty checking success!"() {
        given:
        String address = "서울 특별시 성북구 종암동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = "은혜 약국"

        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(address)
                .pharmacyName(name)
                .build()

        when:
        Pharmacy save = pharmacyRepository.save(pharmacy)
        pharmacyRepositoryService.updateAddress(save.getId(), modifiedAddress)

        def result = pharmacyRepository.findAll()

        then:
        result.get(0).getPharmacyAddress() == modifiedAddress
    }

    def "PharmacyRepository update - dirty checking fail!"() {
        given:
        String address = "서울 특별시 성북구 종암동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = "은혜 약국"

        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(address)
                .pharmacyName(name)
                .build()

        when:
        Pharmacy save = pharmacyRepository.save(pharmacy)
        pharmacyRepositoryService.updateAddressWithoutTransaction(save.getId(), modifiedAddress)

        def result = pharmacyRepository.findAll()

        then:
        result.get(0).getPharmacyAddress() == address
    }

    def "transactional readOnly test - 읽기 전용일 경우 dirty checking 반영 되지 않는다. "() {

        given:
        String inputAddress = "서울 특별시 성북구"
        String modifiedAddress = "서울 특별시 광진구"
        String name = "은혜 약국"
        double latitude = 36.11
        double longitude = 128.11

        def input = Pharmacy.builder()
                .pharmacyAddress(inputAddress)
                .pharmacyName(name)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        def pharmacy = pharmacyRepository.save(input)
        pharmacyRepositoryService.startReadOnlyMethod(pharmacy.id)

        then:
        def result = pharmacyRepositoryService.findAll()
        result.get(0).getPharmacyAddress() == inputAddress
    }
}

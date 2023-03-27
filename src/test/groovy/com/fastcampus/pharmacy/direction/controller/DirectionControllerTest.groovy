package com.fastcampus.pharmacy.direction.controller

import com.fastcampus.pharmacy.direction.service.DirectionService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class DirectionControllerTest extends Specification {

    private MockMvc mockMvc

    private DirectionService directionService = Mock()

    def setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DirectionController(directionService)).build()
    }

    def "GET /dir/{encodedId}"() {
        given:
        String encodedId = "r"
        String redirectURL = "https://map.kakao.com/link/map/pharmacy,38.11,128.11"

        when:
        // 해당 메서드를 실행하면 redirectURL 을 반환한다.
        directionService.findDirectionUrlById(encodedId) >> redirectURL
        def result = mockMvc.perform(get("/dir/{encodedId}", encodedId))

        then:
        result
            .andExpect(handler().handlerType(DirectionController.class))
            .andExpect(status().is3xxRedirection()) // 리다이렉트 발생 확인
            .andExpect(redirectedUrl(redirectURL))
            .andDo(print())

    }


}

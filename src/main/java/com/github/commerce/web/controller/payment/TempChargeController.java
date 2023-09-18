//package com.github.commerce.web.controller.payment;
//
//import com.github.commerce.repository.user.UserDetailsImpl;
//import com.github.commerce.service.payment.TempChargeService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Api(tags = "임시 충전 API")
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/v1/api/charges")
//public class TempChargeController {
//
//    /*todo : role이 User인 경우만 충전, Seller 인경우 예외처리 | 추후 충전기능 완성 후 제거예정 */
//
//    private final TempChargeService tempChargeService;
//
//    @ApiOperation(value = "기존 DB에서 직접 페이머니 입력하지 않은 계정만 가능| 100만 단위충전")
//    @PostMapping("/temp-charge")
//    public ResponseEntity<?> tempCharge(
//            @AuthenticationPrincipal UserDetailsImpl userDetails
//    ) {
//        Long userId = userDetails.getUser().getId();
//        return ResponseEntity.ok(tempChargeService.tempCharge(userId));
//    }
//
//}

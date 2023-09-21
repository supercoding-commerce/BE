package com.github.commerce.service.product.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum  ProductErrorCode {
    //status(HttpStatus.BAD_REQUEST) 400
    TOO_MANY_FILES("본문에 삽입하 이미지 파일은 최대 5까지 가능합니다.",HttpStatus.BAD_REQUEST),
    NOT_IMAGE_EXTENSION("파일 확장자가 없습니다.",HttpStatus.BAD_REQUEST ),
    INVALID_FORMAT_FILE("올바른 파일 이름이 아닙니다", HttpStatus.BAD_REQUEST),
    INVALID_CATEGORY("카테고리 값이 잘못 입력되었습니다",HttpStatus.BAD_REQUEST  ),
    NOT_FOUND_SAVEDTHUMBNAILFILE("기존의 thumbnailfile url을 찾을 수 없습니다.",HttpStatus.BAD_REQUEST),
    //INVALID_IMAGE_NUMBER("")


    //403
    NOT_REGISTERED_SELLER("판매자로 등록된 유저가 아닙니다.", HttpStatus.FORBIDDEN),
    NOT_AUTHORIZED_SELLER("너가 등록한 상품이 아닙니다.", HttpStatus.FORBIDDEN),

    //404
    NOTFOUND_PRODUCT("상품을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    NOTFOUND_URL_IN_S3("s3에 존재하지 않는 상품 url입니다.",HttpStatus.BAD_REQUEST),
    NOT_FOUND_PRODUCT("존재하지 않는 상품입니다.",HttpStatus.NOT_FOUND),
    NOT_FOUND_FILE("파일이 존재하지 않습니다.",HttpStatus.NOT_FOUND),

    //422 요청은 유효하지만 서버에서 처리할 수 없는 상태에 있는 경우에 사용됩니다. 예를 들어, 요청이 유효하지만 업데이트를 수행할 수 없는 데이터를 포함하고 있는 경우에 이 코드를 반환할 수 있습니다.
    UNPROCESSABLE_ENTITY("요청이 유효하지만 업데이트를 수행할 수 없습니다.", HttpStatus.UNPROCESSABLE_ENTITY),

    //409
    FAIL_TO_SAVE( "서버 측의 문제로 데이터의 저장에 실패했습니다. 다시 한 번 시도해주세요.",HttpStatus.CONFLICT),

    //413
    HEAVY_FILESIZE("업로드할 이미지는 하나당 2MB 이하여야 합니다.", HttpStatus.PAYLOAD_TOO_LARGE );






    private final String description;
    private final HttpStatus httpStatus;
}

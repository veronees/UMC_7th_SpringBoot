package com.example.umc7.domain.review.controller;

import com.example.umc7.apipayload.ApiResponse;
import com.example.umc7.apipayload.PageResponseDTO;
import com.example.umc7.domain.membermission.dto.response.MemberMissionResponseDTO;
import com.example.umc7.domain.review.dto.request.CreateReviewDTO;
import com.example.umc7.domain.review.dto.response.ReviewResponseDTO;
import com.example.umc7.domain.review.service.ReviewCommandService;
import com.example.umc7.domain.review.service.ReviewQueryService;
import com.example.umc7.global.validation.annotation.CheckPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createReview(
        @RequestBody @Valid CreateReviewDTO createReviewDTO) {
        reviewCommandService.saveReview(createReviewDTO);
        return ResponseEntity.ok(ApiResponse.onSuccess("리뷰 작성 완료"));
    }


    @Operation(
        summary = "리뷰 목록 조회 API",
        description = "리뷰 목록 조회 API입니다. 해당 API는 사용자 인증이 요구됩니다.\n\n",
        parameters = {
            @Parameter(name = "page", description = "조회할 페이지 번호, 미입력시 기본값 1", required = false),
            @Parameter(name = "size", description = "조회할 페이지의 크기, 미입력시 기본값 10", required = false)
        },
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "COMMON200",
                description = "리뷰 목록을 페이징 처리해 반환",
                content = @Content(schema = @Schema(implementation = ReviewResponseDTO.class))
            )
        }
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<List<ReviewResponseDTO>>>> getReviewList(
        @CheckPage @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
            ApiResponse.onSuccess(reviewQueryService.getReviewList(page, size)));
    }


}
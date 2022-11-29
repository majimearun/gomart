package com.ecommerce.gomart.Stubs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class PostImage {
    private Long productId;
    private MultipartFile file;

}

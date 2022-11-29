package com.ecommerce.gomart.Stubs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
public class PostImage {
    private Long productId;
    private MultipartFile file;

}

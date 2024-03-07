package com.enigma.enigma_shop.dto.request;

import com.enigma.enigma_shop.entity.Image;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
/*
* Anotasi Validasi dari JPA/Hibernate
* @NotBlank -> Validasi tidak boleh kosong termasuk whitespace (spasi kosong)
* @NotNull -> Tidak boleh null/kosong (Number, Object)
* @Min -> Number untuk menentukan minimal angka yg harus di isi
* @Max -> Number untuk menentukan max angka yg harus di isi
* @Size -> Berapa banyak panjang character pada string (Parameternya ada min & max)
* @Email -> untuk menentukan email itu valid atau tidak
* */
public class NewProductRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "price is required")
    @Min(value = 0, message = "price must be greater than or equal 0")
    private Long price;

    @NotNull(message = "stock is required")
    @Min(value = 0, message = "stock must be greater than or equal 0")
    private Integer stock;
    private MultipartFile image;
}

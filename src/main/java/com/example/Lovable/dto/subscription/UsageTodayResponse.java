package com.example.Lovable.dto.subscription;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsageTodayResponse {

    Integer tokenUsed;
    Integer tokenLimit;
    Integer previewsRunning;
    Integer previewsLimit;
}

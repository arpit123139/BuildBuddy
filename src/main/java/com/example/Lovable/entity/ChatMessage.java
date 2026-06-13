package com.example.Lovable.entity;

import com.example.Lovable.enums.MessageRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {

    Long id;
    ChatSession chatSession;

    String content;

    MessageRole messageRole;

    String toolCalls;

    Integer tokensUsed;

    LocalDateTime createdAt;


}

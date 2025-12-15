package com.github.axinger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyChatRequest {

    private String message;

    private boolean stream = true;

    public Prompt toPrompt() {
        return new Prompt(new UserMessage(message));
    }
}

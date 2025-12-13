package com.github.axinger.views;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.axinger.model.Views;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @JsonView(Views.Public.class)
    private String username;

    @JsonView(Views.Internal.class)
    private String email;

    @JsonView(Views.Admin.class)
    private String passwordHash;

}

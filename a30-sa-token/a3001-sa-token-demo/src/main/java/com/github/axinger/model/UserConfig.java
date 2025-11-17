package com.github.axinger.model;

import com.github.axinger.model.entity.PathEntity;
import com.github.axinger.model.entity.UserInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "user-config")
public class UserConfig {

    private List<PathEntity> apiPathList;
    private List<PathEntity> ignorePathList;
    private List<PathEntity> matchPathList;
    private List<PathEntity> notMatchPathList;
    private List<UserInfoEntity> userList;

}

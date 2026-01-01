package com.meindonsa.tech_watch.config;

import com.meindonsa.tech_watch.source.CreateSourceView;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class GlobalAppConfig {
    List<CreateSourceView> sources;
}

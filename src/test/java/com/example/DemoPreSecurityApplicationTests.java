package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;

class DemoPreSecurityApplicationTests {

  @Test
  void contextLoads() {
    assertEquals("3.2.3", SpringBootVersion.getVersion());
  }
}

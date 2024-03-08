package com.example.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.model.Product;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(
    value = {"/send"},
    produces = APPLICATION_JSON_VALUE)
public class DemoController {

  @GetMapping(value = {"/product/{partnerId}/all"})
  @PreAuthorize(value = "hasPermission('Quotes', {#partnerId, #name})")
  public List<Product> all(
      @P("partnerId") @PathVariable(name = "partnerId") String partnerId,
      @P("name") @RequestParam("name") String name) {
    log.info("PartnerId: {} -- name: {}", partnerId, name);
    var p1 = new Product(101, "ASD");
    var p2 = new Product(102, "PQR");
    return List.of(p1, p2);
  }
}
